package projectShopMenu.Service;

import projectShopMenu.Model.*;
import java.util.*;

public class SellerService {
    private List<Product> products;
    private Map<Integer, Integer> productStock;
    private Map<String, List<String>> sellerProducts; // username -> product IDs
    private Map<String, Object> salesData;
    private int nextProductId;
    
    public SellerService() {
        this.products = new ArrayList<>();
        this.productStock = new HashMap<>();
        this.sellerProducts = new HashMap<>();
        this.salesData = new HashMap<>();
        this.nextProductId = 1;
        
        initializeData();
    }
    
    private void initializeData() {
        // Initialize with sample products for existing sellers
        addSampleProduct("toupik", "Laptop Gaming", "Electronics", 15000000, "High-end gaming laptop", "Indonesia", 5);
        addSampleProduct("toupik", "Mouse Gaming", "Electronics", 250000, "RGB gaming mouse", "Indonesia", 20);
        
        // Initialize sales data
        initializeSalesData();
    }
    
    private void addSampleProduct(String sellerUsername, String name, String category, double price, String description, String country, int stock) {
        Product product = new Product(nextProductId, name, category, price, description, country);
        products.add(product);
        productStock.put(nextProductId, stock);
        
        sellerProducts.computeIfAbsent(sellerUsername, k -> new ArrayList<>()).add(String.valueOf(nextProductId));
        nextProductId++;
    }
    
    private void initializeSalesData() {
        // Initialize with sample sales data
        salesData.put("toupik_totalSold", 25);
        salesData.put("toupik_totalRevenue", 2500000.0);
        salesData.put("toupik_bestSeller", "Mouse Gaming");
        
        List<String> recentSales = new ArrayList<>();
        recentSales.add("2024-06-19 10:30 - Mouse Gaming x2 - Rp 500,000");
        recentSales.add("2024-06-18 15:20 - Laptop Gaming x1 - Rp 15,000,000");
        salesData.put("toupik_recentSales", recentSales);
    }
    
    public List<Product> getSellerProducts(String sellerUsername) {
        List<Product> sellerProductList = new ArrayList<>();
        List<String> productIds = sellerProducts.get(sellerUsername);
        
        if (productIds != null) {
            for (String idStr : productIds) {
                int id = Integer.parseInt(idStr);
                Product product = products.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
                if (product != null) {
                    sellerProductList.add(product);
                }
            }
        }
        
        return sellerProductList;
    }
    
    public boolean addProduct(String sellerUsername, String name, String category, double price, String description, String country, int stock) {
        if (name == null || name.trim().isEmpty() || price <= 0 || stock < 0) {
            return false;
        }
        
        Product product = new Product(nextProductId, name, category, price, description, country);
        products.add(product);
        productStock.put(nextProductId, stock);
        
        sellerProducts.computeIfAbsent(sellerUsername, k -> new ArrayList<>()).add(String.valueOf(nextProductId));
        nextProductId++;
        
        return true;
    }
    
    public int getProductStock(int productId) {
        return productStock.getOrDefault(productId, 0);
    }
    
    public boolean updateProductStock(int productId, int newStock) {
        if (newStock < 0) {
            return false;
        }
        
        if (products.stream().anyMatch(p -> p.getId() == productId)) {
            productStock.put(productId, newStock);
            return true;
        }
        
        return false;
    }
    
    public boolean deleteProduct(int productId) {
        // Remove from products list
        boolean productRemoved = products.removeIf(p -> p.getId() == productId);
        
        if (productRemoved) {
            // Remove from stock
            productStock.remove(productId);
            
            // Remove from seller's product list
            for (List<String> productIds : sellerProducts.values()) {
                productIds.remove(String.valueOf(productId));
            }
            
            return true;
        }
        
        return false;
    }
    
    public Map<String, Object> getSalesReport(String sellerUsername) {
        Map<String, Object> report = new HashMap<>();
        
        // Get sales data for this seller
        Object totalSold = salesData.get(sellerUsername + "_totalSold");
        Object totalRevenue = salesData.get(sellerUsername + "_totalRevenue");
        Object bestSeller = salesData.get(sellerUsername + "_bestSeller");
        Object recentSales = salesData.get(sellerUsername + "_recentSales");
        
        report.put("totalSold", totalSold != null ? totalSold : 0);
        report.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        report.put("bestSeller", bestSeller != null ? bestSeller : "Belum ada data");
        report.put("recentSales", recentSales != null ? recentSales : new ArrayList<String>());
        
        return report;
    }
    
    // Method untuk mendapatkan semua produk (untuk AdminService)
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    // Method untuk update sales data (dipanggil saat ada pembelian)
    public void updateSalesData(String sellerUsername, String productName, int quantity, double amount) {
        String totalSoldKey = sellerUsername + "_totalSold";
        String totalRevenueKey = sellerUsername + "_totalRevenue";
        String recentSalesKey = sellerUsername + "_recentSales";
        
        // Update total sold
        int currentSold = (Integer) salesData.getOrDefault(totalSoldKey, 0);
        salesData.put(totalSoldKey, currentSold + quantity);
        
        // Update total revenue
        double currentRevenue = (Double) salesData.getOrDefault(totalRevenueKey, 0.0);
        salesData.put(totalRevenueKey, currentRevenue + amount);
        
        // Update recent sales
        @SuppressWarnings("unchecked")
        List<String> recentSales = (List<String>) salesData.getOrDefault(recentSalesKey, new ArrayList<String>());
        
        String saleRecord = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + 
                           " - " + productName + " x" + quantity + " - Rp " + String.format("%.0f", amount);
        recentSales.add(0, saleRecord); // Add to beginning
        
        // Keep only last 10 sales
        if (recentSales.size() > 10) {
            recentSales = recentSales.subList(0, 10);
        }
        
        salesData.put(recentSalesKey, recentSales);
    }
}