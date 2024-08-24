package org.studies.ms365e5migrator.service.user;

import com.microsoft.graph.models.User;

public interface PasswordService {

  void setPasswordProfile(User user);
}
