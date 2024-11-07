package util;

import model.Data;
import model.Graph;
import service.Dependency;
import service.DependencyGraph;
import service.FNF;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TxtFileProcessor {
    private final String directory;

    public TxtFileProcessor(String directory) {
        this.directory = directory;
    }

    public void processFiles() {
        System.out.println("Processing directory: " + directory);
        try {
            Files.list(Paths.get(directory))
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(this::processFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processFile(Path filePath) {
        TxtReader txtReader = new TxtReader(filePath.toString());
        Data data = txtReader.readData();

        Dependency dependency = new Dependency(data.getTransactions());
        DependencyGraph dependencyGraph = new DependencyGraph();
        Graph graph = dependencyGraph.createDependencyGraph(data.getWord(), dependency.getDependencyList());
        List<List<Character>> fnf = FNF.getFNF(graph, data.getWord());

        System.out.println("Results for: " + filePath.getFileName().toString());
        System.out.println("D = " + dependency.getDependencyList());
        System.out.println("I = " + dependency.getIndependencyList());
        System.out.println("FNF([w]) = " + fnf);
        System.out.println(graph.toDot(data.getWord()));

        String outputFileName = filePath.getFileName().toString().replace(".txt", "_result");
        ResultsWriter resultsWriter = new ResultsWriter(dependency.getDependencyList(), dependency.getIndependencyList(), fnf, graph.toDot(data.getWord()),outputFileName);
        resultsWriter.saveResults();
    }
}
