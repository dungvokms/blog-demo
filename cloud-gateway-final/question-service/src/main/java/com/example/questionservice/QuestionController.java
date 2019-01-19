package com.example.questionservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionRepository questionRepo;

    public QuestionController(QuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    @GetMapping
    public Flux<Question> findAll() {
        return questionRepo.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Question> findById(@PathVariable String id) {
        return questionRepo.findById(id);
    }
}   
