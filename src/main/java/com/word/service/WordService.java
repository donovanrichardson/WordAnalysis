package com.word.service;

import com.word.domain.Text;
import com.word.domain.Word;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public interface WordService {

    List<Word> createWords(Set<String> wordsFromText, Text createdText);

    Word getQualifyingWord(int wordId, int scoreRange, Timestamp endTime);

    List<Word> getQualifyingWords(int scoreRange, Timestamp endTime);
}
