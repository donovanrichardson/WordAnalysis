package com.word.service;

import com.word.dao.WordDao;
import com.word.domain.Text;
import com.word.domain.Word;
import com.word.domain.WordText;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WordServiceDb implements WordService {

    @Autowired
    WordDao wordAccess;

    @Autowired
    WordTextService wtService;

    @Override
    public List<Word> createWords(Set<String> wordsFromText, Text createdText) {

        List<Word> createdWords = new ArrayList<>();

        for(String s: wordsFromText){
             WordText recent = wtService.getMostRecentUsage(s);
        }

        return null;
    }
}
