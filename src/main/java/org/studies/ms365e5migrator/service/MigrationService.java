package org.studies.ms365e5migrator.service;

import com.microsoft.graph.serviceclient.GraphServiceClient;

public interface MigrationService {

  default void run() {
    throw new UnsupportedOperationException("Not implemented");
  }

  void run(GraphServiceClient from, GraphServiceClient to);
}
