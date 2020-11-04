package com.swing.editor;

/**
 * Created with IntelliJ IDEA.
 * User: Sknictik
 * Date: 06.10.13
 * Time: 22:30
 * Объекты класса используется в комбобокс диалога редактирования.
 */
public class ComboBoxObj {
    String name;

    public ComboBoxObj(String newName) {
        name = newName;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ComboBoxObj))
            return false;
        ComboBoxObj oth = (ComboBoxObj) other;

        return this.name.equals(oth.name);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
