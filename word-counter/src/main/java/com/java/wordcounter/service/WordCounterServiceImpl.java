package com.java.wordcounter.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WordCounterServiceImpl implements WordCounterService {

        private Map<String, AtomicInteger> wordCountMap = new HashMap<>();
        // Constructor for injecting a custom wordCountMap (for testing purposes)
        public WordCounterServiceImpl(Map<String, AtomicInteger> wordCountMap) {
            this.wordCountMap = wordCountMap;
        }


        @Override
        public void processText(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }

        String[] words = text.split("\\s+");
        Stream.of(words)
                .map(String::toLowerCase)
                .forEach(word -> wordCountMap.computeIfAbsent(word, k -> new AtomicInteger()).incrementAndGet());
        }

        @Override
        public Map<String, Integer> getWordCount() {
            return Collections.unmodifiableMap(wordCountMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get())));
        }

        @Override
        public void clear() {
            wordCountMap.clear();
        }
}







