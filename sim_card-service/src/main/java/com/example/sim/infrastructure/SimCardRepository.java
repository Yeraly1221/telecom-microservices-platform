package com.example.sim.infrastructure;


import com.example.sim.domain.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimCardRepository extends JpaRepository<SimCard, Long> {
}
