package ru.kata.spring.boot_security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {

    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roles WHERE p.email = :email")
    Optional<Person> findByEmailWithRoles(String email);

    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roles WHERE p.id = :id")
    Optional<Person> findByPersonIdWithRoles(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roles WHERE p.firstName = :firstName")
    Optional<Person> findByFirstNameWithRoles(@Param("firstName") String firstName);

    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roles")
    List<Person> findAllWithRoles();

}
