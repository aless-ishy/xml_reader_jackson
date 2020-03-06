import java.io.File;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.ETL;
import model.LifeCycle;
import org.datavec.api.conf.Configuration;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class Application {
    public static void main(String[] args) {
        File xmlFile = new File("test.xml"), ti = new File("titanic/train.csv");
        LifeCycle lifeCycle;
        try{
            XmlMapper mapper = new XmlMapper();
            lifeCycle = mapper.readValue(xmlFile,LifeCycle.class);

            RecordReader reader = lifeCycle.getTrainReader();
            System.out.println(reader.next());
        }catch (Exception e) {e.printStackTrace();}

    }
}
