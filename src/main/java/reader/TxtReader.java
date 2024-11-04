package reader;

import model.Data;
import model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TxtReader {
    private final String filePath;

    public TxtReader(String filePath) {
        this.filePath = filePath;
    }

    public Data readData() {
        Data data = new Data();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            List<Transaction> transactions = new ArrayList<>();

            while (line != null) {
                switch (line.charAt(0)) {
                    case '(' -> {
                        String[] parts = line.split(":=");

                        char transactionId = parts[0].charAt(1);
                        char transactionLeftAction = parts[0].split(" ")[1].strip().charAt(0);
                        Set<Character> transactionRightActions = parts[1].chars()
                                        .mapToObj(ch -> (char) ch)
                                        .filter(ch -> ch >= 'a' && ch <= 'z')
                                        .collect(Collectors.toSet());

                        transactions.add(new Transaction(transactionId, transactionLeftAction, transactionRightActions));
                    }
                    case 'A' -> {
                        data.setAlphabet(line.split("=")[1].strip().replaceAll("[{}, ]", "").chars()
                                .mapToObj(ch -> (char) ch)
                                .collect(Collectors.toSet()));
                    }
                    case 'w' -> {
                        data.setWord(line.split("=")[1].strip());
                    }
                }

                line = br.readLine();
            }

            data.setTransactions(transactions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}
