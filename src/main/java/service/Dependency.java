package service;

import model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Dependency {
    private final List<List<Character>> dependencyList = new ArrayList<>();
    private final List<List<Character>> independencyList = new ArrayList<>();

    public Dependency(List<Transaction> transactions) {
        calculateDependencies(transactions);
    }

    private void calculateDependencies(List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            transactions.forEach(tr -> {
                List<Character> item = List.of(transaction.getId(), tr.getId());
                if(transaction.getId() == tr.getId()){
                    dependencyList.add(item);
                } else if (tr.getRightActions().contains(transaction.getLeftAction()) || transaction.getRightActions().contains(tr.getLeftAction())) {
                    dependencyList.add(item);
                } else {
                    independencyList.add(item);
                }
            });
        });
    }

    public List<List<Character>> getDependencyList() {
        return dependencyList;
    }

    public List<List<Character>> getIndependencyList() {
        return independencyList;
    }
}
