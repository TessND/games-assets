package com.tessnd.games_assets.forum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tessnd.games_assets.user.User;

import jakarta.transaction.Transactional;

@Service
public class ForumMessageService {
    @Autowired
    private ForumMessageRepository forumMessageRepository;

    @Transactional
    public void save(ForumMessageCreateDTO forumMessageCreateDTO, User user, ForumThread forumThread) {
        ForumMessage forumMessageEntity = new ForumMessage();
        forumMessageEntity.setContent(forumMessageCreateDTO.getContent());
        forumMessageEntity.setUser(user);
        forumMessageEntity.setForumThread(forumThread);
        forumMessageRepository.save(forumMessageEntity);
    }

    @Transactional
    public void delete(ForumMessage forumMessage) {
        forumMessageRepository.delete(forumMessage);
    }

    public List<ForumMessage> getAllByForumThread(ForumThread forumThread) {
        return forumMessageRepository.findByForumThread(forumThread);
    }

    @Transactional
    public void deleteAllByForumThreadId(Long id) {
        forumMessageRepository.deleteByForumThreadId(id);
    }
}
