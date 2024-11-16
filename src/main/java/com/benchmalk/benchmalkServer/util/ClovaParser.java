package com.benchmalk.benchmalkServer.util;

import com.benchmalk.benchmalkServer.clova.dto.ClovaResponse;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponseSegments;
import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import com.benchmalk.benchmalkServer.practice.domain.PracticeAnalysis;
import com.benchmalk.benchmalkServer.practice.domain.PracticeSentence;
import com.benchmalk.benchmalkServer.practice.domain.PracticeWord;
import com.benchmalk.benchmalkServer.practice.repository.PracticeAnalysisRepository;
import com.benchmalk.benchmalkServer.practice.repository.PracticeSentenceRepository;
import com.benchmalk.benchmalkServer.practice.repository.PracticeWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClovaParser {
    private final PracticeAnalysisRepository practiceAnalysisRepository;
    private final PracticeSentenceRepository practiceSentenceRepository;
    private final PracticeWordRepository practiceWordRepository;

    public PracticeAnalysis parsePractice(ClovaResponse response) {
        PracticeAnalysis analysis = new PracticeAnalysis();
        if (!response.getResult().equals("COMPLETED")) {
            System.out.println(response.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        practiceAnalysisRepository.save(analysis);
        response.getSegments().stream().forEach(segment -> {
            parseSentence(segment, analysis.getId());
        });
        return analysis;
    }

    private void parseSentence(ClovaResponseSegments segment, Long id) {
        PracticeSentence sentence = new PracticeSentence();
        sentence.setSentence(segment.getText());
        sentence.setStart(segment.getStart());
        sentence.setEnd(segment.getEnd());
        sentence.setPracticeAnalysis(practiceAnalysisRepository.findById(id).get());
        practiceSentenceRepository.save(sentence);
        parseWords(segment.getWords(), sentence.getId());
    }

    private void parseWords(List<List<Object>> words, Long id) {
        List<PracticeWord> wordsList = new ArrayList<PracticeWord>();
        for (int i = 0; i < words.size() - 1; i++) {
            PracticeWord word = new PracticeWord();
            word.setStart(Long.valueOf((Integer) words.get(i).get(0)));
            word.setEnd(Long.valueOf((Integer) words.get(i).get(1)));
            word.setWord(words.get(i).get(2).toString());
            word.setPracticeSentence(practiceSentenceRepository.findById(id).get());
            practiceWordRepository.save(word);
        }
    }
}
