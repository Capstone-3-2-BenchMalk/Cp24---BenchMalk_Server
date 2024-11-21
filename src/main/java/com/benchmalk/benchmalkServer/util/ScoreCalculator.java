package com.benchmalk.benchmalkServer.util;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.domain.ClovaSentence;
import com.benchmalk.benchmalkServer.clova.domain.ClovaWord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScoreCalculator {

    public Integer calculateSentenceWPM(ClovaSentence sentence) {
        int wordCount = sentence.getClovaWords().size();
        long time = sentence.getEnd() - sentence.getStart();
        return (int) (((double) wordCount / time) * 60000);
    }

    public Integer calculateAnalysisWPM(ClovaAnalysis analysis) {
        int wordCount = 0;
        long time = 0L;
        for (ClovaSentence s : analysis.getSentences()) {
            wordCount += s.getClovaWords().size();
            time += s.getEnd() - s.getStart();
        }
        return (int) (((double) wordCount / time) * 60000);
    }

    public Integer calculateAnalysisRest(ClovaAnalysis analysis) {
        int restCount = 0;
        Long restSum = 0L;
        List<ClovaSentence> sentences = analysis.getSentences();
        restCount += sentences.size() - 1;
        for (int i = 0; i < sentences.size(); i++) {
            if (i < sentences.size() - 1) {
                restSum += sentences.get(i + 1).getStart() - sentences.get(i).getEnd();
            }
            restCount += sentences.get(i).getClovaWords().size() - 1;
            List<ClovaWord> words = sentences.get(i).getClovaWords();
            for (int j = 0; j < words.size() - 1; j++) {
                restSum += words.get(j + 1).getStart() - words.get(j).getEnd();
            }
        }

        Long avgRest = restSum / restCount;
        int longRestCount = 0;
        for (int i = 0; i < sentences.size() - 1; i++) {
            if (sentences.get(i + 1).getStart() - sentences.get(i).getEnd() >= avgRest) {
                longRestCount++;
            }
            List<ClovaWord> words = sentences.get(i).getClovaWords();
            for (int j = 0; j < words.size() - 1; j++) {
                if (words.get(j + 1).getStart() - words.get(j).getEnd() >= avgRest) {
                    longRestCount++;
                }
            }
        }
        return longRestCount;
    }
}
