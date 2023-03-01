package fr.insee.survey.datacollectionmanagement.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class ApplicationConfig {

    //AUTHENTICATION
    @Value("${jwt.role-claim}")
    private String roleClaim;
    @Value("${jwt.id-claim}")
    private String idClaim;


    @Value("#{'${fr.insee.datacollectionmanagement.roles.admin.role}'.split(',')}")
    private List<String> roleAdmin;

    @Value("#{'${fr.insee.datacollectionmanagement.roles.webclient.role}'.split(',')}")
    private List<String> roleWebClient;

    @Value("#{'${fr.insee.datacollectionmanagement.roles.respondent.role}'.split(',')}")
    private List<String> roleRespondent;

    @Value("#{'${fr.insee.datacollectionmanagement.roles.internal.user.role}'.split(',')}")
    private List<String> roleInternalUser;

    @Value("${fr.insee.datacollectionmanagement.auth.mode}")
    private String authType;

    @Value("${fr.insee.datacollectionmanagement.cors.allowedOrigin}")
    private Optional<String> allowedOrigin;

    @Value("${fr.insee.datacollectionmanagement.auth.realm}")
    private String keycloakRealm;
    @Value("${fr.insee.datacollectionmanagement.auth.server-url}")
    private String keyCloakUrl;
    
    @Value("${fr.insee.datacollectionmanagement.api.questioning.url}")
    private String questioningUrl;
}
