package com.word.service;

import com.word.dao.TextDao;
import com.word.dao.WordDao;
import com.word.domain.Text;
import com.word.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface WordService {

    List<Word> createWords(Set<String> wordsFromText, Text createdText);
}
