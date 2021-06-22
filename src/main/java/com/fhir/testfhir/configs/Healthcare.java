package com.fhir.testfhir.configs;

import com.google.api.services.healthcare.v1.CloudHealthcare;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Healthcare {

  public String getFhirBaseUrl() {
    String baseUrl = CloudHealthcare.DEFAULT_BASE_URL;
    return baseUrl+"/v1/projects/new-gardenia-dev/locations/us-central1/datasets/dataset-1/fhirStores/datafhir/fhir";
  }
}
