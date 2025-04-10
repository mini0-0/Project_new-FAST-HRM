package com.project.fasthrm.repository;

import com.project.fasthrm.domain.AcademicCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicCheckRepository extends JpaRepository<AcademicCheck, Long> {
}
