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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public Double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public RecordReader train(){
        CSVRecordReader reader = new CSVRecordReader();
        return reader;
    }
}
