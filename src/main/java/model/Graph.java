package model;

import java.util.*;

public class Graph {
    private final Map<Integer, List<Integer>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(int source, int target) {
        if (!adjacencyList.containsKey(source)) {
            adjacencyList.put(source, new ArrayList<>());
            adjacencyList.get(source).add(target);
        } else {
            adjacencyList.get(source).add(target);
        }

        if (!adjacencyList.containsKey(target)) {
            adjacencyList.put(target, new ArrayList<>());
        }
    }

    public void removeEdge(int source, int target) {
        if (adjacencyList.containsKey(source) && adjacencyList.get(source).contains(target)) {
            adjacencyList.get(source).remove(Integer.valueOf(target));
        }
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public String toDot(String labels) {
        StringBuilder dot = new StringBuilder("digraph g {\n");

        for (int source : adjacencyList.keySet()) {
            for (int target : adjacencyList.get(source)) {
                dot.append("%d -> %d\n".formatted(source, target));
            }
        }

        for (int i=0; i<labels.length(); i++) {
            dot.append("%d[label=%c]\n".formatted(i, labels.charAt(i)));
        }

        dot.append("}");

        return dot.toString();
    }

    @Override
    public String toString() {
        return adjacencyList.toString();
    }
}
