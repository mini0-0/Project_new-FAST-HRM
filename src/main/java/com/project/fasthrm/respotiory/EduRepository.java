package com.project.fasthrm.respotiory;

import com.project.fasthrm.domain.Edu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EduRepository extends JpaRepository<Edu, Long> {
}
