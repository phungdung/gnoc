package com.viettel.tracing.metrics;

import io.jaegertracing.internal.JaegerObjectFactory;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.Reference;
import java.util.List;
import java.util.Map;

public class CustomObjectFactoryGnoc extends JaegerObjectFactory {

  @Override
  public CustomSpanGnoc createSpan(
      JaegerTracer tracer,
      String operationName,
      JaegerSpanContext context,
      long startTimeMicroseconds,
      long startTimeNanoTicks,
      boolean computeDurationViaNanoTicks,
      Map<String, Object> tags,
      List<Reference> references) {
    return new CustomSpanGnoc(
        (CustomTracerGnoc) tracer,
        operationName,
        (CustomSpanContextGnoc) context,
        startTimeMicroseconds,
        startTimeNanoTicks,
        computeDurationViaNanoTicks,
        tags,
        references);
  }

  @Override
  public CustomSpanContextGnoc createSpanContext(
      long traceIdHigh,
      long traceIdLow,
      long spanId,
      long parentId,
      byte flags,
      Map<String, String> baggage,
      String debugId) {
    return new CustomSpanContextGnoc(traceIdHigh, traceIdLow, spanId, parentId, flags, baggage,
        debugId, this);
  }

  @Override
  public CustomTracerGnoc.CustomSpanBuilder createSpanBuilder(
      JaegerTracer tracer, String operationName) {
    return ((CustomTracerGnoc) tracer).new CustomSpanBuilder(operationName);
  }
}
