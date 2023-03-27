package fr.insee.survey.datacollectionmanagement.config.auth.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component("AuthorizeMethodDecider")
@Slf4j
public class AuthorizeMethodDecider {

    private User noAuthUser;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    ApplicationConfig config;

    public User getUser() {
        if (config.getAuthType().equals("OIDC")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userProvider.getUser(authentication);
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
        roles.put(config.getRoleAdmin().get(0));
        roles.put("ROLE_uma_authorization");
        return new User("GUEST", roles);
    }

    public boolean isInternalUser() throws JSONException {
        User user = getUser();
        return isInternalUser(user);
    }

    public boolean isInternalUser(User user) throws JSONException {
        return (hasRole(user, config.getRoleInternalUser()));
    }

    public boolean isAdmin() throws JSONException {
        User user = getUser();
        return isAdmin(user);
    }

    public boolean isAdmin(User user) throws JSONException {
        return (hasRole(user, config.getRoleAdmin()));
    }

    public boolean isWebClient() throws JSONException {
        User user = getUser();
        return isWebClient(user);
    }

    public boolean isWebClient(User user) throws JSONException {
        return hasRole(user, config.getRoleWebClient());
    }

    public boolean isRespondent() throws JSONException {
        User user = getUser();
        return isRespondent(user);
    }

    public boolean isRespondent(User user) throws JSONException {
        return hasRole(user, config.getRoleRespondent());
    }

    private boolean hasRole(User user, List<String> role) throws JSONException {
        Boolean hasRole = false;
        JSONArray roles = user.getRoles();
        for (int i = 0; i < roles.length(); i++) {
            if (role.contains(roles.getString(i))) {
                hasRole = true;
                log.info("role :"+roles.getString(i)+" has been found");
            }
        }
        return hasRole;
    }

    public String getUsername() throws JSONException {
        User user = getUser();
        return user.getId().toUpperCase();
    }

}
