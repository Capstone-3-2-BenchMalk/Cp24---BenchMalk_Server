package com.benchmalk.benchmalkServer.util;

import com.benchmalk.benchmalkServer.clova.domain.ClovaAnalysis;
import com.benchmalk.benchmalkServer.clova.domain.ClovaSentence;
import com.benchmalk.benchmalkServer.clova.domain.ClovaWord;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public Float calculateSD(List<Float> data) {
        SummaryStatistics stats = new SummaryStatistics();
        List<Float> filterData = filterData(data);
        filterData.forEach(stats::addValue);
        double sigma = stats.getStandardDeviation();
        double mean = stats.getMean();
        stats.clear();
        filterData.forEach(p -> {
            if (p < mean + 2 * sigma) {
                stats.addValue(p);
            }
        });
        return (float) stats.getStandardDeviation();
    }

    public Integer calculateAccent(List<Float> pitches, List<Float> volumes) {
        SummaryStatistics vStats = new SummaryStatistics();
        SummaryStatistics pStats = new SummaryStatistics();
        List<Float> filterPitches = filterData(pitches);
        List<Float> filterVolumes = filterData(volumes);
        filterVolumes.forEach(vStats::addValue);
        filterPitches.forEach(pStats::addValue);
        double vSigma = vStats.getStandardDeviation();
        double vMean = vStats.getMean();
        double pSigma = pStats.getStandardDeviation();
        double pMean = pStats.getMean();
        int result = 0;
        int size = Math.min(filterPitches.size(), filterVolumes.size());
        boolean flag = true;
        for (int i = 0; i < size; i++) {
            Float p = pitches.get(i);
            Float v = volumes.get(i);
            if (filter(p) || filter(v)) {
                continue;
            }
            if (p > pMean + 2 * pSigma) {
                continue;
            }
            if (v > vMean + 2 * vSigma) {
                continue;
            }
            if (flag) {
                if (p > 1.5 * pMean || v > 1.1 * vMean) {
                    result += 1;
                    flag = false;
                }
            }
            if (!flag) {
                if (p <= 1.5 * pMean && v <= 1.1 * vMean) {
                    flag = true;
                }
            }
        }
        return result;
    }

    private List<Float> filterData(List<Float> data) {
        List<Float> filterData = removeTrashData(data);
        filterData = removeOutliers(filterData);
        return filterData;
    }

    private boolean filter(Float d) {
        return d == 0 || d.isInfinite() || d.isNaN();
    }

    private List<Float> removeTrashData(List<Float> data) {
        List<Float> filteredData = new ArrayList<>();
        data.forEach(d -> {
            if (!filter(d)) {
                filteredData.add(d);
            }
        });
        return filteredData;
    }

    private List<Float> removeOutliers(List<Float> data) {
        List<Float> filteredData = new ArrayList<>();
        SummaryStatistics stats = new SummaryStatistics();
        data.forEach(stats::addValue);
        double mean = stats.getMean();
        double sigma = stats.getStandardDeviation();
        data.forEach(d -> {
            if (d < mean + 2 * sigma && d > mean - 2 * sigma) {
                filteredData.add(d);
            }
        });
        return filteredData;
    }

    public Integer getMean(List<Float> data) {
        List<Float> filterData = removeTrashData(data);
        SummaryStatistics stats = new SummaryStatistics();
        data.forEach(stats::addValue);
        double sigma = stats.getStandardDeviation();
        double mean = stats.getMean();
        stats.clear();
        filterData.forEach(p -> {
            if (p < mean + 2 * sigma) {
                stats.addValue(p);
            }
        });
        double avg = stats.getMean();
        return (int) avg;
    }

}
