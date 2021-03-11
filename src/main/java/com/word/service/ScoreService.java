package com.word.service;

import com.word.domain.Score;

import java.util.List;

public interface ScoreService {
    List<Score> getScores(int seconds, boolean ratio);

    Score getScore(int wordId, int seconds, boolean ratio);
}
