package util;

import app.AppConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ResultsWriter {
    private final String directoryPath;
    private final TxtWriter txtWriter;
    private final DotWriter dotWriter;
    private final GraphDotToImage graphDotToImage;
    private final List<List<Character>> dependencyList;
    private final List<List<Character>> independencyList;
    private final List<List<Character>> fnf;
    private final String dotGraph;

    public ResultsWriter(List<List<Character>> dependencyList, List<List<Character>> independencyList, List<List<Character>> fnf, String dotGraph, String fileName) {
        this.dependencyList = dependencyList;
        this.independencyList = independencyList;
        this.fnf = fnf;
        this.dotGraph = dotGraph;
        this.directoryPath = AppConfig.OUTPUT_FILE_PATH + "/" + fileName;
        this.txtWriter = new TxtWriter(directoryPath);
        this.dotWriter = new DotWriter(directoryPath);
        this.graphDotToImage = new GraphDotToImage(directoryPath);
    }

    public void saveResults() {
        try {
            Path directory = Path.of(directoryPath);
            if(Files.exists(directory)) {
                Files.walk(directory)
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> lines = List.of("D = " + dependencyList.toString(), "I = " + independencyList.toString(), "FNF([w]) = " + fnf.toString(), dotGraph);
        txtWriter.write(lines, "results");
        dotWriter.write(dotGraph, "graph");
        graphDotToImage.getPNG(dotGraph, "graph");
    }
}
