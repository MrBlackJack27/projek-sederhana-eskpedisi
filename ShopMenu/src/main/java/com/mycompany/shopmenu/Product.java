/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shopmenu;

/**
 *
 * @author Jose
 */
class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private String description;
    
    public Product(int id, String name, String category, double price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s | Rp %.2f | %s", 
                           id, name, price, description);
    }
}
