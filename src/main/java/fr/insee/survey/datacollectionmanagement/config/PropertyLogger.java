package fr.insee.survey.datacollectionmanagement.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@Component
public class PropertyLogger  {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLogger.class);

    private static boolean alreadyDisplayed=false;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        final Environment env = event.getApplicationContext().getEnvironment();

        if (!alreadyDisplayed) {


            LOGGER.info("================================ Properties ================================");
            final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
            StreamSupport.stream(sources.spliterator(), false)
                    .filter(ps -> ps instanceof EnumerablePropertySource)
                    .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
                    .flatMap(Arrays::stream)
                    .distinct()
                    .filter(prop -> !(prop.contains("credentials") || prop.contains("password")
                            || prop.contains("pw") || prop.contains("Password")))
                    .filter(prop -> prop.startsWith("fr.insee") || prop.startsWith("logging") || prop.startsWith("jwt") || prop.startsWith("spring"))
                    .sorted()
                    .forEach(prop -> LOGGER.info("{}: {}", prop, env.getProperty(prop)));
            LOGGER.info("===========================================================================");
        }
        alreadyDisplayed=true;
    }
}