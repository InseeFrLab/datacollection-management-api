package fr.insee.survey.datacollectionmanagement.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
