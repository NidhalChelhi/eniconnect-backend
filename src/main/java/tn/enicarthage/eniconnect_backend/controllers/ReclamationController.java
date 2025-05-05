package tn.enicarthage.eniconnect_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.entities.Reclamation;
import tn.enicarthage.eniconnect_backend.enums.ReclamationStatus;
import tn.enicarthage.eniconnect_backend.repositories.ReclamationRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController

@RequestMapping("/api/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @GetMapping
    public List<Reclamation> getAll() {
        return reclamationRepository.findAll();
    }

    @PostMapping
    public Reclamation create(@RequestBody Reclamation reclamation) {
        reclamation.setCreatedAt(LocalDateTime.now());
        reclamation.setStatus(ReclamationStatus.PENDING);

        // Optional: if student is not provided, it's anonymous
        if (reclamation.getStudent() == null) {
            System.out.println("Anonymous reclamation submitted.");
        }

        return reclamationRepository.save(reclamation);
    }


    @PutMapping("/{id}/resolve")
    public Reclamation resolve(@PathVariable Long id) {
        Reclamation reclamation = reclamationRepository.findById(id).orElseThrow();
        reclamation.setStatus(ReclamationStatus.RESOLVED);
        reclamation.setResolvedAt(LocalDateTime.now());
        return reclamationRepository.save(reclamation);
    }
}



