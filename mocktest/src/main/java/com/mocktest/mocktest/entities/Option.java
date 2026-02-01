package com.mocktest.mocktest.entities;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @Column(nullable = false)
    private String optionTextEn;
    
     @Column(nullable = false)
    private String optionTextTe;

    @Column(nullable = false)
    private boolean isCorrect;

    // MANY Options → ONE Question
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // getters & setters
}
