package com.tessnd.games_assets.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tessnd.games_assets.project.Project;
import com.tessnd.games_assets.user.User;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public void save(CommentCreateDTO comment, User user, Project project) {
        Comment commentToSave = new Comment();
        commentToSave.setText(comment.getText());
        commentToSave.setUser(user);
        commentToSave.setProject(project);
        commentRepository.save(commentToSave);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getAllByProject(Project project) {
        return commentRepository.findByProject(project);
    }

    @Transactional
    public void deleteAllByProjectId(Long id) {
        commentRepository.deleteByProjectId(id);
    }

    // public boolean isCommentOwner(Long commentId, String name) {
    //     return commentRepository.findById(commentId).get().getUser().getUsername().equals(name);
    // }
}
