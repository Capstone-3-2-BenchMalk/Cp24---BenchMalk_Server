package com.benchmalk.benchmalkServer.practice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PracticeAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer wps;
    private Integer pitch;

    @OneToMany(mappedBy = "practice_analysis")
    private List<PracticeSentence> sentences;
}
