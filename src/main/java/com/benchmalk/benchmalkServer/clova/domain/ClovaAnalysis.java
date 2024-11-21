package com.benchmalk.benchmalkServer.clova.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClovaAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double Confidence;

    private Integer wpm;
    private Integer pitch;
    private Integer rest;

    @OneToMany(mappedBy = "clovaAnalysis", cascade = CascadeType.ALL)
    private List<ClovaSentence> sentences;
}
