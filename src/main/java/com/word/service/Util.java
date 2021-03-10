package com.word.service;

public class Util {

    static double diffMilliToSecond(long greater, long lesser){
        Long diff = greater - lesser;
        double diffSecs = diff.doubleValue() / 1000;
        return diffSecs;
    }

}
