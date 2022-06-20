package fr.insee.survey.datacollectionmanagement.questioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.EventOrder;


public interface EventOrderRepository extends JpaRepository<EventOrder, Long> {
}
