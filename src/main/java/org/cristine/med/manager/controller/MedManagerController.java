package org.cristine.med.manager.controller;

import org.cristine.med.manager.rest.MedicineDTO;
import org.cristine.med.manager.service.MedManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedManagerController
{
    private final MedManagerService service;

    public MedManagerController( MedManagerService service) {
        this.service = service;
    }

    @GetMapping
    public List<MedicineDTO> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> get(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MedicineDTO> createOrUpdate(@RequestBody MedicineDTO medicine) {
        MedicineDTO saved = service.createOrUpdate(medicine);
        return ResponseEntity.created(URI.create("/api/medicamentos/" + saved.id())).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
