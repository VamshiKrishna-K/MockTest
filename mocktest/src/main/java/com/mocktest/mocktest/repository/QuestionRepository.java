package com.mocktest.mocktest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mocktest.mocktest.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByExam_ExamNameAndExam_SetNo(String examName, int setNo);
}

