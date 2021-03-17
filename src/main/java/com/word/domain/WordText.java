package com.word.domain;

import java.sql.Timestamp;
import java.util.Objects;

public class WordText {
    private int wordId;
    private int textId;
    private Double difference;
    private Timestamp time;

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordText wordText = (WordText) o;
        return wordId == wordText.wordId && textId == wordText.textId && Objects.equals(difference, wordText.difference) && Objects.equals(time, wordText.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordId, textId, difference, time);
    }
}
