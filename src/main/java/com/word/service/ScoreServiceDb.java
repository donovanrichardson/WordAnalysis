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
import java.util.Comparator;
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

    Comparator<Score> byScore = (Score s1, Score s2) ->
            s1.getValue().compareTo(s2.getValue());

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

        List<Score> result = scoreAccess.saveScores(scores); //done implement
        result.sort(byScore);
        return result;
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
        double sumOfSquares = 0; //initializing
        WordText dummy = new WordText(); //dummy wordtext for when the first wordtext precedes begintime
        dummy.setTime(beginTime);
        WordText endWt = new WordText(); //dummy wordText to represend endTime
        endWt.setTime(endTime);

        List<WordText> intervalWordTexts = wtService.getWordTextsWithinInterval(w, beginTime, endTime); //gets wt within interval
        try{
            WordText preceding = wtService.getPrecedingWordText(w, beginTime); // preceding wt if any. if none, catches error


            dummy.setDifference(Util.diffMilliToSecond(dummy.getTime().getTime(),preceding.getTime().getTime())); //dummy gets its own difference based on preceding
            intervalWordTexts.add(0, dummy);//adds dummy to beginning of list
            WordText lastWt = intervalWordTexts.get(intervalWordTexts.size()-1); //if there are none in between begin and end, then this is the dummy

            double secsDiff;//diff for lastWt
            if(lastWt.equals(dummy)){
                secsDiff = Util.diffMilliToSecond(endTime.getTime(), preceding.getTime().getTime()); // in this case, the lastWt diff is also based on preceding
            }else{
                secsDiff = Util.diffMilliToSecond(endTime.getTime(), lastWt.getTime().getTime()); //otherwise it is based on last between begin and end
            }
            endWt.setDifference(secsDiff);
            intervalWordTexts.add(endWt);
            sumOfSquares += (Math.pow(intervalWordTexts.get(1).getDifference(),2) - Math.pow(dummy.getDifference(),2)); //dummy == get(0)
        }catch(EmptyResultDataAccessException e){// if there is no preceding, then calculating a dummy shouldn't be an issue. also, i think i prevent a score from being calculated for an interval that includes time before the first instance of a given word, so this isn't strictly necessary.
            if (intervalWordTexts.size() == 0){
                return null;
            }
            beginTime = intervalWordTexts.get(0).getTime();//since beginTime is before the first between, reassign beginTime.
            intervalWordTexts.add(0, dummy); //adding a dummy just so that the indices for the for loop line up. the dummy and the first WordText between beginning and end (with a difference of 0) will be skipped.
            double secsDiff = Util.diffMilliToSecond(endTime.getTime(), intervalWordTexts.get(intervalWordTexts.size()-1).getTime().getTime()); //again, because of no preceding, there is no need to calculate difference based on preceding
            endWt.setDifference(secsDiff);
            intervalWordTexts.add(endWt); //adding endWt to the end
            //no noeed to add anything to sum of squares using the difference between the first two squares that i did before, since this instance will include the first wordtime instance of the given word.
        }


//        sumOfSquares = Math.pow(intervalWordTexts.get(1).getDifference(),2) - Math.pow(intervalWordTexts.get(0).getDifference(),2);  //todo what if get(0) is not the dummy WordText

        for(int i = 2; i < intervalWordTexts.size(); i++){
            sumOfSquares+=Math.pow(intervalWordTexts.get(i).getDifference(),2); //if the list is only two long, then the sumOfSquares has already been calculated and this won't run.
        }

        double scoreValue = 2 * (sumOfSquares/Util.diffMilliToSecond(endTime.getTime(),beginTime.getTime())); //it's not square root, its divided by the whole duration

        if(beginTime.equals(endTime)){
            return null;
        }

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

        if(bothSidesScore == null || bothSidesScore.getValue() > seconds){
            return null;
        }
        Score beginningScore = getScoreForWordInterval(w, beginTime, middleTime);
        Score endScore = getScoreForWordInterval(w, middleTime, endTime);

        return constructRatioScore(beginningScore, endScore);

    }

    private Score constructRatioScore(Score beginningScore, Score endScore) { //impl done
        if(beginningScore == null || endScore == null){
            return null;
        }
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
