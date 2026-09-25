package com.example.pesquisa_ia.repository;

import com.example.pesquisa_ia.entities.ResearchResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchResponseRepository extends JpaRepository<ResearchResponseEntity, Long> {

    List<ResearchResponseEntity> findByResearchId(Long researchId);

}
