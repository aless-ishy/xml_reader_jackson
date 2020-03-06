package model;

import exception.ColumnException;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.transform.TransformProcessRecordReader;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.condition.ConditionOp;
import org.datavec.api.transform.condition.column.CategoricalColumnCondition;
import org.datavec.api.transform.condition.column.InvalidValueColumnCondition;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.transform.normalize.Normalize;
import org.datavec.local.transforms.AnalyzeLocal;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ETL {
    private String path;
    private List<Column> columns;
    private List<String> allColumnsNames;

    public String getPath() {
        return path;
    }

    public void setPath(String path) throws IOException, ColumnException {
        this.path = path.trim();

        defineColumnsNamesInCsv();

        if (columns != null)
            validateColumns();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) throws ColumnException {
        this.columns = columns;

        if (path != null)
            validateColumns();
    }

    public List<String> getAllColumnsNames() {
        return allColumnsNames;
    }

    private void setAllColumnsNames(List<String> allColumnsNames) {
        this.allColumnsNames = allColumnsNames;
    }

    public boolean isValid() {
        if (path.isBlank()) return false;
        return true;
    }

    private void defineColumnsNamesInCsv() throws IOException {
        File file = new File(this.path);
        Scanner fileReader = new Scanner(file);
        String[] names = fileReader.nextLine().trim().split(",");
        setAllColumnsNames(Arrays.asList(names));
    }

    private void validateColumns() throws ColumnException {
        //Verifies if all defined columns in the xml file exists in the csv file
        for (Column column : columns)
            if (!allColumnsNames.contains(column.getName()))
                throw new ColumnException("Column " + column.getName() + " was not found in " + path);

        //Mark for removal all non defined columns in the xml file that exists in the csv file and sort the list;
        List<Column> finalColumns = new ArrayList<Column>();
        for (String name : allColumnsNames) {
            boolean notFound = true;
            Column sortColumn = null;
            for (Column column : columns)
                if (name.equals(column.getName())) {
                    notFound = false;
                    sortColumn = column;
                }
            if (notFound)
                finalColumns.add(new Column(name, true));
            else
                finalColumns.add(sortColumn);
        }

        this.columns = finalColumns;
    }

    private Schema createSchema() {
        Schema.Builder builder = new Schema.Builder();

        for (Column column : columns) {
            String name = column.getName();

            switch (column.getType()) {
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
                    builder.addColumnCategorical(name, column.getCategories());
                    break;
            }
        }
        return builder.build();
    }

    public TransformProcess.Builder cleanTransformation() {
        Schema schema = createSchema();
        TransformProcess.Builder transformBuilder = new TransformProcess.Builder(schema);

        for (Column column : columns) {
            String name = column.getName();
            switch (column.getType()) {
                case categorical:
                    transformBuilder
                            .filter(new CategoricalColumnCondition(name, ConditionOp.NotInSet, new HashSet<>(column.getCategories())));
                    break;
                case real:
                case string:
                    transformBuilder
                            .filter(new InvalidValueColumnCondition(name));
                    break;
                case integer:
                    transformBuilder
                            .filter(new InvalidValueColumnCondition(name))
                            .convertToDouble(name);
                    break;
                default:
            }
        }
        return transformBuilder;

    }

    public TransformProcess transform() {
        Schema schema = createSchema();
        TransformProcess.Builder transformBuilder = new TransformProcess.Builder(schema);

        for (Column column : columns) {
            if (column.isRemove())
                transformBuilder.removeColumns(column.getName());
            else {
                String name = column.getName();
                switch (column.getType()) {
                    case categorical:
                        transformBuilder
                                .filter(new CategoricalColumnCondition(name, ConditionOp.NotInSet, new HashSet<>(column.getCategories())))
                                .categoricalToOneHot(name);
                        break;
                    case integer:
                        transformBuilder
                                .filter(new InvalidValueColumnCondition(name))
                                .convertToDouble(name);
                        break;
                    case real:
                        transformBuilder
                                .filter(new InvalidValueColumnCondition(name));
                    default:
                }

            }

        }
        return transformBuilder.build();
    }

    public DataAnalysis analysis(RecordReader reader) {
        Schema schema = createSchema();

        RecordReader cleanReader = new TransformProcessRecordReader(reader, cleanTransformation().build());

        return AnalyzeLocal.analyze(schema, cleanReader);
    }

    public RecordReader normalize(RecordReader reader) {
        Schema schema = createSchema();

        DataAnalysis analysis = analysis(reader);

        TransformProcess.Builder normalizeBuilder = cleanTransformation();
        for (Column column : columns) {
            String name = column.getName();
            if (!column.isRemove())
                switch (column.getType()) {
                    case real:
                    case integer:
                        normalizeBuilder
                                .normalize(name, Normalize.MinMax, analysis);
                        break;
                    default:
                }
            else
                normalizeBuilder.removeColumns(name);
        }

        return new TransformProcessRecordReader(reader, normalizeBuilder.build());
    }


    @Override
    public String toString() {
        String text = "\nETL\nPath: " + path;
        for (Column column : columns)
            text += column;
        return text;
    }
}
