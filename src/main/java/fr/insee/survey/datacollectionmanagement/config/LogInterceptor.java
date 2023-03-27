package fr.insee.survey.datacollectionmanagement.config;


import fr.insee.survey.datacollectionmanagement.config.auth.user.User;
import fr.insee.survey.datacollectionmanagement.config.auth.user.UserProvider;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);


   @Autowired
   ApplicationConfig applicationConfig;

   @Autowired
    UserProvider userProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String fishTag = UUID.randomUUID().toString();
        String method = request.getMethod();
        String operationPath = request.getRequestURI();

        String userId = null;

        switch (applicationConfig.getAuthType()) {

            case "OIDC":
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = userProvider.getUser(authentication);
                userId=(currentUser.getId()!=null ?currentUser.getId() : "anonymous");
                ThreadContext.put("user", userId.toUpperCase());
                break;
            default:
                userId = "GUEST";
                break;
        }

        ThreadContext.put("id", fishTag);
        ThreadContext.put("path", operationPath);
        ThreadContext.put("method", method);


        logger.info("["+userId.toUpperCase()+"] - ["+method+"] - ["+operationPath+"]");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {
        ThreadContext.clearMap();
    }
}

