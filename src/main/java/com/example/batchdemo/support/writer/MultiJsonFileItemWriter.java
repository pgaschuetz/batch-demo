package com.example.batchdemo.support.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonObjectMarshaller;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.function.Function;

public class MultiJsonFileItemWriter<T> implements ItemWriter<T> {

    private final JsonObjectMarshaller<T> jsonObjectMarshaller;
    private final Function<T, Path> filenameProvider;

    public MultiJsonFileItemWriter(JsonObjectMarshaller<T> jsonObjectMarshaller, Function<T, Path> filenameProvider) {
        if(jsonObjectMarshaller == null) {
            jsonObjectMarshaller = new JacksonJsonObjectMarshaller<>();
        }
        this.jsonObjectMarshaller = jsonObjectMarshaller;
        this.filenameProvider = filenameProvider;
    }

    public Function<T, Path> getFilenameProvider() {
        return filenameProvider;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {

        for(T item : items) {
            final Path fileName = filenameProvider.apply(item);
            final String content = jsonObjectMarshaller.marshal(item);
            Files.write(fileName, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        }
    }
}
