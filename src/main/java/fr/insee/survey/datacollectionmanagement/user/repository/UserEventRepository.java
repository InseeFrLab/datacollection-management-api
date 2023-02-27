package fr.insee.survey.datacollectionmanagement.user.repository;

import fr.insee.survey.datacollectionmanagement.user.domain.User;
import fr.insee.survey.datacollectionmanagement.user.domain.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {

    Set<UserEvent> findByUser(User user);
}