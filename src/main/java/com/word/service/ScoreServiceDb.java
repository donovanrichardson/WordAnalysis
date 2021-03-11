package com.word.service;

import com.word.dao.ScoreDao;
import com.word.domain.Score;
import com.word.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceDb implements ScoreService {

    @Autowired
    WordService wordService;

    @Autowired
    ScoreDao scoreAccess;

//    private Timestamp endTime;

    @Override
    public List<Score> getScores(int seconds, boolean ratio) {

        Timestamp endTime = new Timestamp(System.currentTimeMillis());

        List<Score> scores = new ArrayList<>();

        int scoreRange = this.getScoreRange(seconds, ratio);
        List<Word> qualifyingWords = wordService.getQualifyingWords(scoreRange, endTime); //todo impl method; I will call other domain's services from this service, not their daos.

        for(Word w : qualifyingWords){
            Score s = this.getScoreForWord(w, seconds, ratio, endTime); //todo; make sure that you check for update
            scores.add(s);
        }

        scoreAccess.saveScores(scores); //todo implement
        return scores; //todo eliminate nulls?
    }

    private Score getScoreForWord(Word w, int seconds, boolean ratio, Timestamp endTime) {
        Score recentScore = getRecentScoreForWord(w,seconds,ratio,endTime);
        if(recentScore != null){
            return recentScore;
        }
        if(ratio){
            return getScoreForWordWithRatio(w, seconds, endTime);
        }else{
            Timestamp beginTime = new Timestamp(endTime.getTime() - (seconds * 1000));
            return getScoreForWordInterval(w, beginTime, endTime); //todo make sure that you
        }
    }

    /**
     * Gets already existing score (within the past hour) for desired mode.
     * @param w
     * @param seconds
     * @param ratio
     * @param endTime
     * @return
     */
    private Score getRecentScoreForWord(Word w, int seconds, boolean ratio, Timestamp endTime) {
    }

    private Score getScoreForWordInterval(Word w, Timestamp beginTime, Timestamp endTime) {
    }

    private Score getScoreForWordWithRatio(Word w, Integer seconds, Timestamp endTime) {
        Timestamp beginTime = new Timestamp(endTime.getTime() - (seconds * 2000));
        Timestamp middleTime = new Timestamp(endTime.getTime() - (seconds * 1000));
        Score bothSidesScore = getScoreForWordInterval(w, beginTime, endTime);

        if(bothSidesScore.getValue() > seconds){
            return null;
        }
        Score beginningScore = getScoreForWordInterval(w, beginTime, middleTime);
        Score endScore = getScoreForWordInterval(w, middleTime, endTime);

        return constructRatioScore(beginningScore, endScore);

    }

    private Score constructRatioScore(Score beginningScore, Score endScore) {
        return scoreAccess.constructRatioScore(beginningScore, endScore);//todo implement
    }

    private int getScoreRange(int seconds, boolean ratio) {
        int scoreRange = seconds;
        if(ratio){
            scoreRange *= 2;
        }
        return scoreRange;
    }

    @Override
    public Score getScore(int wordId, int seconds, boolean ratio) {
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        int scoreRange = this.getScoreRange(seconds, ratio);
        Word qualifyingWord = wordService.getQualifyingWord(scoreRange, endTime); //todo impl method
        Score result = this.getScoreForWord(qualifyingWord, seconds, ratio, endTime);
        scoreAccess.saveScore(result);
        return result;
    }
}
