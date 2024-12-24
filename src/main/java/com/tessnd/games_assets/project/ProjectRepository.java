package com.tessnd.games_assets.project;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    public Project findByFileName(String filename);
}
