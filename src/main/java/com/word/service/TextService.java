package com.word.service;

import com.word.domain.Text;

import java.util.List;

public interface TextService {

    Text createText(Text t);
    List<Text> getTexts();
    Text getTextById(int id);
    List<Text> getTextsByAuthor(String author);

}
