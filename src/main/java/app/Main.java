package app;

import util.TxtFileProcessor;

public class Main {
    public static void main(String[] args) {
        TxtFileProcessor txtFileProcessor = new TxtFileProcessor(AppConfig.INPUT_FILE_PATH);

        txtFileProcessor.processFiles();
    }
}
