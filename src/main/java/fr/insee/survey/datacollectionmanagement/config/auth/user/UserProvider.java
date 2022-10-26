package fr.insee.survey.datacollectionmanagement.config.auth.user;

import org.springframework.security.core.Authentication;

@FunctionalInterface
public interface UserProvider {

    User getUser(Authentication authentication);

}