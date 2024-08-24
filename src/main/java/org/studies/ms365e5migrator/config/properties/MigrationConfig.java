package org.studies.ms365e5migrator.config.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "migration")
public record MigrationConfig(ClientConfig from, ClientConfig to, String userPassword) {

  public record ClientConfig(
      @NotNull(message = "migration.client-id must not be null")
      String clientId,
      @NotNull(message = "migration.client-secret must not be null")
      String clientSecret,
      @NotNull(message = "migration.tenant-id must not be null")
      String tenantId
  ) {

  }
}


