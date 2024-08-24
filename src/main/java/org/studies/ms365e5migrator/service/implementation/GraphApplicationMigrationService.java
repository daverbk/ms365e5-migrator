package org.studies.ms365e5migrator.service.implementation;

import com.microsoft.graph.models.Application;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.studies.ms365e5migrator.service.Converter;
import org.studies.ms365e5migrator.service.MigrationService;

@Service
@Log4j2
@RequiredArgsConstructor
public class GraphApplicationMigrationService implements MigrationService {

  private final Converter<Application> graphApplicationService;

  @Override
  public void run(GraphServiceClient from, GraphServiceClient to) {
    List<Application> applications = Objects
        .requireNonNull(from.applications().get(),
            "Could not read applications from the initial client.")
        .getValue();

    Objects.requireNonNull(applications, "No applications were retrieved on the initial client.")
        .stream()
        .map(graphApplicationService::convert)
        .forEach(app -> {
          Application createdApp = to.applications().post(app);

          String clientSecret = Objects.requireNonNull(
                  Objects.requireNonNull(
                          createdApp, "Received null from GraphAPI application post.")
                      .getPasswordCredentials(),
                  "Password credentials are not present for a migrated app.")
              .stream().findFirst()
              .orElseThrow(() -> new NullPointerException("Migrated app credentials are empty."))
              .getSecretText();

          log.info("Application {} was created. Its client secret is {}. Write it down!",
              createdApp.getDisplayName(), clientSecret);
        });
  }
}
