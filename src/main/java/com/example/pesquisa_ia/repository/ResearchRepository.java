package com.example.pesquisa_ia.repository;

import com.example.pesquisa_ia.entities.Research;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchRepository extends JpaRepository<Research, Long> {
}
