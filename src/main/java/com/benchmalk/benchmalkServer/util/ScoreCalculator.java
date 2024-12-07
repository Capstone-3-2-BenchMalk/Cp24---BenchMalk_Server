package com.benchmalk.benchmalkServer.util;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.domain.ClovaSentence;
import com.benchmalk.benchmalkServer.clova.domain.ClovaWord;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
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

        List<ClovaSentence> sentences = analysis.getSentences();
        int longRestCount = 0;
        for (int i = 0; i < sentences.size() - 1; i++) {
            if (sentences.get(i + 1).getStart() - sentences.get(i).getEnd() >= 180) {
                longRestCount++;
            }
            List<ClovaWord> words = sentences.get(i).getClovaWords();
            for (int j = 0; j < words.size() - 1; j++) {
                if (words.get(j + 1).getStart() - words.get(j).getEnd() >= 180) {
                    longRestCount++;
                }
            }
        }
        return longRestCount;
    }

    public Float analyzeSD(List<Float> data) {
        SummaryStatistics stats = new SummaryStatistics();
        data.forEach(p -> {
            if (p != 0 && !p.isInfinite() && !p.isNaN()) {
                stats.addValue(p);
            }
        });
        return (float) stats.getStandardDeviation();
    }

}
