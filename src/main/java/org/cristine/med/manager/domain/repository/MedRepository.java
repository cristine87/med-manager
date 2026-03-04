package org.cristine.med.manager.domain.repository;


import org.cristine.med.manager.domain.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedRepository extends JpaRepository<Medicine, Long> {
    java.util.Optional<Medicine> findByName(String name);
}
