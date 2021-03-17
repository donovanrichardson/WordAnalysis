package com.word.service;

import com.word.dao.ScoreDao;
import com.word.domain.Score;
import com.word.domain.Word;
import com.word.domain.WordText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreServiceDb implements ScoreService {

    @Autowired
    WordService wordService;

    @Autowired
    ScoreDao scoreAccess;

    @Autowired
    WordTextService wtService;

    @Autowired
    TextService textService;

//    private Timestamp endTime;

    @Override
    public List<Score> getScores(int seconds, boolean ratio) {

        Timestamp endTime = textService.getLastLink().getTime();

        List<Score> scores = new ArrayList<>();

        int scoreRange = this.getScoreRange(seconds, ratio);
        List<Word> qualifyingWords = wordService.getQualifyingWords(scoreRange, endTime); //done impl method; I will call other domain's services from this service, not their daos.

        for(Word w : qualifyingWords){
            Score s = this.getScoreForWord(w, seconds, ratio, endTime); //impl'd
            scores.add(s);
        }

        while (scores.remove(null));

        return scoreAccess.saveScores(scores); //done implement
//        return scores;
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
            return getScoreForWordInterval(w, beginTime, endTime);
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
        Timestamp beginTime = new Timestamp(endTime.getTime() - (seconds * 1000));
        return scoreAccess.getRecentScoreForWord(w, ratio, beginTime, endTime); //impl done

    }

    private Score getScoreForWordInterval(Word w, Timestamp beginTime, Timestamp endTime) {

        List<WordText> intervalWordTexts = wtService.getWordTextsWithinInterval(w, beginTime, endTime); //done order desc
        try{
            WordText preceding = wtService.getPrecedingWordText(w, beginTime); //done impl
//            intervalWordTexts.add(0, preceding);
//            initialize sumOfSquares = Math.pow(intervalWordTexts.get(0).getDifference(),2) - Math.pow(preceding.getDifference(),2); here, and then initialize the divisor add the difference between the non squares to the divisor.
        }catch(EmptyResultDataAccessException e){
            //
        }
        WordText endWt = new WordText();
        endWt.setTime(endTime);
        double secsDiff = Util.diffMilliToSecond(endTime.getTime(), intervalWordTexts.get(intervalWordTexts.size()-1).getTime().getTime());
        endWt.setDifference(secsDiff);
        intervalWordTexts.add(endWt);

        double sumOfSquares = Math.pow(intervalWordTexts.get(1).getDifference(),2) - Math.pow(intervalWordTexts.get(0).getDifference(),2);

        for(int i = 2; i < intervalWordTexts.size(); i++){
            sumOfSquares+=Math.pow(intervalWordTexts.get(i).getDifference(),2);
        }

        double scoreValue = 2 * Math.sqrt(sumOfSquares); //it's not square root, its divided by the whole duration

        Score s = new Score();
        s.setWord(w);
        s.setRatio(false);
        s.setValue(scoreValue);
        s.setBegin(beginTime);
        s.setEnd(endTime);

        return s;

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

    private Score constructRatioScore(Score beginningScore, Score endScore) { //impl done
        Score res = new Score();
        res.setRatio(true);
        res.setBegin(beginningScore.getBegin());
        res.setEnd(endScore.getEnd());
        res.setWord(beginningScore.getWord());
        res.setValue(endScore.getValue()/beginningScore.getValue());

        return res;
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
        Timestamp endTime = textService.getLastLink().getTime();
        int scoreRange = this.getScoreRange(seconds, ratio);
        Word qualifyingWord = wordService.getQualifyingWord(wordId, scoreRange, endTime); //done impl method
        if(qualifyingWord == null){
            return null;
        }
        Score result = this.getScoreForWord(qualifyingWord, seconds, ratio, endTime);
        return scoreAccess.saveScore(result);//done impl
//        return result;
    }
}
