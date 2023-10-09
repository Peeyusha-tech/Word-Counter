package com.java.wordcounter.controller;

import com.java.wordcounter.service.WordCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class WordCounterController {

    @Autowired
    WordCounterService wordCounterService;

    private final Map<String, AtomicInteger> wordCountMap;

    public WordCounterController() {
        // Use ConcurrentHashMap for thread safety.
        this.wordCountMap = new ConcurrentHashMap<>();
    }

    @GetMapping("/getWordCount")
    public Map<String, Integer> countWords(){

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        String[] texts = {"This is a sample text.", "This is a sample text." , "Sample text for testing.", "Another text to process."};

        for (String text : texts) {
            executorService.submit(() -> wordCounterService.processText(text));
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            // Wait for all threads to finish
        }

        Map<String, Integer> wordCounts = wordCounterService.getWordCount();
        wordCounts.forEach((word, count) -> System.out.println(word + ": " + count));
        wordCounterService.clear();

        return  wordCounts;
    }
}
