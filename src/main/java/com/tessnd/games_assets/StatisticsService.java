package com.tessnd.games_assets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tessnd.games_assets.comment.CommentRepository;
import com.tessnd.games_assets.forum.ForumMessageRepository;
import com.tessnd.games_assets.forum.ForumThreadRepository;
import com.tessnd.games_assets.project.ProjectRepository;
import com.tessnd.games_assets.user.UserRepository;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ForumThreadRepository forumThreadRepository;

    @Autowired
    private ForumMessageRepository forumMessageRepository;

    public void generateStatistics() {
        // Получаем статистику из репозиториев
        long userCount = userRepository.count();
        long projectCount = projectRepository.count();
        long commentCount = commentRepository.count();
        long forumThreadCount = forumThreadRepository.count();
        long forumMessageCount = forumMessageRepository.count();

        // Записываем статистику в файл
        try (FileWriter writer = new FileWriter("statistics.txt")) {
            writer.write("Статистика:\n");
            writer.write("Количество пользователей: " + userCount + "\n");
            writer.write("Количество проектов: " + projectCount + "\n");
            writer.write("Количество комментариев: " + commentCount + "\n");
            writer.write("Количество форумных тредов: " + forumThreadCount + "\n");
            writer.write("Количество сообщений в форумных тредах: " + forumMessageCount + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
