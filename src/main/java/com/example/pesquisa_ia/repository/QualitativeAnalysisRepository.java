package com.example.pesquisa_ia.repository;

import com.example.pesquisa_ia.entities.QualitativeAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QualitativeAnalysisRepository extends JpaRepository<QualitativeAnalysisEntity, Long> {

    Optional<QualitativeAnalysisEntity> findByResearchResponseId(
            Long researchResponseId
    );
}
