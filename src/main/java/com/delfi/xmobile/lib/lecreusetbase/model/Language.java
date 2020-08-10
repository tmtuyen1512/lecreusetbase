package com.delfi.xmobile.lib.lecreusetbase.model;

/**
 * Created by DANHPC on 6/1/2017.
 */

public class Language {

    private String code;
    private String name;
    private boolean selected;
    private int resource;

    public Language(String code, String name, boolean selected, int resource){
        this.code = code;
        this.name = name;
        this.selected = selected;
        this.resource = resource;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
