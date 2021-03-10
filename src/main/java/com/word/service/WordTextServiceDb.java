package com.word.service;

import com.word.dao.WordTextDao;
import com.word.domain.Text;
import com.word.domain.Word;
import com.word.domain.WordText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordTextServiceDb implements WordTextService {

    @Autowired
    WordTextDao wordTextAccess;

    @Override
    public WordText getMostRecentUsage(String s) {
        return wordTextAccess.getMostRecentUsage(s);
    }

    @Override
    public WordText createWordText(Word currentWord, Text createdText, WordText lastWord) {
        WordText wtForCreation = new WordText();
        if(lastWord != null){
            wtForCreation.setDifference(Util.diffMilliToSecond(createdText.getTime().getTime(),lastWord.getTime().getTime()));
        }
        wtForCreation.setTextId(createdText.getId());
        wtForCreation.setWordId(currentWord.getId());
        return wordTextAccess.createWordText(wtForCreation);
    }
}
