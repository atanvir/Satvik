package com.satvick.model;

public class SizeCheckedListModel {
    private String sizeName;
    boolean isSizeChecked;

    public SizeCheckedListModel(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public boolean isSizeChecked() {
        return isSizeChecked;
    }

    public void setSizeChecked(boolean sizeChecked) {
        isSizeChecked = sizeChecked;
    }
}
