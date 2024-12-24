package com.tessnd.games_assets.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tessnd.games_assets.project.Project;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    public List<Comment> findByProject(Project project);
    public void deleteByProjectId(Long projectId);
}
