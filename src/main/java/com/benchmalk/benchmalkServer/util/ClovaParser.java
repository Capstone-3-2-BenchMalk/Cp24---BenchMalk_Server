package com.benchmalk.benchmalkServer.util;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.domain.ClovaSentence;
import com.benchmalk.benchmalkServer.clova.domain.ClovaWord;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponse;
import com.benchmalk.benchmalkServer.clova.dto.ClovaResponseSegments;
import com.benchmalk.benchmalkServer.clova.repository.ClovaAnalysisRepository;
import com.benchmalk.benchmalkServer.clova.repository.ClovaSentenceRepository;
import com.benchmalk.benchmalkServer.clova.repository.ClovaWordRepository;
import com.benchmalk.benchmalkServer.common.exception.CustomException;
import com.benchmalk.benchmalkServer.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClovaParser {
    private final ClovaAnalysisRepository clovaAnalysisRepository;
    private final ClovaSentenceRepository clovaSentenceRepository;
    private final ClovaWordRepository clovaWordRepository;

    public ClovaAnalysis parse(ClovaResponse response) {
        ClovaAnalysis analysis = new ClovaAnalysis();
        if (!response.getResult().equals("COMPLETED")) {
            System.out.println(response.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        clovaAnalysisRepository.save(analysis);
        response.getSegments().stream().forEach(segment -> {
            parseSentence(segment, analysis.getId());
        });
        return analysis;
    }

    private void parseSentence(ClovaResponseSegments segment, Long analysisId) {
        ClovaSentence sentence = new ClovaSentence();
        sentence.setSentence(segment.getText());
        sentence.setStart(segment.getStart());
        sentence.setEnd(segment.getEnd());
        sentence.setClovaAnalysis(clovaAnalysisRepository.findById(analysisId).get());
        clovaSentenceRepository.save(sentence);
        parseWords(segment.getWords(), sentence.getId());
    }

    private void parseWords(List<List<Object>> words, Long sentenceId) {
        List<ClovaWord> wordsList = new ArrayList<ClovaWord>();
        for (int i = 0; i < words.size() - 1; i++) {
            ClovaWord word = new ClovaWord();
            word.setStart(Long.valueOf((Integer) words.get(i).get(0)));
            word.setEnd(Long.valueOf((Integer) words.get(i).get(1)));
            word.setWord(words.get(i).get(2).toString());
            word.setClovaSentence(clovaSentenceRepository.findById(sentenceId).get());
            clovaWordRepository.save(word);
        }
    }
}
