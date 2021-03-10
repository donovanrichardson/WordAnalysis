package com.word.service;

import com.word.domain.WordText;

public interface WordTextService {
    WordText getMostRecentUsage(String s);
}
