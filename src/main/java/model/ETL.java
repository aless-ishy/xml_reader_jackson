package model;

import java.util.Arrays;

public class ETL {
    private String path;
    private Column[] columns;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Column[] getColumns() {
        return columns;
    }

    public void setColumns(Column[] columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        String text = "\nETL\nPath: " + path;
        for(Column column : columns)
            text += column;
        return text;
    }
}
