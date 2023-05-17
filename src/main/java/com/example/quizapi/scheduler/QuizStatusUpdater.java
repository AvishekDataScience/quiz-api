package com.example.quizapi.scheduler;

import com.example.quizapi.model.Quiz;
import com.example.quizapi.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class QuizStatusUpdater {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizStatusUpdater(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Scheduled(cron = "0 * * * * *") // Runs every minute
    public void updateQuizStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> quizzes = quizRepository.findAll();
        for (Quiz quiz : quizzes) {
            if (now.isBefore(quiz.getStartDate())) {
                quiz.setStatus("inactive");
            } else if (now.isAfter(quiz.getEndDate())) {
                quiz.setStatus("finished");
            } else {
                quiz.setStatus("active");
            }
            quizRepository.save(quiz);
        }
    }
}
