package com.project.fasthrm.repository;

import com.project.fasthrm.domain.Edu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EduRepository extends JpaRepository<Edu, Long> {
}