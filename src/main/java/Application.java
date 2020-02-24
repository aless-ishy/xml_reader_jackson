import java.io.File;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.ETL;

public class Application {
    public static void main(String[] args) {
        File xmlFile = new File("test.xml");
        try{
            XmlMapper mapper = new XmlMapper();
            ETL etl = mapper.readValue(xmlFile,ETL.class);
            System.out.println(etl);
        }catch (Exception e) {e.printStackTrace();}
    }
}
