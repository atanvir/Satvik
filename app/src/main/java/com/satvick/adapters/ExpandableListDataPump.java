package com.satvick.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();


        List<String> Men = new ArrayList<String>();
        Men.add("Brazil");
        Men.add("Spain");
        Men.add("Germany");
        Men.add("Netherlands");
        Men.add("Italy");

        List<String> Women = new ArrayList<String>();
        Women.add("United States");
        Women.add("Spain");
        Women.add("Argentina");
        Women.add("France");
        Women.add("Russia");

        List<String> Kids = new ArrayList<String>();
        Kids.add("Explore Kids Store");
        Kids.add("Brands");
        Kids.add("Boys Clothing");
        Kids.add("Girls Clothing");
        Kids.add("Boys Footwear");
        Kids.add("Girls Footwear");
        Kids.add("Kids Accessories");
        Kids.add("Toys");


        List<String> Home = new ArrayList<String>();
        Home.add("United States");
        Home.add("Spain");
        Home.add("Argentina");
        Home.add("France");
        Home.add("Russia");


        List<String> Gadgets = new ArrayList<String>();
        Gadgets.add("United States");
        Gadgets.add("Spain");
        Gadgets.add("Argentina");
        Gadgets.add("France");
        Gadgets.add("Russia");

        expandableListDetail.put("Men", Men);
        expandableListDetail.put("Women", Women);
        expandableListDetail.put("KIDS", Kids);
        expandableListDetail.put("Home", Home);
        expandableListDetail.put("Gadgets", Gadgets);

        return expandableListDetail;
    }
}
