package app;

import model.Data;
import reader.TxtReader;

public class Main {
    public static void main(String[] args) {
        TxtReader txtReader = new TxtReader("C:\\Users\\Dariusz\\Desktop\\DependencyGraph\\src\\main\\resources\\inputFiles\\case1.txt");
        Data data = txtReader.readData();
    }
}
