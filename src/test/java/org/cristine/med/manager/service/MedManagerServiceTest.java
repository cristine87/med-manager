package org.cristine.med.manager.service;

import org.cristine.med.manager.domain.entity.Medicine;
import org.cristine.med.manager.domain.repository.MedRepository;
import org.cristine.med.manager.rest.MedicineDTO;
import org.cristine.med.manager.service.impl.MedManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicineManagerServiceImplTest
{

    @Mock
    private MedRepository repository;

    @InjectMocks
    private MedManagerServiceImpl service;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    @DisplayName( "Should return all medicines when repository contains data" )
    void findAllReturnsAllMedicines()
    {
        List<MedicineDTO> medicines = List.of( new MedicineDTO( 1L, "Paracetamol", "Paracetamol", "500mg", LocalDate.now(), 10 ) );
        when( repository.findAll() ).thenReturn(
            List.of( new Medicine( 1L, "Paracetamol", "Paracetamol", "500mg", LocalDate.now(), 10 ) ) );

        List<MedicineDTO> result = service.findAll();

        assertEquals( 1, result.size() );
        assertEquals( "Paracetamol", result.getFirst().name() );
        verify( repository, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName( "Should return empty list when repository is empty" )
    void findAllReturnsEmptyList()
    {
        when( repository.findAll() ).thenReturn( List.of() );

        List<MedicineDTO> result = service.findAll();

        assertTrue( result.isEmpty() );
        verify( repository, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName( "Should return medicine by ID when it exists" )
    void findByIdReturnsMedicine()
    {
        Medicine medicine = new Medicine( 1L, "Ibuprofen", "Ibuprofeno", "200mg", LocalDate.now(), 5 );
        when( repository.findById( 1L ) ).thenReturn( Optional.of( medicine ) );

        Optional<MedicineDTO> result = service.findById( 1L );

        assertTrue( result.isPresent() );
        assertEquals( "Ibuprofen", result.get().name() );
        verify( repository, times( 1 ) ).findById( 1L );
    }

    @Test
    @DisplayName( "Should return empty when medicine ID does not exist" )
    void findByIdReturnsEmpty()
    {
        when( repository.findById( 1L ) ).thenReturn( Optional.empty() );

        Optional<MedicineDTO> result = service.findById( 1L );

        assertTrue( result.isEmpty() );
        verify( repository, times( 1 ) ).findById( 1L );
    }

    @Test
    @DisplayName( "Should create a new medicine when name does not exist" )
    void createOrUpdateCreatesNewMedicine()
    {
        MedicineDTO medicineDTO = new MedicineDTO( null, "Aspirin", "Aspirina", "100mg", LocalDate.now(), 20 );
        Medicine savedMedicine = new Medicine( 1L, "Aspirin", "Aspirina", "100mg", LocalDate.now(), 20 );
        when( repository.findByName( "Aspirin" ) ).thenReturn( Optional.empty() );
        when( repository.save( any( Medicine.class ) ) ).thenReturn( savedMedicine );

        MedicineDTO result = service.createOrUpdate( medicineDTO );

        assertNotNull( result.id() );
        assertEquals( "Aspirin", result.name() );
        assertEquals( 20, result.quantity() );
        verify( repository, times( 1 ) ).findByName( "Aspirin" );
        verify( repository, times( 1 ) ).save( any( Medicine.class ) );
    }

    @Test
    @DisplayName( "Should update existing medicine when name already exists" )
    void createOrUpdateUpdatesExistingMedicine()
    {
        MedicineDTO medicineDTO = new MedicineDTO( null, "Ibuprofen", "Ibuprofeno", "200mg", LocalDate.now(), 10 );
        Medicine existingMedicine = new Medicine( 1L, "Ibuprofen", "Ibuprofeno", "200mg", LocalDate.now(), 5 );
        Medicine updatedMedicine = new Medicine( 1L, "Ibuprofen", "Ibuprofeno", "200mg", LocalDate.now(), 15 );
        when( repository.findByName( "Ibuprofen" ) ).thenReturn( Optional.of( existingMedicine ) );
        when( repository.save( any( Medicine.class ) ) ).thenReturn( updatedMedicine );

        MedicineDTO result = service.createOrUpdate( medicineDTO );

        assertEquals( "Ibuprofen", result.name() );
        assertEquals( 15, result.quantity() );
        verify( repository, times( 1 ) ).findByName( "Ibuprofen" );
        verify( repository, times( 1 ) ).save( any( Medicine.class ) );
    }

    @Test
    @DisplayName( "Should delete medicine by ID" )
    void deleteRemovesMedicine()
    {
        doNothing().when( repository ).deleteById( 1L );

        service.delete( 1L );

        verify( repository, times( 1 ) ).deleteById( 1L );
    }

    @Test
    @DisplayName( "Should find medicine by name" )
    void findByNameReturnsMedicine()
    {
        Medicine medicine = new Medicine( 1L, "Amoxicillin", "Amoxicilina", "500mg", LocalDate.now(), 15 );
        when( repository.findByName( "Amoxicillin" ) ).thenReturn( Optional.of( medicine ) );

        Optional<MedicineDTO> result = service.findByName( "Amoxicillin" );

        assertTrue( result.isPresent() );
        assertEquals( "Amoxicillin", result.get().name() );
        verify( repository, times( 1 ) ).findByName( "Amoxicillin" );
    }

    @Test
    @DisplayName( "Should return empty when medicine name does not exist" )
    void findByNameReturnsEmpty()
    {
        when( repository.findByName( "NonExistent" ) ).thenReturn( Optional.empty() );

        Optional<MedicineDTO> result = service.findByName( "NonExistent" );

        assertTrue( result.isEmpty() );
        verify( repository, times( 1 ) ).findByName( "NonExistent" );
    }
}