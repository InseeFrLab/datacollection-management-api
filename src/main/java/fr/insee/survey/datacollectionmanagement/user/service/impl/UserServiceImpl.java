package fr.insee.survey.datacollectionmanagement.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.survey.datacollectionmanagement.user.domain.User;
import fr.insee.survey.datacollectionmanagement.user.domain.UserEvent;
import fr.insee.survey.datacollectionmanagement.user.repository.UserRepository;
import fr.insee.survey.datacollectionmanagement.user.service.UserEventService;
import fr.insee.survey.datacollectionmanagement.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserEventService userEventService;

    @Autowired
    UserRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findByIdentifier(String identifier) {
        return userRepository.findById(identifier);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String identifier) {
        userRepository.deleteById(identifier);
    }


    @Override
    public User createUserEvent(User user, JsonNode payload) {

        UserEvent newUserEvent = userEventService.createUserEvent(user, UserEvent.UserEventType.create,
                payload);
        user.setUserEvents(new HashSet<>(Arrays.asList(newUserEvent)));
        return saveUser(user);
    }

    @Override
    public User updateUserEvent(User user, JsonNode payload) {

        User existingUser = findByIdentifier(user.getIdentifier()).get();

        Set<UserEvent> setUserEventsUser = existingUser.getUserEvents();
        UserEvent userEventUpdate = userEventService.createUserEvent(user, UserEvent.UserEventType.update,
                payload);
        setUserEventsUser.add(userEventUpdate);
        user.setUserEvents(setUserEventsUser);
        return saveUser(user);
    }

    @Override
    public void deleteContactAddressEvent(User user) {
        deleteUser(user.getIdentifier());
    }
}
