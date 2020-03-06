package model;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;

import java.io.File;
import java.io.IOException;

public class LifeCycle {
    private ETL etl;
    private Train train;
    private Test test;

    public ETL getEtl() {
        return etl;
    }

    public void setEtl(ETL etl) {
        this.etl = etl;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public RecordReader getTrainReader() throws InterruptedException, IOException {

        RecordReader reader = new CSVRecordReader();
        reader.initialize(new FileSplit(new File(train.getPath())));

        reader = etl.normalize(reader);
        reader.reset();
        System.out.println(reader.hasNext());


        return reader;


    }

    public boolean isValid(){
        if(etl == null || !etl.isValid()) return false;
        if(train != null) {
            if (!train.isValid()) return false;
        }
        else if(test != null) return false;
        if(test != null && !test.isValid()) return false;
        return true;
    }

    @Override
    public String toString() {
        return etl.toString();
    }
}
