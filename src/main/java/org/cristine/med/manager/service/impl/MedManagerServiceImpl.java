package org.cristine.med.manager.service.impl;

import org.cristine.med.manager.domain.entity.Medicine;
import org.cristine.med.manager.domain.repository.MedRepository;
import org.cristine.med.manager.rest.MedicineDTO;
import org.cristine.med.manager.service.MedManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedManagerServiceImpl
    implements MedManagerService
{
    private final MedRepository repository;

    public MedManagerServiceImpl( MedRepository repository )
    {
        this.repository = repository;
    }

    public List<MedicineDTO> findAll()
    {
        List<Medicine> medicines = repository.findAll();
        return medicines.stream().map( this::mapper ).toList();
    }

    public Optional<MedicineDTO> findById( Long id )
    {
        return repository.findById( id )
            .map( this::mapper );
    }

    @Override public Optional<MedicineDTO> findByName( final String name )
    {
        return repository.findByName( name )
            .map( this::mapper );
    }

    public MedicineDTO createOrUpdate( MedicineDTO medicine )
    {
        return repository.findByName( medicine.name() )
            .map( existing -> {
                Medicine updated = new Medicine(
                    existing.getId(),
                    existing.getName(),
                    medicine.formulation(),
                    medicine.dosage(),
                    medicine.expiration(),
                    existing.getQuantity() + medicine.quantity()
                );
                return mapper( repository.save( updated ) );
            } )
            .orElseGet( () -> mapper( repository.save( new Medicine(
                null,
                medicine.name(),
                medicine.formulation(),
                medicine.dosage(),
                medicine.expiration(),
                medicine.quantity()
            ) ) ) );
    }

    public void delete( Long id )
    {
        repository.deleteById( id );
    }

    private MedicineDTO mapper( Medicine medicine )
    {
        return new MedicineDTO(
            medicine.getId(),
            medicine.getName(),
            medicine.getFormulation(),
            medicine.getDosage(),
            medicine.getExpiration(),
            medicine.getQuantity()
        );
    }
}
