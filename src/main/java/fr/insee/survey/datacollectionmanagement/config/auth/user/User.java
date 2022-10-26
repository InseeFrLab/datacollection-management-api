package fr.insee.survey.datacollectionmanagement.config.auth.user;

import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.springframework.security.core.GrantedAuthority;

public class User {

    private JSONArray roles;
    private String id;

    private Collection<GrantedAuthority> authorities;

    public User() {
        super();
    }

    public User(String id, JSONArray roles) {
        this.id=id;
        this.roles = roles;
    }

    public User(String id, List<String> roles) {
        this.id=id;
        this.roles = new JSONArray(roles);
    }

    public JSONArray getRoles() {
        return roles;
    }
    public void setRoles(JSONArray roles) {
        this.roles = roles;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
