package com.example.questionservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class Initializer implements ApplicationRunner {

    private final QuestionRepository questionRepo;

    public Initializer(QuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        questionRepo.deleteAll()
                .thenMany(Flux.just("q1", "q2", "q3", "q4", "q5"))
                .map(name -> new Question(null, name, ThreadLocalRandom.current().nextInt(1, 100)))
                .flatMap(questionRepo::save)
                .thenMany(questionRepo.findAll())
                .subscribe(q -> log.info("Created: " + q));
    }
}
