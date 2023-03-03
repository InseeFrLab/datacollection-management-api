package fr.insee.survey.datacollectionmanagement.user.repository;

import fr.insee.survey.datacollectionmanagement.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaRepository<User, String> {

    Page<User> findAll(Pageable pageable);
}
