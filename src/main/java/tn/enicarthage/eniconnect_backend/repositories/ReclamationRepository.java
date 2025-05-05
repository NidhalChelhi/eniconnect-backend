package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.Reclamation;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
}
