package com.project.fasthrm.respotiory;

import com.project.fasthrm.domain.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {
}
