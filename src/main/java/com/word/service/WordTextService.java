package com.word.service;

import com.word.domain.Score;
import com.word.domain.Text;
import com.word.domain.Word;
import com.word.domain.WordText;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface WordTextService {
    WordText getMostRecentUsage(String s);

    WordText createWordText(Word currentWord, Text createdText, WordText lastWord);

    /**
     * order newest to oldest. Desc. Make sure that if the whole interval is empty, something is still returned
     * @param w
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WordText> getWordTextsWithinInterval(Word w, Timestamp beginTime, Timestamp endTime);

    WordText getPrecedingWordText(Timestamp beginTime);
}

