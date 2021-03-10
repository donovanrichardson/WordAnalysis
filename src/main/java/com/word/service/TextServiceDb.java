package com.word.service;

import com.word.dao.TextDao;
import com.word.domain.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TextServiceDb implements TextService {

    @Autowired
    TextDao textAccess;

    @Override
    public Text createText(Text t) {
        if(t.getTime() == null){
            t.setTime(new Timestamp(System.currentTimeMillis()));
        }
        Text lastText = getLastLink();
        if(lastText != null){
            long newMillis = t.getTime().getTime();
            long oldMillis = lastText.getTime().getTime();
            Long diff = newMillis - oldMillis;
            double diffSecs = diff.doubleValue()     / 1000;
            t.setDifference(diffSecs);
        }
        Text createdText = textAccess.createText(t);
        return createdText;
    }

    @Override
    public List<Text> getTexts() {
        return textAccess.getTexts();
    }

    @Override
    public Text getTextById(int id) {
        return textAccess.getTextById(id);
    }

    @Override
    public List<Text> getTextsByAuthor(String author) {
        return textAccess.getTextsByAuthor(author);
    }

    @Override
    public Text getLastLink() {
        return textAccess.getLastLink();
    }
}
