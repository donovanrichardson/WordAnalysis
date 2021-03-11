package com.word.controller;

import com.word.domain.Score;
import com.word.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @PostMapping
    public List<Score> getScores(@RequestParam Optional<Integer> hours, @RequestParam Optional<Integer> days, @RequestParam boolean ratio){

        int seconds;

        if(hours.isPresent()){
            seconds = hours.get() * 3600;
        }else{
            seconds = days.get()*86400;
        }

        return scoreService.getScores(seconds, ratio);
    }

    @PostMapping("/{wordId}")
    public Score getScore(@PathVariable int wordId, @RequestParam Optional<Integer> hours, @RequestParam Optional<Integer> days, @RequestParam boolean ratio){

        int seconds;

        if(hours.isPresent()){
            seconds = hours.get() * 3600;
        }else{
            seconds = days.get()*86400;
        }

        return scoreService.getScore(wordId, seconds, ratio);

    }

}
