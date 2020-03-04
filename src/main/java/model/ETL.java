package model;

import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.condition.ConditionOp;
import org.datavec.api.transform.condition.column.CategoricalColumnCondition;
import org.datavec.api.transform.schema.Schema;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;

import java.util.HashSet;
import java.util.List;

public class ETL {
    private String path;
    private List<Column> columns;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path.trim();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public boolean isValid(){
        if(path.isBlank()) return false;
        return true;
     }

     private Schema createSchema(){
        Schema.Builder builder = new Schema.Builder();

        for(Column column: columns){
            String name = column.getName();

            switch (column.getType()){
                case string:
                    builder.addColumnString(name);
                    break;
                case integer:
                    builder.addColumnInteger(name);
                    break;
                case real:
                    builder.addColumnDouble(name);
                    break;
                case categorical:
                    builder.addColumnCategorical(name,column.getCategories());
                    break;
            }
        }
        return builder.build();
     }

     public TransformProcess transform(){
        TransformProcess.Builder transformBuilder = new TransformProcess.Builder(createSchema());
        for(Column column : columns){
            if(column.isRemove())
                transformBuilder.removeColumns(column.getName());
            else{
                if(column.getType() == Column.Type.categorical)
                    transformBuilder.filter(new CategoricalColumnCondition(column.getName(), ConditionOp.NotInSet, new HashSet<>(column.getCategories())));
            }

        }
        return transformBuilder.build();
     }


    @Override
    public String toString() {
        String text = "\nETL\nPath: " + path;
        for(Column column : columns)
            text += column;
        return text;
    }
}
