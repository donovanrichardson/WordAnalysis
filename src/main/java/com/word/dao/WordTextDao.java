package com.word.dao;

import com.word.domain.WordText;

public interface WordTextDao {
    WordText getMostRecentUsage(String s);

    WordText createWordText(WordText wtForCreation);
}