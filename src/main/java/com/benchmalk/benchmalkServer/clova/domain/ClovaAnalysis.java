package com.benchmalk.benchmalkServer.clova.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    private Float Energy;

    private Double Confidence;

    private Integer wpm;
    private Integer pitch;
    private Integer rest;
    private Float restPerMinute;

    @Lob
    private String pitches;
    @Lob
    private String volumes;

    @OneToMany(mappedBy = "clovaAnalysis", cascade = CascadeType.ALL)
    private List<ClovaSentence> sentences;
}
