package com.word.service;

import com.word.dao.WordTextDao;
import com.word.domain.Text;
import com.word.domain.Word;
import com.word.domain.WordText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class WordTextServiceDb implements WordTextService {

    @Autowired
    WordTextDao wtAccess;

    @Override
    public WordText getMostRecentUsage(String s) {
        return wtAccess.getMostRecentUsage(s);
    }

    @Override
    public WordText createWordText(Word currentWord, Text createdText, WordText lastWord) {
        WordText wtForCreation = new WordText();
        if(lastWord != null&& lastWord.getTime() != null){
            wtForCreation.setDifference(Util.diffMilliToSecond(createdText.getTime().getTime(),lastWord.getTime().getTime()));
        }
        wtForCreation.setTextId(createdText.getId());
        wtForCreation.setWordId(currentWord.getId());
        return wtAccess.createWordText(wtForCreation);
    }

    @Override
    public List<WordText> getWordTextsWithinInterval(Word w, Timestamp beginTime, Timestamp endTime) {
        return wtAccess.getWordTextsWithinInterval(w,beginTime,endTime);
    }

    @Override
    public WordText getPrecedingWordText(Word w, Timestamp beginTime) {
        return wtAccess.getPrecedingWordText(w, beginTime);
    }
}
