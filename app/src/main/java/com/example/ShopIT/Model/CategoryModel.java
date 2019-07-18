package com.example.ShopIT.Model;

public class CategoryModel<string> {

    private string categoryIconLink;
    private string categoryName;

    public CategoryModel(string categoryIcon, string categoryName) {
        this.categoryIconLink = categoryIcon;
        this.categoryName = categoryName;
    }

    public string getCategoryIconLink() {
        return categoryIconLink;
    }

    public void setCategoryIconLink(string categoryIcon) {
        this.categoryIconLink = categoryIcon;
    }

    public string getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(string categoryName) {
        this.categoryName = categoryName;
    }
}
