package com.word.service;

import com.word.domain.Text;
import com.word.domain.Word;
import com.word.domain.WordText;

public interface WordTextService {
    WordText getMostRecentUsage(String s);

    WordText createWordText(Word currentWord, Text createdText, WordText lastWord);
}

