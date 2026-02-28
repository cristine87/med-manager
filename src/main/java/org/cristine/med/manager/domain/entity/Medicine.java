package org.cristine.med.manager.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "med_seq_gen", sequenceName = "med_seq", allocationSize = 1)
@Table(name = "medicamentos",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_med_name_dosage",
        columnNames = {"name", "dosage"}
    )
)
public class Medicine
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "med_seq_gen")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String formulation;

    @Column(nullable = false)
    private String dosage;

    private LocalDate expiration;

    @Column(nullable = false)
    private Integer quantity;

    public Medicine() {}

    public Medicine(Long id, String name, String formulation, String dosage, LocalDate expiration, Integer quantity) {
        this.id = id;
        this.name = name;
        this.formulation = formulation;
        this.dosage = dosage;
        this.expiration = expiration;
        this.quantity = quantity;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getFormulation() { return formulation; }

    public String getDosage() { return dosage; }

    public LocalDate getExpiration() { return expiration; }

    public Integer getQuantity() { return quantity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof final Medicine medicine )) return false;
        return Objects.equals(id, medicine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Med{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", formulation='" + formulation + '\'' +
            ", dosage='" + dosage + '\'' +
            ", expiration=" + expiration +
            ", quantity=" + quantity +
            '}';
    }
}
