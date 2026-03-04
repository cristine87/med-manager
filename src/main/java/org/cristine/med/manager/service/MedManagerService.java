package org.cristine.med.manager.service;

import org.cristine.med.manager.rest.MedicineDTO;

import java.util.List;
import java.util.Optional;

public interface MedManagerService
{
    List<MedicineDTO> findAll();
    Optional<MedicineDTO> findById(Long id);
    Optional<MedicineDTO> findByName(String name);
    MedicineDTO createOrUpdate( MedicineDTO medicine);
    void delete(Long id);
}
