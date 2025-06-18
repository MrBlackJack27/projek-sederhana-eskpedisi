package projectShopMenu.Model;


public class PaymentMethod {
    private String name;
    private String description;
    private double fee;
    private double feePercentage;
    
    public PaymentMethod(String name, String description, double fee, double feePercentage) {
        this.name = name;
        this.description = description;
        this.fee = fee;
        this.feePercentage = feePercentage;
    }
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getFee() { return fee; }
    public double getFeePercentage() { return feePercentage; }
    
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setFee(double fee) { this.fee = fee; }
    public void setFeePercentage(double feePercentage) { this.feePercentage = feePercentage; }
    
    public double calculateFee(double amount) {
        return fee + (amount * feePercentage / 100);
    }
    
    @Override
    public String toString() {
        if (feePercentage > 0) {
            return String.format("%s - %s (Biaya: Rp %.0f + %.1f%%)", name, description, fee, feePercentage);
        } else {
            return String.format("%s - %s (Biaya: Rp %.0f)", name, description, fee);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PaymentMethod payment = (PaymentMethod) obj;
        return name.equals(payment.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}