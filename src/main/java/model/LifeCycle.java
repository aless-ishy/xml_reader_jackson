package model;

import javax.annotation.Nonnull;

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

    public boolean isValid(){
        if(etl == null || !etl.isValid()) return false;
        if(train != null) {
            if (!train.isValid()) return false;
        }
        else if(test != null) return false;
        if(test != null && !test.isValid()) return false;
        return true;
    }
}
