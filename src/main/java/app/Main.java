package app;

import model.Data;
import model.Graph;
import service.DependencyGraph;
import util.TxtReader;
import service.Dependency;

public class Main {
    public static void main(String[] args) {
        TxtReader txtReader = new TxtReader("src/main/resources/inputFiles/test1.txt");
        Data data = txtReader.readData();

        System.out.println(data.getTransactions());
        System.out.println(data.getAlphabet());
        System.out.println(data.getWord());
        Dependency dependency = new Dependency(data.getTransactions());
        System.out.println(dependency.getDependencyList());
        System.out.println(dependency.getIndependencyList());

        DependencyGraph dependencyGraph = new DependencyGraph();

        Graph graph = dependencyGraph.createDependencyGraph(data.getWord(), dependency.getDependencyList());
        System.out.println(graph.toDot(data.getWord()));
    }
}
