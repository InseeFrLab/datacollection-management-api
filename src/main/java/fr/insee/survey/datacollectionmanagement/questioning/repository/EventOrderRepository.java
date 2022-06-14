package fr.insee.survey.datacollectionmanagement.questioning.repository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.EventOrder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventOrderRepository extends JpaRepository<EventOrder, Long> {
}
