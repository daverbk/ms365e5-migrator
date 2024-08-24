package org.studies.ms365e5migrator.config.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "graph")
public record GraphConfig(
    @NotNull(message = "graph.default-scope must not be null")
    String defaultScope) {

}
