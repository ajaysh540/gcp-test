package com.fhir.testfhir.configs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FHIRStore {

  @Autowired private Configurations gcpCredentials;

  @Autowired private Healthcare healthcare;

  private final FhirContext fhirContext;

  FHIRStore() {
    log.debug("creating hapi FHIR context");
    fhirContext = FhirContext.forR4();
    fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
  }

  private IGenericClient createClient() {
    log.debug("creating new client");
    String baseUrl = healthcare.getFhirBaseUrl();
    IGenericClient newClient = fhirContext.newRestfulGenericClient(baseUrl);
    return attachClientInterceptors(newClient);
  }

  private IGenericClient attachClientInterceptors(IGenericClient restfulClient) {
    // auth token interceptor
    String token = gcpCredentials.getGCPAccessToken();
    BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(token);
    restfulClient.registerInterceptor(authInterceptor);

    // logging interceptor
    LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
    restfulClient.registerInterceptor(loggingInterceptor);

    return restfulClient;
  }

  public IGenericClient getNewFHIRClient() {
    return createClient();
  }

  public FhirContext getFhirContext() {
    return fhirContext;
  }

  public String serializeResource(IBaseResource resource) {
    IParser parser = fhirContext.newJsonParser();
    parser.setPrettyPrint(true);
    return parser.encodeResourceToString(resource);
  }
}
