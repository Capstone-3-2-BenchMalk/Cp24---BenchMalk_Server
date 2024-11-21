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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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
        analysis.setSentences(new ArrayList<>());
        analysis.setConfidence(response.getConfidence());
        response.getSegments().stream().forEach(segment -> {
            analysis.getSentences().add(parseSentence(segment, analysis));
        });
        return analysis;
    }

    private ClovaSentence parseSentence(ClovaResponseSegments segment, ClovaAnalysis analysis) {
        ClovaSentence sentence = new ClovaSentence();
        sentence.setSentence(segment.getText());
        sentence.setStart(segment.getStart());
        sentence.setEnd(segment.getEnd());
        sentence.setConfidence(segment.getConfidence());
        sentence.setClovaAnalysis(analysis);
        sentence.setClovaWords(parseWords(segment.getWords(), sentence));
        return sentence;
    }

    private List<ClovaWord> parseWords(List<List<?>> words, ClovaSentence sentence) {
        List<ClovaWord> wordsList = new ArrayList<ClovaWord>();
        for (int i = 0; i < words.size() - 1; i++) {
            ClovaWord word = new ClovaWord();
            word.setStart(Long.valueOf((Integer) words.get(i).get(0)));
            word.setEnd(Long.valueOf((Integer) words.get(i).get(1)));
            word.setWord(words.get(i).get(2).toString());
            word.setClovaSentence(sentence);
            wordsList.add(word);
        }
        return wordsList;
    }
}
