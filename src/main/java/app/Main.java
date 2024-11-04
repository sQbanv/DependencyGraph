package app;

import model.Data;
import reader.TxtReader;
import service.Dependency;

import java.util.Set;

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
    }
}
