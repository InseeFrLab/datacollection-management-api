package fr.insee.survey.datacollectionmanagement.config;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;


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
                        .components(
                        new Components().addSecuritySchemes("oauth2", new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows)))
                        .info(new Info().title(buildProperties.getName()).version(buildProperties.getVersion()))
                        .addSecurityItem(new SecurityRequirement().addList("oauth2", Arrays.asList("read", "write")));

            default:
                return new OpenAPI()
                        .info(new Info().title(buildProperties.getName()).version(buildProperties.getVersion()));

        }

    }



}

