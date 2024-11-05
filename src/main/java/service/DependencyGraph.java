package service;

import model.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DependencyGraph {

    public Graph createDependencyGraph(String word, List<List<Character>> dependencyList) {
        Graph graph = new Graph();

        for (int i=0; i<word.length(); i++) {
            for (int j=i+1; j<word.length(); j++){
                if (dependencyList.contains(List.of(word.charAt(i), word.charAt(j)))) {
                    graph.addEdge(i, j);
                }
            }
        }

        DFS(graph);

        return graph;
    }

    private void DFS(Graph graph){
        Set<Integer> visited = new HashSet<>();
        List<Integer> edgesToRemove = new ArrayList<>();

        for (int u : graph.getAdjacencyList().keySet()) {
            visited.clear();
            edgesToRemove.clear();

            for (int v : graph.getAdjacencyList().get(u)) {
                if (visited.contains(v)) {
                    edgesToRemove.add(v);
                }
                DFSVisit(graph, v, visited);
            }

            for (int v : edgesToRemove) {
                graph.removeEdge(u, v);
            }
        }
    }

    private void DFSVisit(Graph graph, int u, Set<Integer> visited) {
        visited.add(u);

        for (int v : graph.getAdjacencyList().get(u)) {
            if (!visited.contains(v)) {
                DFSVisit(graph, v, visited);
            }
        }
    }
}
