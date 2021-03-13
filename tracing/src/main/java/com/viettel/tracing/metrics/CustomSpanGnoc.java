package com.viettel.tracing.metrics;

import io.jaegertracing.internal.JaegerSpan;
import io.jaegertracing.internal.Reference;
import java.util.List;
import java.util.Map;

public class CustomSpanGnoc extends JaegerSpan {

  protected CustomSpanGnoc(
      CustomTracerGnoc tracer,
      String operationName,
      CustomSpanContextGnoc context,
      long startTimeMicroseconds,
      long startTimeNanoTicks,
      boolean computeDurationViaNanoTicks,
      Map<String, Object> tags,
      List<Reference> references) {
    super(
        tracer,
        operationName,
        context,
        startTimeMicroseconds,
        startTimeNanoTicks,
        computeDurationViaNanoTicks,
        tags,
        references);
  }

  @Override
  public CustomSpanContextGnoc context() {
    return (CustomSpanContextGnoc) super.context();
  }
}
