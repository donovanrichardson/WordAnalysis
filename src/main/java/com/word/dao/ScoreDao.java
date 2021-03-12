package com.word.dao;

import com.word.domain.Score;
import com.word.domain.Word;

import java.sql.Timestamp;
import java.util.List;

public interface ScoreDao {

    /**
     * Used to save scores in DB. Because some created scores are intermediate, this method is used to explicitly persist scores.
     *
     * @param scores
     */
    void saveScores(List<Score> scores);

    /**
     * Used to save a single score in DB. Because some created scores are intermediate, this method is used to explicitly persist scores.
     *
     * @param result
     */
    void saveScore(Score result);

    /**
     * Constructs a ratio score for two indvidual beginning and end scores
     *
     * @param beginningScore
     * @param endScore
     * @return
     */
    Score constructRatioScore(Score beginningScore, Score endScore);

    /**
     * Gets a recent score with begin and end for particular word and ratio.
     *
     * @param w
     * @param ratio
     * @param beginTime
     * @param endTime
     * @return may return null if there is none
     */
    Score getRecentScoreForWord(Word w, boolean ratio, Timestamp beginTime, Timestamp endTime);
}
