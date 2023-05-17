package com.example.quizapi.controller;

import com.example.quizapi.model.Quiz;
import com.example.quizapi.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        quiz.setStatus("inactive");
        Quiz createdQuiz = quizRepository.save(quiz);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    @GetMapping("/active")
    public ResponseEntity<Quiz> getActiveQuiz() {
        LocalDateTime now = LocalDateTime.now();
        Quiz activeQuiz = quizRepository.findByStartDateBeforeAndEndDateAfter(now, now);
        if (activeQuiz != null) {
            activeQuiz.setStatus("active");
            quizRepository.save(activeQuiz);
            return ResponseEntity.ok(activeQuiz);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<Integer> getQuizResult(@PathVariable("id") Long id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(quiz.getEndDate().plusMinutes(5))) {
                return ResponseEntity.ok(quiz.getRightAnswer());
            }
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return ResponseEntity.ok(quizzes);
    }
}
