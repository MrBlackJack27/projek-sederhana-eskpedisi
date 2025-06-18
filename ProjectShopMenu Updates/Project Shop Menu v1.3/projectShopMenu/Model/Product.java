package projectShopMenu.Model;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private String description;
    private String country;
    
    public Product(int id, String name, String category, double price, String description, String country) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.country = country;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCountry() { return country; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setCountry(String country) { this.country = country; }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s | Rp %.2f | %s | %s", 
                           id, name, price, description, country);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return id == product.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}