package org.studies.ms365e5migrator.service.implementation.user;

import com.microsoft.graph.models.PasswordProfile;
import com.microsoft.graph.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.studies.ms365e5migrator.config.properties.MigrationConfig;
import org.studies.ms365e5migrator.service.user.PasswordService;

@Service
@RequiredArgsConstructor
public class GraphPasswordService implements PasswordService {

  private final MigrationConfig migrationConfig;

  @Override
  public void setPasswordProfile(User user) {
    user.setPasswordProfile(generatePasswordProfile(migrationConfig.userPassword()));
  }

  private PasswordProfile generatePasswordProfile(String password) {
    // DEVNOTE: We do not force multifactor authentication and the user to change the password on the next sign in
    PasswordProfile passwordProfile = new PasswordProfile();
    passwordProfile.setForceChangePasswordNextSignIn(false);
    passwordProfile.setPassword(password);
    return passwordProfile;
  }
}
