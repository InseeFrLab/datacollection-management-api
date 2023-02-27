package fr.insee.survey.datacollectionmanagement.user.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.survey.datacollectionmanagement.user.domain.User;
import fr.insee.survey.datacollectionmanagement.user.domain.UserEvent;
import fr.insee.survey.datacollectionmanagement.user.repository.UserEventRepository;
import fr.insee.survey.datacollectionmanagement.user.service.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    private UserEventRepository userEventRepository;

    @Override
    public Page<UserEvent> findAll(Pageable pageable) {
        return userEventRepository.findAll(pageable);
    }

    @Override
    public Optional<UserEvent> findById(Long id) {
        return userEventRepository.findById(id);
    }

    @Override
    public UserEvent saveUserEvent(UserEvent userEvent) {
        return userEventRepository.save(userEvent);
    }

    @Override
    public void deleteUserEvent(Long id) {
        userEventRepository.deleteById(id);
    }

    @Override
    public Set<UserEvent> findUserEventsByUser(User user) {
        return userEventRepository.findByUser(user);
    }

    @Override
    public UserEvent createUserEvent(User user, UserEvent.UserEventType type, JsonNode payload) {
        UserEvent userEventCreate = new UserEvent();
        userEventCreate.setUser(user);
        userEventCreate.setType(type);
        userEventCreate.setPayload(payload);
        userEventCreate.setEventDate(new Date());
        return userEventCreate;
    }
}
