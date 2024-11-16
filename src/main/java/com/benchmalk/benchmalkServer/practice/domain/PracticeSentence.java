package com.benchmalk.benchmalkServer.practice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PracticeSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sentence;

    private Double confidence;
    private Long start;
    private Long end;

    private Integer wpm;
    private Integer energy;

    @OneToMany(mappedBy = "practiceSentence")
    private List<PracticeWord> practiceWords;

    @ManyToOne
    private PracticeAnalysis practiceAnalysis;
}
