package com.word.dao;

import com.word.domain.Word;
import com.word.domain.WordText;

import java.sql.Timestamp;
import java.util.List;

public interface WordTextDao {
    WordText getMostRecentUsage(String s);

    WordText createWordText(WordText wtForCreation);

    List<WordText> getWordTextsWithinInterval(Word w, Timestamp beginTime, Timestamp endTime);

    WordText getPrecedingWordText(Word w, Timestamp beginTime);
}