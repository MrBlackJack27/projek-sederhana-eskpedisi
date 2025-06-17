package projectShopMenu.View;

import projectShopMenu.Model.*;
import projectShopMenu.Service.ShoppingService;
import java.util.*;

public class ShoppingView {
    private ShoppingService shoppingService;
    private Scanner scanner;
    
    public ShoppingView() {
        this.shoppingService = new ShoppingService();
        this.scanner = new Scanner(System.in);
    }
    
    public void showWelcomeMessage() {
        System.out.println("Selamat datang di Personal Shopper!");
        System.out.println("Asisten belanja personal Anda.");
    }
    
    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== PERSONAL SHOPPER MENU ===");
            System.out.println("1. Lihat Semua Produk");
            System.out.println("2. Cari Produk");
            System.out.println("3. Filter by Kategori");
            System.out.println("4. Filter by Negara");
            System.out.println("5. Tambah ke Keranjang");
            System.out.println("6. Lihat Keranjang");
            System.out.println("7. Update Keranjang");
            System.out.println("8. Checkout");
            System.out.println("9. Rekomendasi Personal");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu (0-9): ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1: viewAllProducts(); break;
                case 2: searchProducts(); break;
                case 3: filterByCategory(); break;
                case 4: filterByCountry(); break;
                case 5: addToCart(); break;
                case 6: viewCart(); break;
                case 7: updateCart(); break;
                case 8: checkout(); break;
                case 9: showPersonalRecommendations(); break;
                case 0: 
                    System.out.println("Terima kasih telah menggunakan Personal Shopper!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private void viewAllProducts() {
        System.out.println("\n=== SEMUA PRODUK ===");
        List<Product> products = shoppingService.getAllProducts();
        for (Product product : products) {
            System.out.println(product);
        }
    }
    
    private void searchProducts() {
        System.out.print("Masukkan kata kunci pencarian: ");
        String keyword = scanner.nextLine();
        
        List<Product> results = shoppingService.searchProducts(keyword);
        
        System.out.println("\n=== HASIL PENCARIAN ===");
        if (results.isEmpty()) {
            System.out.println("Produk tidak ditemukan!");
        } else {
            for (Product product : results) {
                System.out.println(product);
            }
        }
    }
    
    private void filterByCategory() {
        Set<String> categories = shoppingService.getAvailableCategories();
        List<String> categoryList = new ArrayList<>(categories);
        
        System.out.println("\n=== KATEGORI TERSEDIA ===");
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i + 1) + ". " + categoryList.get(i));
        }
        
        System.out.print("Pilih kategori (1-" + categoryList.size() + "): ");
        int choice = getIntInput();
        
        if (choice > 0 && choice <= categoryList.size()) {
            String selectedCategory = categoryList.get(choice - 1);
            List<Product> products = shoppingService.filterByCategory(selectedCategory);
            
            System.out.println("\n=== PRODUK KATEGORI: " + selectedCategory.toUpperCase() + " ===");
            for (Product product : products) {
                System.out.println(product);
            }
        } else {
            System.out.println("Pilihan tidak valid!");
        }
    }
    
    private void filterByCountry() {
        Set<String> countries = shoppingService.getAvailableCountries();
        List<String> countryList = new ArrayList<>(countries);
        
        System.out.println("\n=== NEGARA TERSEDIA ===");
        for (int i = 0; i < countryList.size(); i++) {
            System.out.println((i + 1) + ". " + countryList.get(i));
        }
        
        System.out.print("Pilih negara (1-" + countryList.size() + "): ");
        int choice = getIntInput();
        
        if (choice > 0 && choice <= countryList.size()) {
            String selectedCountry = countryList.get(choice - 1);
            List<Product> products = shoppingService.filterByCountry(selectedCountry);
            
            System.out.println("\n=== PRODUK NEGARA: " + selectedCountry.toUpperCase() + " ===");
            for (Product product : products) {
                System.out.println(product);
            }
        } else {
            System.out.println("Pilihan tidak valid!");
        }
    }
    
    private void addToCart() {
        viewAllProducts();
        System.out.print("Masukkan ID produk yang ingin ditambahkan: ");
        int productId = getIntInput();
        
        Product selectedProduct = shoppingService.findProductById(productId);
        if (selectedProduct == null) {
            System.out.println("Produk tidak ditemukan!");
            return;
        }
        
        System.out.print("Masukkan jumlah: ");
        int quantity = getIntInput();
        
        if (shoppingService.addToCart(productId, quantity)) {
            System.out.println("Produk berhasil ditambahkan ke keranjang!");
        } else {
            System.out.println("Gagal menambahkan produk ke keranjang!");
        }
    }
    
    private void viewCart() {
        if (shoppingService.isCartEmpty()) {
            System.out.println("\nKeranjang kosong!");
            return;
        }
        
        System.out.println("\n=== KERANJANG BELANJA ===");
        List<CartItem> cartItems = shoppingService.getCartItems();
        
        for (int i = 0; i < cartItems.size(); i++) {
            System.out.println((i + 1) + ". " + cartItems.get(i));
        }
        System.out.println("--------------------------------");
        System.out.printf("TOTAL: Rp %.2f\n", shoppingService.calculateCartTotal());
    }
    
    private void updateCart() {
        if (shoppingService.isCartEmpty()) {
            System.out.println("\nKeranjang kosong!");
            return;
        }
        
        viewCart();
        System.out.println("\n1. Ubah jumlah item");
        System.out.println("2. Hapus item");
        System.out.print("Pilih aksi: ");
        int action = getIntInput();
        
        System.out.print("Pilih nomor item: ");
        int itemIndex = getIntInput() - 1;
        
        if (action == 1) {
            System.out.print("Masukkan jumlah baru: ");
            int newQuantity = getIntInput();
            if (shoppingService.updateCartItemQuantity(itemIndex, newQuantity)) {
                System.out.println("Jumlah berhasil diubah!");
            } else {
                System.out.println("Gagal mengubah jumlah item!");
            }
        } else if (action == 2) {
            if (shoppingService.removeCartItem(itemIndex)) {
                System.out.println("Item berhasil dihapus!");
            } else {
                System.out.println("Gagal menghapus item!");
            }
        } else {
            System.out.println("Aksi tidak valid!");
        }
    }
    
    private void checkout() {
        if (shoppingService.isCartEmpty()) {
            System.out.println("\nKeranjang kosong! Tidak bisa checkout.");
            return;
        }
        
        viewCart();
        double subtotal = shoppingService.calculateCartTotal();
        
        // Select courier
        List<Courier> couriers = shoppingService.getAllCouriers();
        System.out.println("\n=== PILIH KURIR PENGIRIMAN ===");
        for (int i = 0; i < couriers.size(); i++) {
            System.out.println((i + 1) + ". " + couriers.get(i));
        }
        
        System.out.print("Pilih kurir (1-" + couriers.size() + "): ");
        int courierChoice = getIntInput();
        
        Courier selectedCourier = shoppingService.getCourierByIndex(courierChoice - 1);
        if (selectedCourier == null) {
            System.out.println("Pilihan kurir tidak valid!");
            return;
        }
        
        // Select payment method
        List<PaymentMethod> paymentMethods = shoppingService.getAllPaymentMethods();
        System.out.println("\n=== PILIH METODE PEMBAYARAN ===");
        for (int i = 0; i < paymentMethods.size(); i++) {
            System.out.println((i + 1) + ". " + paymentMethods.get(i));
        }
        
        System.out.print("Pilih metode pembayaran (1-" + paymentMethods.size() + "): ");
        int paymentChoice = getIntInput();
        
        PaymentMethod selectedPayment = shoppingService.getPaymentMethodByIndex(paymentChoice - 1);
        if (selectedPayment == null) {
            System.out.println("Pilihan metode pembayaran tidak valid!");
            return;
        }
        
        // Calculate totals
        double shippingCost = selectedCourier.getPrice();
        double paymentFee = selectedPayment.calculateFee(subtotal + shippingCost);
        double total = subtotal + shippingCost + paymentFee;
        
        // Show checkout summary
        showCheckoutSummary(subtotal, selectedCourier, shippingCost, selectedPayment, paymentFee, total);
        
        System.out.print("\nKonfirmasi checkout? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            printReceipt(subtotal, selectedCourier, shippingCost, selectedPayment, paymentFee, total);
            shoppingService.clearCart();
        } else {
            System.out.println("Checkout dibatalkan.");
        }
    }
    
    private void showCheckoutSummary(double subtotal, Courier courier, double shippingCost, 
                                   PaymentMethod payment, double paymentFee, double total) {
        System.out.println("\n=== RINGKASAN CHECKOUT ===");
        System.out.printf("Subtotal produk: Rp %.2f\n", subtotal);
        System.out.printf("Ongkos kirim (%s): Rp %.2f\n", courier.getName(), shippingCost);
        System.out.printf("Biaya pembayaran (%s): Rp %.2f\n", payment.getName(), paymentFee);
        System.out.println("--------------------------------");
        System.out.printf("TOTAL PEMBAYARAN: Rp %.2f\n", total);
        System.out.println("Estimasi pengiriman: " + courier.getDeliveryTime());
    }
    
    private void printReceipt(double subtotal, Courier courier, double shippingCost, 
                             PaymentMethod payment, double paymentFee, double total) {
        
        String orderId = shoppingService.generateOrderId();
        String timestamp = shoppingService.getCurrentTimestamp();
        List<CartItem> cartItems = shoppingService.getCartItems();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            PERSONAL SHOPPER");
        System.out.println("          STRUK PEMBELIAN");
        System.out.println("=".repeat(50));
        System.out.println("Order ID    : " + orderId);
        System.out.println("Tanggal     : " + timestamp);
        System.out.println("=".repeat(50));
        
        // Detail produk
        System.out.println("DETAIL PEMBELIAN:");
        System.out.println("-".repeat(50));
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            System.out.printf("%-62s \n%2dx @%,.0f", 
                shoppingService.truncateString(item.getProduct().getName(), 62),
                item.getQuantity(), 
                item.getProduct().getPrice());
            System.out.printf("%-20s%,.2f\n","" ,item.getTotalPrice());
        }
        
        System.out.println("-".repeat(50));
        System.out.printf("%-35s %,.2f\n", "Subtotal:", subtotal);
        System.out.printf("%-35s %,.2f\n", "Ongkir (" + courier.getName() + "):", shippingCost);
        System.out.printf("%-35s %,.2f\n", "Biaya (" + payment.getName() + "):", paymentFee);
        System.out.println("=".repeat(50));
        System.out.printf("%-30s %,.2f\n", "TOTAL PEMBAYARAN:", total);
        System.out.println("=".repeat(50));
        
        // Detail pengiriman
        System.out.println("DETAIL PENGIRIMAN:");
        System.out.println("Kurir       : " + courier.getName());
        System.out.println("Estimasi    : " + courier.getDeliveryTime());
        System.out.println("Metode Bayar: " + payment.getName());
        
        System.out.println("=".repeat(50));
        System.out.println("        TERIMA KASIH ATAS");
        System.out.println("         PEMBELIAN ANDA!");
        System.out.println("=".repeat(50));
        System.out.println("Barang akan segera diproses dan dikirim");
        System.out.println("Simpan struk ini sebagai bukti pembelian");
        System.out.println("=".repeat(50));
    }
    
    private void showPersonalRecommendations() {
        List<Product> recommendations = shoppingService.getPersonalRecommendations();
        
        if (shoppingService.isCartEmpty()) {
            System.out.println("\n=== REKOMENDASI UNTUK ANDA ===");
            System.out.println("Produk terpopuler:");
        } else {
            System.out.println("\n=== REKOMENDASI BERDASARKAN KERANJANG ANDA ===");
        }
        
        if (recommendations.isEmpty()) {
            System.out.println("Tidak ada rekomendasi saat ini.");
        } else {
            for (Product product : recommendations) {
                if (shoppingService.isCartEmpty()) {
                    System.out.println(product);
                } else {
                    System.out.println("⭐ " + product);
                }
            }
        }
    }
    
    private int getIntInput() {
        try {
            int input = Integer.parseInt(scanner.nextLine());
            return input;
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return -1;
        }
    }
    
    public void startApplication() {
        showWelcomeMessage();
        showMainMenu();
    }
}