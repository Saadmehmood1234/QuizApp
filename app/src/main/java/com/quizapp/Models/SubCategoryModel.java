package com.quizapp.Models;

public class SubCategoryModel {

    private String categoryName;
    private String key;

    // No-argument constructor
    public SubCategoryModel() {
        // Required for Firebase
    }

    // Constructor with category name
    public SubCategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    // Getter and Setter methods
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
