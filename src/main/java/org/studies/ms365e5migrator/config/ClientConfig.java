package org.studies.ms365e5migrator.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.studies.ms365e5migrator.config.properties.GraphConfig;
import org.studies.ms365e5migrator.config.properties.MigrationConfig;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

  private final MigrationConfig migrationConfig;
  private final GraphConfig graphConfig;

  @Bean
  public GraphServiceClient from() {
    return configureClient(migrationConfig.from());
  }

  @Bean
  public GraphServiceClient to() {
    return configureClient(migrationConfig.to());
  }

  private GraphServiceClient configureClient(MigrationConfig.ClientConfig clientConfig) {
    ClientSecretCredential credentials = new ClientSecretCredentialBuilder()
        .clientId(clientConfig.clientId())
        .clientSecret(clientConfig.clientSecret())
        .tenantId(clientConfig.tenantId())
        .build();

    return new GraphServiceClient(credentials, graphConfig.defaultScope());
  }
}
