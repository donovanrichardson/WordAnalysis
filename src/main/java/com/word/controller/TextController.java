package com.word.controller;

import com.word.domain.Text;
import com.word.service.TextService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService){
        this.textService = textService;
    }

    @GetMapping
    public List<Text> all(){
        return textService.getTexts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Text create(@RequestBody Text t){
        return textService.createText(t);
    }

    @GetMapping("/{id}")
    public Text textById(@PathVariable int id){
        return textService.getTextById(id);
    }

    @GetMapping("/{author}")
    public List<Text> textsWithAuthor(@PathVariable String author){
        return textService.getTextsByAuthor(author);
    }




}
