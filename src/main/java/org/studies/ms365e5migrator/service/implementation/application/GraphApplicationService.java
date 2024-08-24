package org.studies.ms365e5migrator.service.implementation.application;

import com.microsoft.graph.models.Application;
import com.microsoft.graph.models.PasswordCredential;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.studies.ms365e5migrator.service.Converter;

@Service
public class GraphApplicationService implements Converter<Application> {

  @Override
  public Application convert(Application entity) {
    entity.setAppId(null);
    entity.setPublisherDomain(null);

    Objects.requireNonNull(entity.getWeb(), "App's web settings are not present.")
        .setRedirectUris(new LinkedList<>());
    entity.getWeb().setRedirectUriSettings(new LinkedList<>());

    // DEVNOTE: We can only add one password credential in time of an apps creation
    PasswordCredential passwordCredential = Objects.requireNonNull(entity.getPasswordCredentials(),
            "App's password credentials settings are not present.").stream().findFirst()
        .orElseThrow(() -> new NullPointerException(
            "No password credentials found. Set one password credential for the 'from' app."));

    passwordCredential.setHint(null);
    passwordCredential.setStartDateTime(null);
    passwordCredential.setKeyId(null);
    entity.setPasswordCredentials(new LinkedList<>(List.of(passwordCredential)));

    return entity;
  }
}
