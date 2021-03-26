package com.satvick.model;

import androidx.annotation.NonNull;

public class ColorCheckedListModel {

    private String colorName;
    private String hexCode;
    boolean isColorChecked;

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public ColorCheckedListModel(String colorName,String hexCode) {
        this.colorName = colorName;
        this.hexCode=hexCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public boolean isColorChecked() {
        return isColorChecked;
    }

    public void setColorChecked(boolean colorChecked) {
        isColorChecked = colorChecked;
    }


    @NonNull
    @Override
    public String toString() {
        return "colorName:"+colorName+"\n"+
                "hexcode:"+hexCode+"\n"+isColorChecked;
    }
}
