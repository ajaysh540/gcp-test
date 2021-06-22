package com.fhir.testfhir.configs;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Configuration
public class Configurations {
    protected static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    protected HttpRequestFactory httpRequestFactory;
    private GoogleCredentials credentials = null;

    public Configurations() {
        try {
            log.debug("creating google credentials");
            credentials =
                    GoogleCredentials.getApplicationDefault()
                            .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-healthcare"));
            refreshCredential();
        } catch (IOException e) {
            log.error("failed to get google credentials: {} ", e);
            throw new RuntimeException(e);
        }
    }

    protected void refreshCredential() {
        try {
            log.info("Refreshing creds: {}",credentials.getAccessToken());
            initHttpRequestFactory(credentials.refreshAccessToken());
            log.info("Refreshed creds: {}",credentials.getAccessToken());
        } catch (IOException e) {
            log.error("Unable to refresh credentials: {}", e);
            throw new RuntimeException(e);
        }
    }

    private void initHttpRequestFactory(AccessToken accessToken) {
        Credential credential =
                new Credential(BearerToken.authorizationHeaderAccessMethod())
                        .setAccessToken(accessToken.getTokenValue());
        httpRequestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
    }

    public String getGCPAccessToken() {
        log.debug("generating GCP access token");
        try {
            String token = credentials.refreshAccessToken().getTokenValue();
            log.info("Token: {}",token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
