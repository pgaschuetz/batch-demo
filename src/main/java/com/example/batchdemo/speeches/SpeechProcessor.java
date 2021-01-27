package com.example.batchdemo.speeches;

import com.example.batchdemo.model.MagnoliaContent;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class SpeechProcessor implements ItemProcessor<Speech, MagnoliaContent> {

    @Override
    public MagnoliaContent process(Speech item) {
        MagnoliaContent content = new MagnoliaContent();
        // map Speech to Content, generate random id
        content.setId(String.format("%s__%s", item.id, UUID.randomUUID()));
        content.setOriginalId(String.valueOf(item.id));
        return content;
    }
}
