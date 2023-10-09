package com.java.wordcounter.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface WordCounterService {

     void processText(String text);
     Map<String, Integer> getWordCount();
     void clear();
}
