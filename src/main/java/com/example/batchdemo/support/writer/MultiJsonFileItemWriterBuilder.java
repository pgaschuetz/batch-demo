package com.example.batchdemo.support.writer;

import org.springframework.batch.item.json.JsonObjectMarshaller;

import java.nio.file.Path;
import java.util.function.Function;

public class MultiJsonFileItemWriterBuilder<T> {
    private JsonObjectMarshaller<T> jsonObjectMarshaller;
    private Function<T, Path> filepathProvider;

    public MultiJsonFileItemWriterBuilder<T> withJsonObjectMarshaller(JsonObjectMarshaller<T> jsonObjectMarshaller) {
        this.jsonObjectMarshaller = jsonObjectMarshaller;
        return this;
    }

    public MultiJsonFileItemWriterBuilder<T> withFilepathProvider(Function<T, Path> filepathProvider) {
        this.filepathProvider = filepathProvider;
        return this;
    }

    public MultiJsonFileItemWriterBuilder<T> withFilenameProvider(Path basePath, Function<T, String> filenameProvider) {
        this.filepathProvider = i -> basePath.resolve( filenameProvider.apply(i) );
        return this;
    }

    public MultiJsonFileItemWriter<T> build() {
        return new MultiJsonFileItemWriter<>(jsonObjectMarshaller, filepathProvider);
    }
}