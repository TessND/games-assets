package com.tessnd.games_assets.forum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ForumMessageRepository extends JpaRepository<ForumMessage, Long> {
    public List<ForumMessage> findByForumThread(ForumThread forumThread);
    public void deleteByForumThreadId(Long forumThreadId);
}
