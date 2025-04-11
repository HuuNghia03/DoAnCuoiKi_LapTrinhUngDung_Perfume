package com.example.perfume;

import java.util.List;

public class SearchSection {
    private String sectionTitle;
    private List<com.example.perfume.PerfumeEntity> itemList;

    public SearchSection(String sectionTitle, List<com.example.perfume.PerfumeEntity> itemList) {
        this.sectionTitle = sectionTitle;
        this.itemList = itemList;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public List<com.example.perfume.PerfumeEntity> getItemList() {
        return itemList;
    }
}
