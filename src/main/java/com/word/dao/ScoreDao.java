package com.word.dao;

import com.word.domain.Score;
import com.word.domain.Word;

import java.util.List;

public interface ScoreDao {

    void saveScores(List<Score> scores);

    void saveScore(Score result);

    Score constructRatioScore(Score beginningScore, Score endScore);
}
