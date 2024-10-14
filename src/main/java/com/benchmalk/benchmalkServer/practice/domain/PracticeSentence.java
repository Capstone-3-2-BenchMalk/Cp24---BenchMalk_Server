package com.benchmalk.benchmalkServer.practice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PracticeSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sentence;

    private Integer wpm;
    private Integer energy;

    @ManyToOne
    private PracticeAnalysis practice_analysis;
}
