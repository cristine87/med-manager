package org.cristine.med.manager.controller;

import org.cristine.med.manager.rest.MedicineDTO;
import org.cristine.med.manager.service.MedManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicineManagerControllerTest
{

    @Mock
    private MedManagerService service;

    @InjectMocks
    private MedManagerController controller;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    @DisplayName( "Should return all medicines when service contains data" )
    void listReturnsAllMedicines()
    {
        MedicineDTO medicineDTO = new MedicineDTO( 1L, "Paracetamol", "Paracetamol", "500mg", LocalDate.now(), 10 );
        when( service.findAll() ).thenReturn( List.of( medicineDTO ) );

        List<MedicineDTO> result = controller.list();

        assertEquals( 1, result.size() );
        assertEquals( "Paracetamol", result.getFirst().name() );
        assertEquals( "500mg", result.getFirst().dosage() );
        assertEquals( "Paracetamol", result.getFirst().formulation() );
        verify( service, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName( "Should return medicine by ID when it exists" )
    void getReturnsMedicineById()
    {
        MedicineDTO medicineDTO = new MedicineDTO( 1L, "Advil", "Ibuprofeno", "200mg", LocalDate.now(), 5 );
        when( service.findById( 1L ) ).thenReturn( Optional.of( medicineDTO ) );

        ResponseEntity<MedicineDTO> response = controller.get( 1L );

        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertNotNull( response.getBody() );
        assertEquals( "Advil", response.getBody().name() );
        assertEquals( "200mg", response.getBody().dosage() );
        assertEquals( "Ibuprofeno", response.getBody().formulation() );
        verify( service, times( 1 ) ).findById( 1L );
    }

    @Test
    @DisplayName( "Should return 404 when medicine ID does not exist" )
    void getReturnsNotFoundForNonExistentId()
    {
        when( service.findById( 1L ) ).thenReturn( Optional.empty() );

        ResponseEntity<MedicineDTO> response = controller.get( 1L );

        assertTrue( response.getStatusCode().is4xxClientError() );
        verify( service, times( 1 ) ).findById( 1L );
    }

    @Test
    @DisplayName( "Should create a new medicine and return it" )
    void createReturnsCreatedMedicine()
    {
        MedicineDTO medicineDTO = getDip();
        MedicineDTO savedMedicineDTO = new MedicineDTO( 1L, "Novalgina", "Dipirona", "500mg", LocalDate.now(), 20 );
        when( service.createOrUpdate( medicineDTO ) ).thenReturn( savedMedicineDTO );

        ResponseEntity<MedicineDTO> response = controller.createOrUpdate( medicineDTO );

        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertEquals( URI.create( "/api/medicamentos/1" ), response.getHeaders().getLocation() );
        assertNotNull( response.getBody() );
        assertEquals( "Novalgina", response.getBody().name() );
        assertEquals( "500mg", response.getBody().dosage() );
        assertEquals( "Dipirona", response.getBody().formulation() );
        verify( service, times( 1 ) ).createOrUpdate( medicineDTO );
    }

    private static MedicineDTO getDip()
    {
        return new MedicineDTO( null, "Novalgina", "Dipirona", "500mg", LocalDate.now(), 20 );
    }

    @Test
    @DisplayName( "Should update existing medicine when name already exists" )
    void createOrUpdateUpdatesExistingMedicine()
    {
        MedicineDTO medicineDTO = getAmoxicilina();
        MedicineDTO updatedMedicineDTO = new MedicineDTO( 1L, "Amoxicilina", "Amoxicilina tri-hidratada", "500mg", LocalDate.now(), 25 );
        when( service.createOrUpdate( medicineDTO ) ).thenReturn( updatedMedicineDTO );

        ResponseEntity<MedicineDTO> response = controller.createOrUpdate( medicineDTO );

        assertTrue( response.getStatusCode().is2xxSuccessful() );
        assertNotNull( response.getBody() );
        assertEquals( "Amoxicilina", response.getBody().name() );
        assertEquals( "500mg", response.getBody().dosage() );
        assertEquals( "Amoxicilina tri-hidratada", response.getBody().formulation() );
        verify( service, times( 1 ) ).createOrUpdate( medicineDTO );
    }

    private static MedicineDTO getAmoxicilina()
    {
        return new MedicineDTO( null, "Amoxicilina", "Amoxicilina tri-hidratada", "500mg", LocalDate.now(), 10 );
    }

    @Test
    @DisplayName( "Should delete medicine by ID" )
    void deleteRemovesMedicine()
    {
        doNothing().when( service ).delete( 1L );

        ResponseEntity<Void> response = controller.delete( 1L );

        assertTrue( response.getStatusCode().is2xxSuccessful() );
        verify( service, times( 1 ) ).delete( 1L );
    }
}