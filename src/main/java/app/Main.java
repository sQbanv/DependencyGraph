package app;

import model.Data;
import model.Graph;
import service.DependencyGraph;
import service.FNF;
import util.ResultsWriter;
import util.TxtReader;
import service.Dependency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String inputFileDirectory = AppConfig.INPUT_FILE_PATH;

        try {
            System.out.println(Files.list(Paths.get(inputFileDirectory)));
            Files.list(Paths.get(inputFileDirectory))
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(Main::processFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processFile(Path filePath) {
        TxtReader txtReader = new TxtReader(filePath.toString());
        Data data = txtReader.readData();

        Dependency dependency = new Dependency(data.getTransactions());
        DependencyGraph dependencyGraph = new DependencyGraph();
        Graph graph = dependencyGraph.createDependencyGraph(data.getWord(), dependency.getDependencyList());
        List<List<Character>> fnf = FNF.getFNF(graph, data.getWord());

        System.out.println("D = " + dependency.getDependencyList());
        System.out.println("I = " + dependency.getIndependencyList());
        System.out.println("FNF([w]) = " + fnf);
        System.out.println(graph.toDot(data.getWord()));

        String outputFileName = filePath.getFileName().toString().replace(".txt", "_result");
        ResultsWriter resultsWriter = new ResultsWriter(dependency.getDependencyList(), dependency.getIndependencyList(), fnf, graph.toDot(data.getWord()),outputFileName);
        resultsWriter.saveResults();
    }
}
