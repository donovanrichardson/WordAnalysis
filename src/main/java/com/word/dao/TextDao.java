package com.word.dao;

import com.word.domain.Text;

import java.util.List;

public interface TextDao {
    Text createText(Text t);

    List<Text> getTexts();

    Text getTextById(int id);

    List<Text> getTextsByAuthor(String author);
}
