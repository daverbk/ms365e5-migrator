package org.studies.ms365e5migrator.service.implementation;

import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.studies.ms365e5migrator.service.user.LicenseService;
import org.studies.ms365e5migrator.service.MigrationService;
import org.studies.ms365e5migrator.service.Converter;

@Service
@RequiredArgsConstructor
public class GraphUserMigrationService implements MigrationService {

  private final Converter<User> graphUserService;
  private final LicenseService licenseService;

  @Override
  public void run(GraphServiceClient from, GraphServiceClient to) {
    List<User> users = Objects
        .requireNonNull(from.users().get(), "Could not read users from the initial client.")
        .getValue();

    // DEVNOTE: We assume that the user with business phone is an admin, and we do not migrate them
    Objects.requireNonNull(users, "No users were retrieved on the initial client.")
        .stream()
        .map(graphUserService::convert)
        .filter(user -> Objects.requireNonNull(user.getBusinessPhones(),
            "No business phones could be retrieved from GraphAPI").isEmpty())
        .forEach(user -> {
          to.users().post(user);

          if (licenseService.isE5Licensed(user)) {
            licenseService.assignE5Licence(user);
          }
        });
  }
}
