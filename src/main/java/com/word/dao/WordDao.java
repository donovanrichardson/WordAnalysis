package com.word.dao;

import com.word.domain.Word;

public interface WordDao {
    Word getMostRecentUsage(String s);
}
