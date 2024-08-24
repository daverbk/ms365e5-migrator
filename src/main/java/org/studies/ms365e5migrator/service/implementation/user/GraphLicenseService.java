package org.studies.ms365e5migrator.service.implementation.user;

import com.microsoft.graph.models.AssignedLicense;
import com.microsoft.graph.models.CompanySubscription;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.assignlicense.AssignLicensePostRequestBody;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.studies.ms365e5migrator.service.user.LicenseService;

@Service
public class GraphLicenseService implements LicenseService {

  private final CompanySubscription subscription;
  private final GraphServiceClient from;
  private final GraphServiceClient to;

  public GraphLicenseService(GraphServiceClient from, GraphServiceClient to) {
    this.from = from;
    this.to = to;

    // DEVNOTE: We assume the developer program subscription is the first and the only one
    subscription = Objects.requireNonNull(
            Objects.requireNonNull(to.directory().subscriptions().get(),
                    "Could not retrieve subscription details.")
                .getValue(),
            "No subscription details were returned.")
        .get(0);
  }

  @Override
  public void assignE5Licence(User user) {
    to.users().byUserId(Objects.requireNonNull(
            user.getUserPrincipalName(),
            "User principal name cannot be extracted because of a null reference."))
        .assignLicense()
        .post(generateAssignLicenseRequest());
  }

  @Override
  public boolean isE5Licensed(User user) {
    long licensesCount =
        Objects.requireNonNull(
                Objects.requireNonNull(
                        from.users().byUserId(
                                Objects.requireNonNull(
                                    user.getId(),
                                    "User id cannot be extracted because of a null reference."))
                            .licenseDetails()
                            .get(), "Could not retrieve license details for a user.")
                    .getValue(), "No license details were returned.")
            .size();

    // DEVNOTE: We assume that if the user hast at least one license, it is an E5 license
    return licensesCount > 0;
  }

  private AssignLicensePostRequestBody generateAssignLicenseRequest() {
    AssignLicensePostRequestBody assignLicensePostRequestBody = new AssignLicensePostRequestBody();

    AssignedLicense assignedLicense = new AssignedLicense();
    assignedLicense.setDisabledPlans(
        new LinkedList<>(List.of(
            Objects.requireNonNull(
                Objects.requireNonNull(subscription.getServiceStatus(),
                        "Service Status could not be retrieved from the target client subscription.")
                    .get(0).getServicePlanId(),
                "ServicePlanId could not be retrieved from the target client subscription.")
        )));
    assignedLicense.setSkuId(UUID.fromString(Objects.requireNonNull(subscription.getSkuId(),
        "SkuId could not be retrieved from the subscription.")));

    assignLicensePostRequestBody.setAddLicenses(new LinkedList<>(List.of(assignedLicense)));
    assignLicensePostRequestBody.setRemoveLicenses(new LinkedList<>());

    return assignLicensePostRequestBody;
  }
}
