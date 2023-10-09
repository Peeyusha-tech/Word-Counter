package com.java.wordcounter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class WordCounterServiceImplTest {

    @InjectMocks
    private WordCounterServiceImpl wordCounterServiceImpl;
    @Mock
    private Map<String, AtomicInteger> wordCountMap;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        wordCounterServiceImpl = new WordCounterServiceImpl(wordCountMap);
    }

    @Test
    public void test_processText() {

        String text = "Sample text for testing.";
        String[] words = text.split("\\s+");

        // Mock the behavior of the wordCountMap
        when(wordCountMap.computeIfAbsent(anyString(), any())).thenAnswer(i -> {
            String word = i.getArgument(0);
            AtomicInteger atomicInteger = new AtomicInteger(1);
            wordCountMap.put(word, atomicInteger);
            return atomicInteger;
        });

        // Act
        wordCounterServiceImpl.processText(text);

        // Assert
        for (String word : words) {
            verify(wordCountMap).computeIfAbsent(eq(word.toLowerCase()), any());
        }
    }


    @Test
    public void test_getWordCount() {

        Map<String, AtomicInteger> map = new HashMap<>();
        map.put("ABC", new AtomicInteger(1));
        map.put("XYZ", new AtomicInteger(2));

        // Mock the wordCountMap
        when(wordCountMap.entrySet()).thenReturn(map.entrySet());

        // Act
        Map<String, Integer> wordCounts = wordCounterServiceImpl.getWordCount();

        // Assert
        assertEquals(1, wordCounts.get("ABC"));
        assertEquals(2, wordCounts.get("XYZ"));
    }
}