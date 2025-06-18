
package projectShopMenu.Model;

public class Courier {
    private String name;
    private double price;
    private String deliveryTime;
    
    public Courier(String name, double price, String deliveryTime) {
        this.name = name;
        this.price = price;
        this.deliveryTime = deliveryTime;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDeliveryTime() { return deliveryTime; }
    
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }
    
    @Override
    public String toString() {
        return String.format("%s - Rp %.2f (%s)", name, price, deliveryTime);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Courier courier = (Courier) obj;
        return name.equals(courier.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}