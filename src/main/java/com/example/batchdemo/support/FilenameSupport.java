package com.example.batchdemo.support;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilenameSupport {

    public static Path validateAndCreateExportPath(Path basePath, boolean createSubdirectories, String... subdirectories) {
        if(! basePath.toFile().isDirectory()) {
            throw new IllegalArgumentException("BasePath " + basePath.toFile().getAbsolutePath() + " does not exist");
        }

        Path path = basePath;
        for(String subdir : subdirectories) {
            path = path.resolve(subdir);
            path.toFile().mkdirs();
        }

        return path;
    }

    public static Path validateAndCreateIndexFile(Path basePath, String filename) {
        Path file = basePath.resolve(filename);
        return file;
    }
}
