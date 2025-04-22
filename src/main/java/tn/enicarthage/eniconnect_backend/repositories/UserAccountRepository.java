package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.UserAccount;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findOneByUsername(String username);

    @Query("""
            SELECT COUNT(u) > 0 FROM UserAccount u
            WHERE u.username = :username AND u.student.id = :studentId
            """)
    public boolean isOwner(@Param("username") String username, @Param("studentId") UUID studentId);
}
