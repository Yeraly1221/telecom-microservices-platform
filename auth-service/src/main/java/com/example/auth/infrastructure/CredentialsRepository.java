package com.example.auth.infrastructure;

import com.example.auth.domain.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findByEmail(String email);

    Boolean existsByEmail(String email);
}
