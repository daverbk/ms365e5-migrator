package org.studies.ms365e5migrator.service.implementation.user;

import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.studies.ms365e5migrator.service.Converter;
import org.studies.ms365e5migrator.service.user.PasswordService;

@Service
public class GraphUserService implements Converter<User> {

  private final String domain;
  private final String location;
  private final PasswordService passwordService;

  public GraphUserService(PasswordService passwordService, GraphServiceClient to) {
    this.passwordService = passwordService;

    // DEVNOTE: We take the first domain suggesting the receiving account is clean and contains only one
    domain = Objects.requireNonNull(Objects.requireNonNull(to.domains().get(),
                "We could not send get request on domains via GraphAPI.")
            .getValue(), "No domains data was returned for the 'to' client from GraphAPI.")
        .get(0)
        .getId();

    // DEVNOTE: We assume the 'to' client is empty and has only the admin user
    location = Objects.requireNonNull(
            Objects.requireNonNull(
                to.users().delta().get(
                    requestConfiguration -> Objects.requireNonNull(
                        requestConfiguration.queryParameters,
                        "Query params are null.").select = new String[]{
                        "usageLocation"}),
                "GraphAPI could not provide delta data on users.").getValue(),
            "No delta was retrieved from the 'to' client.")
        .get(0)
        .getUsageLocation();
  }

  @Override
  public User convert(User entity) {
    String principalName = entity.getUserPrincipalName();

    int atIndex = Objects.requireNonNull(principalName, "Principal name of a user is null.")
        .indexOf('@');
    String newPrincipalName = principalName.substring(0, atIndex + 1) + domain;

    entity.setAccountEnabled(true);
    entity.setUserPrincipalName(newPrincipalName);
    entity.setMailNickname(newPrincipalName.substring(0, atIndex));
    entity.setUsageLocation(location);
    passwordService.setPasswordProfile(entity);

    return entity;
  }
}
