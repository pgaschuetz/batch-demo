package com.example.batchdemo.speeches;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpeechReader extends AbstractItemStreamItemReader<Speech> {

    List<Speech> items;
    Iterator<Speech> it;


    @Override
    public void open(ExecutionContext executionContext) {
        // load some dummy data^
        this.items = IntStream.range(0,10)
                .mapToObj(id -> new Speech(id))
                .collect(Collectors.toList());
        this.it = items.iterator();
    }

    @Override
    public void close() {
        this.it = null;
        this.items = null;
    }

    @Override
    public Speech read() throws Exception {
        return it.hasNext() ? it.next() : null;
    }
}
