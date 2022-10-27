package fr.insee.survey.datacollectionmanagement.config.auth.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import fr.insee.survey.datacollectionmanagement.config.auth.user.User;
import fr.insee.survey.datacollectionmanagement.config.auth.user.UserProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = false, prePostEnabled = true)
@ConditionalOnMissingBean(OpenIDConnectSecurityContext.class)
public class DefaultSecurityContext {

    @Autowired
    ApplicationConfig config;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.sessionManagement().disable();
        http.cors(withDefaults())
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/csrf", "/", "/webjars/**", "/swagger-resources/**").permitAll()
                .antMatchers("/environnement").permitAll()// PublicResources
                .antMatchers("/healthcheck").permitAll()
                .antMatchers("/swagger-ui/*").permitAll()
                .antMatchers("/v3/api-docs/swagger-config", "/v3/api-docs").permitAll()
                .antMatchers("/openapi.json").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().permitAll();

        return http.build();
    }

    @Bean
    public UserProvider getUserProvider() {
        return auth -> new User();
    }

}