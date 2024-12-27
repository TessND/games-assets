package com.tessnd.games_assets.forum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tessnd.games_assets.user.UserRepository;
import com.tessnd.games_assets.user.exceptions.ForumThreadNotFoundException;

import jakarta.transaction.Transactional;


@Service
public class ForumThreadService {
    @Autowired
    private ForumThreadRepository forumThreadRepository;

    @Autowired
    private ForumMessageService forumMessageService;

    @Autowired
    private UserRepository userRepository;

    public List<ForumThread> getAllForumThreads() {
        return forumThreadRepository.findAll();
    }

    public ForumThread getForumThreadById(Long id) {
        return forumThreadRepository.findById(id).orElseThrow(() -> new ForumThreadNotFoundException("Forum thread not found"));
    }
    
    @Transactional
    public ForumThread saveForumThread(ForumThreadCreateDTO forumThread, String username) {
        ForumThread forumThreadEntity = new ForumThread();
        forumThreadEntity.setTitle(forumThread.getTitle());
        forumThreadEntity.setDescription(forumThread.getDescription());
        forumThreadEntity.setUser(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        return forumThreadRepository.save(forumThreadEntity);
    }

    @Transactional
    public void deleteForumThread(Long id) {
        if (!forumThreadRepository.existsById(id)) {
            throw new ForumThreadNotFoundException("Forum thread not found");
        }
        forumMessageService.deleteAllByForumThreadId(id);
        forumThreadRepository.deleteById(id);
    }

    @Transactional
    public ForumThread editForumThread(Long id, ForumThreadCreateDTO forumThread) {
        ForumThread forumThreadEntity = getForumThreadById(id);
        forumThreadEntity.setTitle(forumThread.getTitle());
        forumThreadEntity.setDescription(forumThread.getDescription());
        return forumThreadRepository.save(forumThreadEntity);
    }

    public boolean isForumThreadOwner(Long id, String username) {
        return getForumThreadById(id).getUser().getUsername().equals(username);
    }

    public boolean isThreadOwner(Long id, String name) {
        return forumThreadRepository.findById(id).get().getUser().getUsername().equals(name);
    }
}
