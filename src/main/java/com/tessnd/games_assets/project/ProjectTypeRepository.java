package com.tessnd.games_assets.project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long>{

    Optional<ProjectType> findByName(String roleName);
}