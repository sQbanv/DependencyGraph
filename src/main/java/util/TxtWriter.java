package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class TxtWriter {
    private final String directoryPath;

    public TxtWriter(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void write(List<String> lines, String fileName) {
        String filePath = Paths.get(directoryPath, fileName + ".txt").toString();

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
