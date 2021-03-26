package com.satvick.model;

public class CategoryItemResponse {

    String TitleName;
    String ItemName1;
    String ItemName2;
    String ItemName3;
    String ItemName4;
    String ItemName5;
    String ItemName6;

    public CategoryItemResponse(String titleName, String itemName1, String itemName2, String itemName3, String itemName4, String itemName5, String itemName6) {
        TitleName = titleName;
        ItemName1 = itemName1;
        ItemName2 = itemName2;
        ItemName3 = itemName3;
        ItemName4 = itemName4;
        ItemName5 = itemName5;
        ItemName6 = itemName6;
    }

    public String getTitleName() {
        return TitleName;
    }

    public void setTitleName(String titleName) {
        TitleName = titleName;
    }

    public String getItemName1() {
        return ItemName1;
    }

    public void setItemName1(String itemName1) {
        ItemName1 = itemName1;
    }

    public String getItemName2() {
        return ItemName2;
    }

    public void setItemName2(String itemName2) {
        ItemName2 = itemName2;
    }

    public String getItemName3() {
        return ItemName3;
    }

    public void setItemName3(String itemName3) {
        ItemName3 = itemName3;
    }

    public String getItemName4() {
        return ItemName4;
    }

    public void setItemName4(String itemName4) {
        ItemName4 = itemName4;
    }

    public String getItemName5() {
        return ItemName5;
    }

    public void setItemName5(String itemName5) {
        ItemName5 = itemName5;
    }

    public String getItemName6() {
        return ItemName6;
    }

    public void setItemName6(String itemName6) {
        ItemName6 = itemName6;
    }
}
