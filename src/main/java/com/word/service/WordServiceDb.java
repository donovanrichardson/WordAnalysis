package com.word.service;

import com.word.dao.WordDao;
import com.word.domain.Text;
import com.word.domain.Word;
import com.word.domain.WordText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WordServiceDb implements WordService {

    @Autowired
    WordDao wordAccess;

    @Autowired
    WordTextService wtService;

    @Override
    public List<Word> createWords(Set<String> wordsFromText, Text createdText) {

        List<Word> createdWords = new ArrayList<>();

        for(String s: wordsFromText){
             WordText lastWord = wtService.getMostRecentUsage(s); //fields may be null
             Word currentWord = wordAccess.createWord(s);
             WordText currentWordText = wtService.createWordText(currentWord, createdText, lastWord);
             createdWords.add(currentWord);
        }

        return createdWords;
    }
}
