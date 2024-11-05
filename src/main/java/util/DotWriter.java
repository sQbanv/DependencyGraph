package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class DotWriter {
    private final String directoryPath;

    public DotWriter(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void write(String dotGraph, String fileName) {
        String filePath = Paths.get(directoryPath, fileName + ".dot").toString();

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(dotGraph);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
