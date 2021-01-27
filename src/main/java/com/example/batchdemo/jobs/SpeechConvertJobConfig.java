package com.example.batchdemo.jobs;

import com.example.batchdemo.model.MagnoliaContent;
import com.example.batchdemo.speeches.Speech;
import com.example.batchdemo.speeches.SpeechProcessor;
import com.example.batchdemo.speeches.SpeechReader;
import com.example.batchdemo.support.FilenameSupport;
import com.example.batchdemo.support.writer.MultiJsonFileItemWriter;
import com.example.batchdemo.support.writer.MultiJsonFileItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;
import java.util.function.Function;

@Configuration
public class SpeechConvertJobConfig {

    @Value("${demo.export-base-path}")
    Path exportBasePath;

    @Bean
    public Job speechConvertJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {

        Step step1 = stepBuilders.get("speechStep1")
                .<Speech, MagnoliaContent>chunk(1)
                .reader(new SpeechReader())
                .processor(new SpeechProcessor())
                .writer(mgnlJsonWriter())
                .build();

        return jobBuilders.get("convertSpeech")
                .preventRestart()
                .start(step1)
                //add additional steps to the same
                //.next(step2)
                .build();
    }

    /**
     * ein writer, der sowohl jedes Item in eine eigene JSON schreibt und eine Datei mit allen IDs, und URLs
     */
    @Bean
    public ItemWriter<MagnoliaContent> mgnlJsonWriter() {

        Path exportPath = FilenameSupport.validateAndCreateExportPath(exportBasePath, true, "speeches");

        // a writer which writes each item to speeches/{originalId}.json
        MultiJsonFileItemWriter<MagnoliaContent> jsonFileWriter = new MultiJsonFileItemWriterBuilder<MagnoliaContent>()
                .withFilenameProvider(exportPath, i -> String.format("%s.json", i.getOriginalId()))
                .build();

        final Function<MagnoliaContent, Path> filenameProvider = jsonFileWriter.getFilenameProvider();
        final Path indexFilePath = FilenameSupport.validateAndCreateIndexFile(exportBasePath, "speeches.index.txt");

        // a writer which writes each item to a combined index file - speeches.index.txt - containing Id, filename and URL
        ItemWriter<MagnoliaContent> speechesIndexWriter = new FlatFileItemWriterBuilder<MagnoliaContent>()
                .delimited()
                .delimiter("\t")
                .fieldExtractor(i -> new Object[] {i.getId(), filenameProvider.apply(i).toString(), "www.foo.com/de/iabout/somewhere/" + i.getId()})
                .saveState(false)
                .shouldDeleteIfExists(true)
                .name("speechesIndexWriter")
                .resource(new FileSystemResource(indexFilePath))
                .build();

        // combine them
        return new CompositeItemWriterBuilder<MagnoliaContent>()
                .delegates(speechesIndexWriter, jsonFileWriter)
                .build();
    }
}
