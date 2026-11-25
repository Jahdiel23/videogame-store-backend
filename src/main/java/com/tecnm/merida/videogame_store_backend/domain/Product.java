package com.tecnm.merida.videogame_store_backend.domain;

public class Product {
    private Integer productId;
    private String name;
    private Integer categoryId;
    private double price;
    private int stock;
    private boolean active;
    private GameConsole category;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public GameConsole getCategory() {
        return category;
    }

    public void setCategory(GameConsole category) {
        this.category = category;
    }
}