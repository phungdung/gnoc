package com.viettel.tracing.metrics;

import io.jaegertracing.internal.JaegerSpanContext;
import java.util.Map;

public class CustomSpanContextGnoc extends JaegerSpanContext {

  protected CustomSpanContextGnoc(
      long traceIdHigh,
      long traceIdLow,
      long spanId,
      long parentId,
      byte flags,
      Map<String, String> baggage,
      String debugId,
      CustomObjectFactoryGnoc objectFactory) {
    super(traceIdHigh, traceIdLow, spanId, parentId, flags, baggage, debugId, objectFactory);
  }
}
