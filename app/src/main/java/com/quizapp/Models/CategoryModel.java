package com.quizapp.Models;

public class CategoryModel {

    private String categoryName;
    private String categoryImage;
    private String key;

    // No-argument constructor
    public CategoryModel() {
        // Required for Firebase
    }

    // Constructor with category name
    public CategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    // Constructor with category name and category image
    public CategoryModel(String categoryName, String categoryImage) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    // Getter and Setter methods
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
