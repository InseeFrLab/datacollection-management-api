package fr.insee.survey.datacollectionmanagement.config.auth.security;

import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import fr.insee.survey.datacollectionmanagement.config.auth.user.User;
import fr.insee.survey.datacollectionmanagement.config.auth.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled = true)
@ConditionalOnProperty(name = "fr.insee.datacollectionmanagement.auth.mode", havingValue = "OIDC")
public class OpenIDConnectSecurityContext  {

    private static final Logger logger = LoggerFactory.getLogger(OpenIDConnectSecurityContext.class);


    @Autowired
    ApplicationConfig config;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.sessionManagement().disable();

        http.cors(withDefaults())
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/csrf", "/", "/webjars/**", "/swagger-resources/**").permitAll()
                .antMatchers("/environnement").permitAll()//PublicResources
                .antMatchers("/healthcheck").permitAll()
                .antMatchers("/swagger-ui/*").permitAll()
                .antMatchers("/v3/api-docs/swagger-config", "/v3/api-docs").permitAll()
                .antMatchers("/openapi.json").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();

    }

    @Bean
    public UserProvider getUserProvider() {
        return auth -> {
            if ("anonymousUser".equals(auth.getPrincipal())) return null; //init request, or request without authentication
            final Jwt jwt = (Jwt) auth.getPrincipal();
            List<String> tryRoles = jwt.getClaimAsStringList(config.getRoleClaim());
            String tryId=jwt.getClaimAsString(config.getIdClaim());
            logger.info("Current User is {},  with roles {}",tryId,tryRoles);
            return new User(tryId, tryRoles);
        };
    }


}
