package model;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;

public class Train {
    private String path;
    private int epochs = 1;
    private Double learningRate = 0.01D;
    private int batchSize = 36;

    public boolean isValid() {
        return !path.isBlank();
    }

    public RecordReader train(){
        CSVRecordReader reader = new CSVRecordReader();
        return reader;
    }
}
