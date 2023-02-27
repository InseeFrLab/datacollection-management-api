package fr.insee.survey.datacollectionmanagement.user.service;


import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.survey.datacollectionmanagement.user.domain.User;
import fr.insee.survey.datacollectionmanagement.user.domain.UserEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public interface UserEventService {

    public Page<UserEvent> findAll(Pageable pageable);

    public Optional<UserEvent> findById(Long id);

    public UserEvent saveUserEvent(UserEvent userEvent);

    public void deleteUserEvent(Long id);

    public Set<UserEvent> findUserEventsByUser (User user);

    UserEvent createUserEvent(User user, UserEvent.UserEventType type, JsonNode payload);

}

