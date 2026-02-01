package com.mocktest.mocktest.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

@Getter
@Setter
@Entity
public class Exam {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;
    
    @Column(nullable = false)
    private String examName;

    @Column(nullable=false)
    private Long setNo;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private int totalMarks;

    @OneToMany(mappedBy = "exam")
    private List<Question> questions;
}
