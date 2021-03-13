/**
 * Copyright 2018 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.viettel.tracing.diagnostic;

import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;

public class DiagnosticContextScopeManager implements ScopeManager {

  private final ScopeManager scopeManager;
  private final SpanDiagnosticContext spanDiagnosticContext;

  public DiagnosticContextScopeManager(ScopeManager scopeManager,
      SpanDiagnosticContext spanDiagnosticContext) {
    this.scopeManager = Objects.requireNonNull(scopeManager);
    this.spanDiagnosticContext = Objects.requireNonNull(spanDiagnosticContext);
  }

  @Override
  public Scope activate(Span span, boolean finishSpanOnClose) {
    // Activate scope
    Scope scope = scopeManager.activate(span, finishSpanOnClose);
    Map<String, String> context = spanDiagnosticContext.create(scope.span());
    span.setTag("traceId", context.get("traceId"));
    span.setTag("spanId", context.get("spanId"));

    // Return wrapper
    return new DiagnosticContextScope(scope, context);
  }

  @Nullable
  @Override
  public Scope active() {
    return scopeManager.active();
  }

  /**
   * Wrapper class for {@link Scope}, which also closes attached {@link SpanDiagnosticContext}.
   * <p>
   * Created by {@link DiagnosticContextScopeManager}.
   */
  public static class DiagnosticContextScope implements Scope {

    private final Scope scope;
    private final Map<String, String> previous = new HashMap<>();

    public DiagnosticContextScope(Scope scope, Map<String, String> context) {
      this.scope = scope;

      // Initialize MDC
      for (Map.Entry<String, String> entry : context.entrySet()) {
        this.previous.put(entry.getKey(), MDC.get(entry.getKey()));
        mdcReplace(entry.getKey(), entry.getValue());
      }
    }

    private static void mdcReplace(String key, @Nullable String value) {
      if (value != null) {
        MDC.put(key, value);
      } else {
        MDC.remove(key);
      }
    }

    @Override
    public void close() {
      // Close
      scope.close();

      // Restore previous context
      for (Map.Entry<String, String> entry : previous.entrySet()) {
        mdcReplace(entry.getKey(), entry.getValue());
      }
    }

    @Nullable
    @Override
    public Span span() {
      return scope.span();
    }
  }
}
