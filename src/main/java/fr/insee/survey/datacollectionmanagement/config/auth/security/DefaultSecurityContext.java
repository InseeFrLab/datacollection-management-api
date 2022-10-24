package fr.insee.survey.datacollectionmanagement.config.auth.security;


import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import fr.insee.survey.datacollectionmanagement.config.auth.user.User;
import fr.insee.survey.datacollectionmanagement.config.auth.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=false, prePostEnabled = true)
@ConditionalOnMissingBean(OpenIDConnectSecurityContext.class)
public class DefaultSecurityContext  {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSecurityContext.class);

    @Autowired
    ApplicationConfig config;

    @Value("${fr.insee.datacollectionmanagement.cors.allowedOrigin}")
    private Optional<String> allowedOrigin;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors(withDefaults())

                .authorizeRequests()
                .antMatchers("/csrf", "/", "/webjars/**", "/swagger-resources/**").permitAll()
                .antMatchers("/environnement").permitAll()//PublicResources
                .antMatchers("/healthcheck").permitAll()
                .antMatchers("/swagger-ui/*").permitAll()
                .antMatchers("/v3/api-docs/swagger-config", "/v3/api-docs").permitAll()
                .antMatchers("/openapi.json").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().permitAll();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        logger.info("Allowed origins : {}", allowedOrigin);
        String ao = allowedOrigin.isPresent() ? allowedOrigin.get() : allowedOrigin.orElse("*");
        configuration.setAllowedOrigins(List.of(ao));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserProvider getUserProvider() {
        return auth -> new User();
    }

}