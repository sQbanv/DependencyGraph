package app;

import model.Data;
import model.Graph;
import service.DependencyGraph;
import service.FNF;
import util.TxtReader;
import service.Dependency;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TxtReader txtReader = new TxtReader("src/main/resources/inputFiles/test3.txt");
        Data data = txtReader.readData();

        Dependency dependency = new Dependency(data.getTransactions());

        DependencyGraph dependencyGraph = new DependencyGraph();

        Graph graph = dependencyGraph.createDependencyGraph(data.getWord(), dependency.getDependencyList());

        List<List<Character>> fnf = FNF.getFNF(graph, data.getWord());

        System.out.println("D = " + dependency.getDependencyList());
        System.out.println("I = " + dependency.getIndependencyList());
        System.out.println("FNF([w]) = " + fnf);
        System.out.println(graph.toDot(data.getWord()));
    }
}
