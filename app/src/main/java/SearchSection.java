package com.example.perfume;

import java.util.List;

public class SearchSection<T> {
    private String sectionTitle;
    private List<T> itemList;

    public SearchSection(String sectionTitle, List<T> itemList) {
        this.sectionTitle = sectionTitle;
        this.itemList = itemList;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public List<T> getItemList() {
        return itemList;
    }
}

