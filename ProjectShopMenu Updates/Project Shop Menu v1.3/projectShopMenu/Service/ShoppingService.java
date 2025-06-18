package projectShopMenu.Service;

import projectShopMenu.Model.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Service class untuk menangani business logic aplikasi shopping
 *
 * @author Jose
 */
public class ShoppingService {

    private Map<String, String> currencyMap;
    private Map<String, Double> exchangeRates;
    private List<Product> products;
    private List<CartItem> cart;
    private List<Courier> couriers;
    private List<PaymentMethod> paymentMethods;

    public ShoppingService() {
        products = new ArrayList<>();
        cart = new ArrayList<>();
        couriers = new ArrayList<>();
        paymentMethods = new ArrayList<>();
        currencyMap = new HashMap<>();
        exchangeRates = new HashMap<>();
        initializeData();
        initializeCurrency();
    }

    private void initializeData() {
        initializeProducts();
        initializeCouriers();
        initializePaymentMethods();
    }

    // Initialize Products
    private void initializeProducts() {
        products.add(new Product(1, "Smartphone Samsung Galaxy", "Elektronik", 8500000, "Smartphone flagship terbaru", "korea"));
        products.add(new Product(2, "Laptop ASUS VivoBook", "Elektronik", 12000000, "Laptop untuk kerja dan gaming", "amerika"));
        products.add(new Product(3, "Sepatu Nike Air Max", "Fashion", 1500000, "Sepatu olahraga premium", "amerika"));
        products.add(new Product(4, "Kemeja Hawaii", "Fashion", 350000, "Kemeja untuk acara formal", "hawaii"));
        products.add(new Product(5, "Skincare Laruel edition Set", "Kecantikan", 450000, "Set perawatan kulit lengkap", "kamboja"));
        products.add(new Product(6, "Protein om dedy Powder", "Kesehatan", 800000, "Suplemen protein untuk fitness", "indonesia"));
        products.add(new Product(7, "Buku Pemrograman Java", "Edukasi", 250000, "Panduan lengkap Java programming", "indonesia"));
        products.add(new Product(8, "Kopi Premium Arabica", "Makanan", 180000, "Kopi premium dari petani lokal", "arabica"));
    }

    // Initialize Couriers
    private void initializeCouriers() {
        couriers.add(new Courier("JNE Regular", 15000, "2-3 hari"));
        couriers.add(new Courier("JNE YES", 25000, "1-2 hari"));
        couriers.add(new Courier("J&T Express", 12000, "2-4 hari"));
        couriers.add(new Courier("SiCepat Regular", 13000, "2-3 hari"));
        couriers.add(new Courier("SiCepat HALU", 22000, "1 hari"));
        couriers.add(new Courier("Pos Indonesia", 10000, "3-5 hari"));
        couriers.add(new Courier("AnterAja", 14000, "2-4 hari"));
    }

    // Initialize Payment Methods
    private void initializePaymentMethods() {
        paymentMethods.add(new PaymentMethod("Cash on Delivery (COD)", "Bayar saat barang tiba", 5000, 0));
        paymentMethods.add(new PaymentMethod("Transfer Bank", "Transfer ke rekening toko", 0, 0));
        paymentMethods.add(new PaymentMethod("E-Wallet (OVO)", "Pembayaran via OVO", 0, 0.5));
        paymentMethods.add(new PaymentMethod("E-Wallet (GoPay)", "Pembayaran via GoPay", 0, 0.5));
        paymentMethods.add(new PaymentMethod("E-Wallet (DANA)", "Pembayaran via DANA", 0, 0.7));
        paymentMethods.add(new PaymentMethod("Kartu Kredit", "Pembayaran dengan kartu kredit", 2500, 2.9));
        paymentMethods.add(new PaymentMethod("Virtual Account", "Transfer via VA Bank", 4000, 0));
        paymentMethods.add(new PaymentMethod("QRIS", "Scan QR untuk pembayaran", 0, 0.7));
    }

    // Method baru untuk inisialisasi mata uang
    private void initializeCurrency() {
        // Mapping negara ke mata uang
        currencyMap.put("indonesia", "IDR");
        currencyMap.put("korea", "KRW");
        currencyMap.put("amerika", "USD");
        currencyMap.put("hawaii", "USD");
        currencyMap.put("kamboja", "KHR");
        currencyMap.put("arabica", "SAR");

        // Exchange rates dari IDR (Indonesian Rupiah sebagai base)
        exchangeRates.put("IDR", 1.0);
        exchangeRates.put("KRW", 0.0855); // 1 IDR = 0.0855 KRW
        exchangeRates.put("USD", 0.000067); // 1 IDR = 0.000067 USD
        exchangeRates.put("KHR", 0.27); // 1 IDR = 0.27 KHR
        exchangeRates.put("SAR", 0.00025); // 1 IDR = 0.00025 SAR
    }
    
    public String getCurrencyByCountry(String country) {
    return currencyMap.getOrDefault(country.toLowerCase(), "IDR");
}
    
    public double convertPrice(double priceInIDR, String targetCurrency) {
    Double rate = exchangeRates.get(targetCurrency);
    if (rate != null) {
        return priceInIDR * rate;
    }
    return priceInIDR; // Return original price if currency not found
}
    
    // Method untuk format mata uang
public String formatCurrency(double amount, String currency) {
    switch (currency) {
        case "USD":
            return String.format("$%.2f", amount);
        case "KRW":
            return String.format("₩%.0f", amount);
        case "KHR":
            return String.format("៛%.0f", amount);
        case "SAR":
            return String.format("﷼%.2f", amount);
        case "IDR":
        default:
            return String.format("Rp %.2f", amount);
    }
}

    // Product Services
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Product product : products) {
            if (product.getName().toLowerCase().contains(lowerKeyword)
                    || product.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(product);
            }
        }
        return results;
    }

    public List<Product> filterByCategory(String category) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                results.add(product);
            }
        }
        return results;
    }

    public List<Product> filterByCountry(String country) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getCountry().equals(country)) {
                results.add(product);
            }
        }
        return results;
    }

    public Set<String> getAvailableCategories() {
        Set<String> categories = new HashSet<>();
        for (Product product : products) {
            categories.add(product.getCategory());
        }
        return categories;
    }

    public Set<String> getAvailableCountries() {
        Set<String> countries = new HashSet<>();
        for (Product product : products) {
            countries.add(product.getCountry());
        }
        return countries;
    }

    public Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    // Cart Services
    public boolean addToCart(int productId, int quantity) {
        Product product = findProductById(productId);
        if (product == null || quantity <= 0) {
            return false;
        }

        CartItem existingItem = findCartItem(productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            cart.add(new CartItem(product, quantity));
        }
        return true;
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cart);
    }

    public boolean updateCartItemQuantity(int itemIndex, int newQuantity) {
        if (itemIndex < 0 || itemIndex >= cart.size() || newQuantity <= 0) {
            return false;
        }
        cart.get(itemIndex).setQuantity(newQuantity);
        return true;
    }

    public boolean removeCartItem(int itemIndex) {
        if (itemIndex < 0 || itemIndex >= cart.size()) {
            return false;
        }
        cart.remove(itemIndex);
        return true;
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public double calculateCartTotal() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        cart.clear();
    }

    private CartItem findCartItem(int productId) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }

    private boolean isProductInCart(int productId) {
        return findCartItem(productId) != null;
    }

    // Courier Services
    public List<Courier> getAllCouriers() {
        return new ArrayList<>(couriers);
    }

    public Courier getCourierByIndex(int index) {
        if (index < 0 || index >= couriers.size()) {
            return null;
        }
        return couriers.get(index);
    }

    // Payment Services
    public List<PaymentMethod> getAllPaymentMethods() {
        return new ArrayList<>(paymentMethods);
    }

    public PaymentMethod getPaymentMethodByIndex(int index) {
        if (index < 0 || index >= paymentMethods.size()) {
            return null;
        }
        return paymentMethods.get(index);
    }

    // Recommendation Services
    public List<Product> getPersonalRecommendations() {
        if (cart.isEmpty()) {
            // Return top 3 products as default recommendations
            return products.subList(0, Math.min(3, products.size()));
        }

        // Get categories from cart items
        Set<String> cartCategories = new HashSet<>();
        for (CartItem item : cart) {
            cartCategories.add(item.getProduct().getCategory());
        }

        // Find products in same categories that are not in cart
        List<Product> recommendations = new ArrayList<>();
        for (Product product : products) {
            if (cartCategories.contains(product.getCategory())
                    && !isProductInCart(product.getId())) {
                recommendations.add(product);
            }
        }

        return recommendations;
    }

    // Checkout Services
    public String generateOrderId() {
        return "PS" + System.currentTimeMillis() % 100000;
    }

    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date());
    }

    public String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}
