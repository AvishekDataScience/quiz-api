package com.example.quizapi.repository;

import com.example.quizapi.model.Quiz;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findByStartDateBeforeAndEndDateAfter(LocalDateTime start, LocalDateTime end);
}
