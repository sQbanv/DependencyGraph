package service;

import model.Graph;

import java.util.*;

public class FNF {

    public static List<List<Character>> getFNF(Graph graph, String labels) {
        Map<Integer, Integer> group = new HashMap<>();
        Graph invertedGraph = graph.getInvertedGraph();

        for (int source : invertedGraph.getAdjacencyList().keySet()) {
            if (invertedGraph.getAdjacencyList().get(source).isEmpty()) {
                group.put(source, 0);
            } else {
                int max = 0;
                for (int target : invertedGraph.getAdjacencyList().get(source)) {
                    max = Math.max(max, group.get(target) + 1);
                }
                group.put(source, max);
            }
        }

        int groups = Collections.max(group.values());

        List<List<Character>> fnf = new ArrayList<>();

        for (int i=0; i<groups+1; i++){
            fnf.add(i, new ArrayList<>());
        }

        for (int v : group.keySet()){
            fnf.get(group.get(v)).add(labels.charAt(v));
        }

        return fnf;
    }
}
