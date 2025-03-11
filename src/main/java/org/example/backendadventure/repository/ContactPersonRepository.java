package org.example.backendadventure.repository;

import org.example.backendadventure.model.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer> {
}
