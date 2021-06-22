package com.fhir.testfhir.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
  @Autowired
  Configurations configurations;

  @GetMapping("/")
  public String testMethod() {
    System.out.println("Fetching Token");
    String token = configurations.getGCPAccessToken();
    System.out.println(token);
    return token;
  }
}
