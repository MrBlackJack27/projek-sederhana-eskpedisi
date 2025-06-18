package projectShopMenu.Service;

import projectShopMenu.Model.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class AdminService {
    private List<String> transactions;
    private Map<String, LoginUser> userData;
    private List<Product> products;
    private Map<String, Boolean> suspendedUsers;
    private ShoppingService shoppingService;
    private LoginService loginService;
    
    public AdminService() {
        this.transactions = new ArrayList<>();
        this.userData = new HashMap<>();
        this.products = new ArrayList<>();
        this.suspendedUsers = new HashMap<>();
        this.shoppingService = new ShoppingService();
        this.loginService = new LoginService();
        
        initializeData();
    }
    
    private void initializeData() {
        // Initialize with sample data
        addSampleTransactions();
        loadUsersFromLoginService();
        loadProductsFromShoppingService();
    }
    
    private void addSampleTransactions() {
        transactions.add("2024-06-19 10:30 - Order #ORD001 - faris - Rp 150,000");
        transactions.add("2024-06-19 11:15 - Order #ORD002 - customer1 - Rp 75,000");
        transactions.add("2024-06-19 14:20 - Order #ORD003 - faris - Rp 200,000");
    }
    
    private void loadUsersFromLoginService() {
        // Sample users - in real implementation, this would come from LoginService
        userData.put("admin", new LoginUser("admin", "123", "Indonesia", "admin"));
        userData.put("faris", new LoginUser("faris", "123", "Amerika", "customer"));
        userData.put("toupik", new LoginUser("toupik", "123", "Indonesia", "seller"));
        userData.put("customer1", new LoginUser("customer1", "123", "Indonesia", "customer"));
    }
    
    private void loadProductsFromShoppingService() {
    // Load products from seller service instead of shopping service
    SellerService sellerService = new SellerService();
    products = sellerService.getAllProducts();
}
    // Transaction Management
    public List<String> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
    public void addTransaction(String transaction) {
        transactions.add(transaction);
    }
    
    public Map<String, Integer> getDailyTransactionStats() {
        Map<String, Integer> stats = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        int todayCount = 0;
        for (String transaction : transactions) {
            if (transaction.startsWith(today)) {
                todayCount++;
            }
        }
        
        stats.put("Hari ini", todayCount);
        stats.put("Total", transactions.size());
        
        return stats;
    }
    
    // User Management
    public List<LoginUser> getAllUsers() {
        return new ArrayList<>(userData.values());
    }
    
    public LoginUser getUserByUsername(String username) {
        return userData.get(username);
    }
    
    public boolean changeUserRole(String username, String newRole) {
        LoginUser user = userData.get(username);
        if (user != null) {
            user.setRole(newRole);
            return true;
        }
        return false;
    }
    
    public boolean suspendUser(String username) {
        if (userData.containsKey(username)) {
            suspendedUsers.put(username, true);
            return true;
        }
        return false;
    }
    
    public boolean activateUser(String username) {
        if (userData.containsKey(username)) {
            suspendedUsers.put(username, false);
            return true;
        }
        return false;
    }
    
    public boolean isUserSuspended(String username) {
        return suspendedUsers.getOrDefault(username, false);
    }
    
    public boolean deleteUser(String username) {
        if (userData.containsKey(username)) {
            userData.remove(username);
            suspendedUsers.remove(username);
            return true;
        }
        return false;
    }
    
    public int getSellerProductCount(String sellerUsername) {
        return (int) products.stream()
                .filter(p -> p.getCountry().equals(userData.get(sellerUsername).getCountry()))
                .count();
    }
    
    // Product Management
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    public Product getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        
        for (Product product : products) {
            if (String.valueOf(product.getId()).contains(keyword) ||
                product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(product);
            }
        }
        
        return results;
    }
    
    public boolean updateProduct(int id, String name, String category, double price, String description) {
        Product product = getProductById(id);
        if (product != null) {
            if (!name.isEmpty()) product.setName(name);
            if (!category.isEmpty()) product.setCategory(category);
            if (price > 0) product.setPrice(price);
            if (!description.isEmpty()) product.setDescription(description);
            return true;
        }
        return false;
    }
    
    public boolean deleteProduct(int id) {
        return products.removeIf(p -> p.getId() == id);
    }
    
    public Map<String, Object> getProductStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalProducts", products.size());
        
        Set<String> categories = new HashSet<>();
        Map<String, Integer> categoryBreakdown = new HashMap<>();
        Product mostExpensive = null;
        Product cheapest = null;
        
        for (Product product : products) {
            categories.add(product.getCategory());
            categoryBreakdown.put(product.getCategory(), 
                categoryBreakdown.getOrDefault(product.getCategory(), 0) + 1);
            
            if (mostExpensive == null || product.getPrice() > mostExpensive.getPrice()) {
                mostExpensive = product;
            }
            
            if (cheapest == null || product.getPrice() < cheapest.getPrice()) {
                cheapest = product;
            }
        }
        
        stats.put("totalCategories", categories.size());
        stats.put("categoryBreakdown", categoryBreakdown);
        stats.put("mostExpensive", mostExpensive != null ? mostExpensive.getName() : "N/A");
        stats.put("cheapest", cheapest != null ? cheapest.getName() : "N/A");
        
        return stats;
    }
    
    // System Reports
    public Map<String, Object> getSystemReport() {
        Map<String, Object> report = new HashMap<>();
        
        report.put("totalUsers", userData.size());
        report.put("totalProducts", products.size());
        report.put("totalTransactions", transactions.size());
        
        // Calculate total revenue from transactions
        double totalRevenue = 0;
        for (String transaction : transactions) {
            // Extract amount from transaction string (simplified)
            if (transaction.contains("Rp ")) {
                try {
                    String amount = transaction.substring(transaction.indexOf("Rp ") + 3)
                            .replaceAll("[,.]", "");
                    totalRevenue += Double.parseDouble(amount);
                } catch (Exception e) {
                    // Skip if parsing fails
                }
            }
        }
        report.put("totalRevenue", totalRevenue);
        
        // User role breakdown
        Map<String, Integer> roleBreakdown = new HashMap<>();
        for (LoginUser user : userData.values()) {
            roleBreakdown.put(user.getRole(), 
                roleBreakdown.getOrDefault(user.getRole(), 0) + 1);
        }
        report.put("userRoleBreakdown", roleBreakdown);
        
        return report;
    }
    
    // System Management
    public boolean backupData() {
        // Simulate backup process
        try {
            Thread.sleep(1000); // Simulate processing time
            System.out.println("Data telah dibackup ke: backup_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".dat");
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
    
    public boolean restoreData() {
        // Simulate restore process
        try {
            Thread.sleep(1500); // Simulate processing time
            // Reset to initial state
            transactions.clear();
            addSampleTransactions();
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
    
    public void clearCache() {
        // Simulate cache clearing
        System.out.println("Cache sistem telah dibersihkan.");
    }
    
    public void resetSystem() {
        // Reset all data to initial state
        transactions.clear();
        userData.clear();
        products.clear();
        suspendedUsers.clear();
        
        // Reinitialize with default data
        initializeData();
        
        System.out.println("Sistem telah direset ke pengaturan awal.");
    }
    
    public String exportSystemReport() {
        String filename = "system_report_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
        
        // Simulate export process
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            // Handle interruption
        }
        
        return filename;
    }
}