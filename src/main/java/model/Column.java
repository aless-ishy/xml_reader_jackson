package model;

import java.util.ArrayList;
import java.util.List;

public class Column {
    public enum Type{string, integer, real, categorical};

    private String name;
    private Type type = Type.real;
    private boolean remove = false;

    private List<String> categories = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    @Override
    public String toString() {
        String text = "\n\nColumn\nName: " + name + (remove ? "\nRemove: True" : "\nType: " + type) ;
        for(String cat: categories)
            text += "\nCategorie: " + cat;
        return text;
    }
}
