package com.word.domain;

import java.sql.Timestamp;

public class Score {

    private Double value;
    private boolean ratio;
    private Timestamp begin;
    private Timestamp end;
    private Word word;
    private int id;

    public boolean isRatio() {
        return ratio;
    }

    public void setRatio(boolean ratio) {
        this.ratio = ratio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
