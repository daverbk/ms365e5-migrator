package org.studies.ms365e5migrator.service.user;

import com.microsoft.graph.models.User;

public interface LicenseService {

  void assignE5Licence(User user);

  boolean isE5Licensed(User user);
}
