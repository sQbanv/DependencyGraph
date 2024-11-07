package util;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class GraphDotToImage {
    private final String directoryPath;

    public GraphDotToImage(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void getPNG(String dotGraph, String fileName) {
        String filePath = Paths.get(directoryPath, fileName + ".png").toString();
        try {
            Graphviz.fromString(dotGraph).render(Format.PNG).toFile(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
