package org.studies.ms365e5migrator.service.implementation;

import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.studies.ms365e5migrator.service.MigrationService;

@Service
@RequiredArgsConstructor
public class GraphMigrationService implements MigrationService {

  private final GraphServiceClient from;
  private final GraphServiceClient to;
  private final MigrationService graphUserMigrationService;
  private final MigrationService graphApplicationMigrationService;

  @Override
  public void run() {
    this.run(from, to);
  }

  @Override
  public void run(GraphServiceClient from, GraphServiceClient to) {
    graphUserMigrationService.run(from, to);
    graphApplicationMigrationService.run(from, to);
  }
}
