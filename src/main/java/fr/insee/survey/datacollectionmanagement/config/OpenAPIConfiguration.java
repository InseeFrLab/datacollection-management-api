package fr.insee.survey.datacollectionmanagement.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true)

public class OpenAPIConfiguration {

    @Autowired
    BuildProperties buildProperties;

    @Autowired
    ApplicationConfig applicationConfig;


    @Bean public OpenAPI customOpenAPI() {

        switch(applicationConfig.getAuthType()) {

            case "OIDC":

                OAuthFlows flows = new OAuthFlows();
                OAuthFlow flow = new OAuthFlow();

                flow.setAuthorizationUrl(applicationConfig.getKeyCloakUrl() + "/realms/" + applicationConfig.getKeycloakRealm() + "/protocol/openid-connect/auth");
                flow.setTokenUrl(applicationConfig.getKeyCloakUrl() + "/realms/" + applicationConfig.getKeycloakRealm() + "/protocol/openid-connect/token");
                Scopes scopes = new Scopes();
                // scopes.addString("global", "accessEverything");
                flow.setScopes(scopes);
                flows = flows.authorizationCode(flow);

                return new OpenAPI()
                        .addServersItem(new Server().url(applicationConfig.getHost()))
                        .components(
                        new Components().addSecuritySchemes("oauth2", new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows)))
                        .info(new Info().title(buildProperties.getName()).version(buildProperties.getVersion()))
                        .addSecurityItem(new SecurityRequirement().addList("oauth2", Arrays.asList("read", "write")));

            default:
                return new OpenAPI()
                        .addServersItem(new Server().url(applicationConfig.getHost()))
                        .info(new Info().title(buildProperties.getName()).version(buildProperties.getVersion()));

        }

    }



}

