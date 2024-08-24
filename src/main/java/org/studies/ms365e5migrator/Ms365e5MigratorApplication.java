package org.studies.ms365e5migrator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.studies.ms365e5migrator.service.MigrationService;

@SpringBootApplication
@RequiredArgsConstructor
@ConfigurationPropertiesScan("org.studies.ms365e5migrator.config")
public class Ms365e5MigratorApplication implements CommandLineRunner {

  private final MigrationService graphMigrationService;

  public static void main(String... args) {
    SpringApplication.run(Ms365e5MigratorApplication.class, args);
  }

  @Override
  public void run(String... args) {
    graphMigrationService.run();
  }
}
