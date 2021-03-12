package com.word.dao;

import com.word.domain.Word;

import java.sql.Timestamp;
import java.util.List;

public interface WordDao {
    Word createWord(String s);

    Word getQualifyingWord(int wordId, int scoreRange, Timestamp endTime);

    List<Word> getQualifyingWords(int scoreRange, Timestamp endTime);
}