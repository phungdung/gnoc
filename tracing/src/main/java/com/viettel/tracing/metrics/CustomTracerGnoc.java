package com.viettel.tracing.metrics;

import io.jaegertracing.internal.JaegerTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;

@Slf4j
public class CustomTracerGnoc extends JaegerTracer {

  @Autowired
  private MetricsEndpoint metricsEndpoint;

  protected CustomTracerGnoc(CustomBuilder builder) {
    super(builder);
  }

  public static class CustomBuilder extends Builder {

    public CustomBuilder(String serviceName) {
      super(serviceName, new CustomObjectFactoryGnoc());
    }

    @Override
    public CustomTracerGnoc build() {
      return (CustomTracerGnoc) super.build();
    }

    @Override
    protected JaegerTracer createTracer() {
      return new CustomTracerGnoc(this);
    }
  }

  public class CustomSpanBuilder extends SpanBuilder {

    CustomSpanBuilder(String operationName) {
      super(operationName);
    }

    @Override
    public CustomSpanGnoc start() {
      super.withTag("jvm.memory.used", metricsEndpoint.metric("jvm.memory.used", null)
          .getMeasurements().get(0).getValue());
      super.withTag("process.cpu.usage", metricsEndpoint.metric("process.cpu.usage", null)
          .getMeasurements().get(0).getValue());
      super.withTag("system.cpu.count", metricsEndpoint.metric("system.cpu.count", null)
          .getMeasurements().get(0).getValue());
      super.withTag("system.cpu.usage", metricsEndpoint.metric("system.cpu.usage", null)
          .getMeasurements().get(0).getValue());
      try {
        super.withTag("hikaricp.connections.usage",
            metricsEndpoint.metric("hikaricp.connections.usage", null)
                .getMeasurements().get(0).getValue());
        super.withTag("hikaricp.connections.active",
            metricsEndpoint.metric("hikaricp.connections.active", null)
                .getMeasurements().get(0).getValue());
        super.withTag("hikaricp.connections.timeout",
            metricsEndpoint.metric("hikaricp.connections.timeout", null)
                .getMeasurements().get(0).getValue());
        super.withTag("hikaricp.connections.pending",
            metricsEndpoint.metric("hikaricp.connections.pending", null)
                .getMeasurements().get(0).getValue());
      } catch (NullPointerException e) {
        super.withTag("hikaricp.connections.usage", "null");
        super.withTag("hikaricp.connections.active", "null");
        super.withTag("hikaricp.connections.timeout", "null");
        super.withTag("hikaricp.connections.pending", "null");
        log.info("Exception:", e);
      }
      return (CustomSpanGnoc) super.start();
    }
  }
}
