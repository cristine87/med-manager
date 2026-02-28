package org.cristine.med.manager.rest;

import java.time.LocalDate;

public record MedicineDTO(
    Long id,
    String name,
    String formulation,
    String dosage,
    LocalDate expiration,
    Integer quantity
) {}
