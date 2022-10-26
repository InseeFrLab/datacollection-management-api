package fr.insee.survey.datacollectionmanagement.config.auth.user;


import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component("AuthorizeMethodDecider")
public class AuthorizeMethodDecider {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeMethodDecider.class);

    private User noAuthUser;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    ApplicationConfig config;

    public User getUser() {
        if (config.getAuthType().equals("OIDC")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userProvider.getUser(authentication);
            logger.info("id and roles user {},{}",currentUser.getId(),currentUser.getRoles());
            return currentUser;
        }
        return noAuthUser();
    }

    private User noAuthUser() {
        if (this.noAuthUser != null) {
            return this.noAuthUser;
        }

        JSONArray roles = new JSONArray();
        roles.put("ROLE_offline_access");
        roles.put(config.getRoleAdmin());
        roles.put("ROLE_uma_authorization");
        return new User("GUEST",roles);
    }


    public boolean isInternalUser() throws JSONException {
        User user = getUser();
        return isInternalUser(user);
    }

    public boolean isInternalUser(User user) throws JSONException {
        logger.info("Check if user is internal (admin, manager, helpdesk)");
        return (hasRole(user,config.getRoleAdmin()) || hasRole(user,config.getRoleManager())|| hasRole(user,config.getRoleHelpdesk()) );
    }

    public boolean isWebClient() throws JSONException {
        User user = getUser();
        return isWebClient(user);
    }

    public boolean isWebClient(User user) throws JSONException {
        logger.info("Check if user is webclient");
        return hasRole(user,config.getRoleWebClient());
    }

    public boolean isRespondent() throws JSONException {
        User user = getUser();
        return isRespondent(user);
    }

    public boolean isRespondent(User user) throws JSONException {
        logger.info("Check if user is respondent");
        return hasRole(user,config.getRoleRespondent());
    }

    private boolean hasRole(User user, String role) throws JSONException {
        Boolean hasRole = false;
        JSONArray roles = user.getRoles();
        for (int i = 0; i < roles.length(); i++) {
            if (roles.getString(i).equals(role)) {
                hasRole = true;
            }
        }
        return hasRole;
    }


}
