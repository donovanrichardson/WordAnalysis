package com.word.service;

import com.word.dao.TextDao;
import com.word.domain.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextServiceDb implements TextService {

    @Autowired
    TextDao textAccess;

    @Autowired
    WordService wordService;

    @Override
    public Text createText(Text t) {
        if(t.getTime() == null){
            t.setTime(new Timestamp(System.currentTimeMillis()));
        }
        Text lastText = getLastLink();
        if(lastText != null){
            long newMillis = t.getTime().getTime();
            long oldMillis = lastText.getTime().getTime();
            double diffSecs = Util.diffMilliToSecond(newMillis, oldMillis);
            t.setDifference(diffSecs);
        }
        Text createdText = textAccess.createText(t);

        Set<String> wordsFromText = getWordsFromText(createdText);

        wordService.createWords(wordsFromText, createdText);

        return createdText;
    }

    private Set<String> getWordsFromText(Text createdText) {
        Pattern reg = Pattern.compile("[^\\p{Punct}\\s]+");
        Matcher m = reg.matcher("hello world! it is me, and you're dying");
        List<String> wordsFromText = new ArrayList<>();
        while(m.find()){
            wordsFromText.add(m.group().toLowerCase());
        }
        return new HashSet<>(wordsFromText);
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
