package com.project.fasthrm.repository;

import com.project.fasthrm.domain.Takes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakesRepository extends JpaRepository<Takes, Long> {
}
