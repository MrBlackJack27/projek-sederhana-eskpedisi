/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.shopmenu;

import java.util.*;

/**
 *
 * @author Jose
 */
public class PersonalShopper {
    private List<Product> products;
    private List<CartItem> cart;
    private Scanner scanner;
    
    public PersonalShopper() {
        products = new ArrayList<>();
        cart = new ArrayList<>();
        scanner = new Scanner(System.in);
        initializeProducts();
    }
    
    // Inisialisasi produk sample
    private void initializeProducts() {
        products.add(new Product(1, "Smartphone Samsung Galaxy", "Elektronik", 8500000, "Smartphone flagship terbaru"));
        products.add(new Product(2, "Laptop ASUS VivoBook", "Elektronik", 12000000, "Laptop untuk kerja dan gaming"));
        products.add(new Product(3, "Sepatu Nike Air Max", "Fashion", 1500000, "Sepatu olahraga premium"));
        products.add(new Product(4, "Kemeja Formal", "Fashion", 350000, "Kemeja untuk acara formal"));
        products.add(new Product(5, "Skincare Set", "Kecantikan", 450000, "Set perawatan kulit lengkap"));
        products.add(new Product(6, "Protein Powder", "Kesehatan", 800000, "Suplemen protein untuk fitness"));
        products.add(new Product(7, "Buku Pemrograman Java", "Edukasi", 250000, "Panduan lengkap Java programming"));
        products.add(new Product(8, "Kopi Premium Arabica", "Makanan", 180000, "Kopi premium dari petani lokal"));
    }
    
    // Menu utama
    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== PERSONAL SHOPPER MENU ===");
            System.out.println("1. Lihat Semua Produk");
            System.out.println("2. Cari Produk");
            System.out.println("3. Filter by Kategori");
            System.out.println("4. Tambah ke Keranjang");
            System.out.println("5. Lihat Keranjang");
            System.out.println("6. Update Keranjang");
            System.out.println("7. Checkout");
            System.out.println("8. Rekomendasi Personal");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu (0-8): ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1: viewAllProducts(); break;
                case 2: searchProducts(); break;
                case 3: filterByCategory(); break;
                case 4: addToCart(); break;
                case 5: viewCart(); break;
                case 6: updateCart(); break;
                case 7: checkout(); break;
                case 8: showPersonalRecommendations(); break;
                case 0: 
                    System.out.println("Terima kasih telah menggunakan Personal Shopper!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    // Tampilkan semua produk
    private void viewAllProducts() {
        System.out.println("\n=== SEMUA PRODUK ===");
        for (Product product : products) {
            System.out.println(product);
        }
    }
    
    // Cari produk berdasarkan nama
    private void searchProducts() {
        System.out.print("Masukkan kata kunci pencarian: ");
        String keyword = scanner.nextLine().toLowerCase();
        
        System.out.println("\n=== HASIL PENCARIAN ===");
        boolean found = false;
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(keyword) || 
                product.getDescription().toLowerCase().contains(keyword)) {
                System.out.println(product);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("Produk tidak ditemukan!");
        }
    }
    
    // Filter produk berdasarkan kategori
    private void filterByCategory() {
        Set<String> categories = new HashSet<>();
        for (Product product : products) {
            categories.add(product.getCategory());
        }
        
        System.out.println("\n=== KATEGORI TERSEDIA ===");
        int i = 1;
        List<String> categoryList = new ArrayList<>(categories);
        for (String category : categoryList) {
            System.out.println(i + ". " + category);
            i++;
        }
        
        System.out.print("Pilih kategori (1-" + categoryList.size() + "): ");
        int choice = getIntInput();
        
        if (choice > 0 && choice <= categoryList.size()) {
            String selectedCategory = categoryList.get(choice - 1);
            System.out.println("\n=== PRODUK KATEGORI: " + selectedCategory.toUpperCase() + " ===");
            
            for (Product product : products) {
                if (product.getCategory().equals(selectedCategory)) {
                    System.out.println(product);
                }
            }
        } else {
            System.out.println("Pilihan tidak valid!");
        }
    }
    
    // Tambah produk ke keranjang
    private void addToCart() {
        viewAllProducts();
        System.out.print("Masukkan ID produk yang ingin ditambahkan: ");
        int productId = getIntInput();
        
        Product selectedProduct = findProductById(productId);
        if (selectedProduct == null) {
            System.out.println("Produk tidak ditemukan!");
            return;
        }
        
        System.out.print("Masukkan jumlah: ");
        int quantity = getIntInput();
        
        if (quantity <= 0) {
            System.out.println("Jumlah harus lebih dari 0!");
            return;
        }
        
        // Cek apakah produk sudah ada di keranjang
        CartItem existingItem = findCartItem(productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            System.out.println("Produk berhasil ditambahkan ke keranjang!");
        } else {
            cart.add(new CartItem(selectedProduct, quantity));
            System.out.println("Produk berhasil ditambahkan ke keranjang!");
        }
    }
    
    // Tampilkan isi keranjang
    private void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("\nKeranjang kosong!");
            return;
        }
        
        System.out.println("\n=== KERANJANG BELANJA ===");
        double total = 0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            System.out.println((i + 1) + ". " + item);
            total += item.getTotalPrice();
        }
        System.out.println("--------------------------------");
        System.out.printf("TOTAL: Rp %.2f\n", total);
    }
    
    // Update keranjang (ubah quantity atau hapus item)
    private void updateCart() {
        if (cart.isEmpty()) {
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
        
        if (itemIndex < 0 || itemIndex >= cart.size()) {
            System.out.println("Item tidak valid!");
            return;
        }
        
        CartItem item = cart.get(itemIndex);
        
        if (action == 1) {
            System.out.print("Masukkan jumlah baru: ");
            int newQuantity = getIntInput();
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                System.out.println("Jumlah berhasil diubah!");
            } else {
                System.out.println("Jumlah harus lebih dari 0!");
            }
        } else if (action == 2) {
            cart.remove(itemIndex);
            System.out.println("Item berhasil dihapus!");
        } else {
            System.out.println("Aksi tidak valid!");
        }
    }
    
    // Checkout
    private void checkout() {
        if (cart.isEmpty()) {
            System.out.println("\nKeranjang kosong! Tidak bisa checkout.");
            return;
        }
        
        viewCart();
        double total = calculateTotal();
        
        System.out.println("\n=== CHECKOUT ===");
        System.out.printf("Total pembayaran: Rp %.2f\n", total);
        System.out.print("Konfirmasi checkout? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            System.out.println("\n=== PEMBAYARAN BERHASIL ===");
            System.out.println("Terima kasih atas pembelian Anda!");
            System.out.println("Pesanan akan segera diproses.");
            cart.clear();
        } else {
            System.out.println("Checkout dibatalkan.");
        }
    }
    
    // Rekomendasi personal berdasarkan kategori terpopuler
    private void showPersonalRecommendations() {
        if (cart.isEmpty()) {
            System.out.println("\n=== REKOMENDASI UNTUK ANDA ===");
            System.out.println("Produk terpopuler:");
            // Tampilkan 3 produk pertama sebagai rekomendasi
            for (int i = 0; i < Math.min(3, products.size()); i++) {
                System.out.println("⭐ " + products.get(i));
            }
            return;
        }
        
        // Ambil kategori dari item di keranjang
        Set<String> cartCategories = new HashSet<>();
        for (CartItem item : cart) {
            cartCategories.add(item.getProduct().getCategory());
        }
        
        System.out.println("\n=== REKOMENDASI BERDASARKAN KERANJANG ANDA ===");
        boolean hasRecommendation = false;
        
        for (Product product : products) {
            if (cartCategories.contains(product.getCategory()) && 
                !isProductInCart(product.getId())) {
                System.out.println("⭐ " + product);
                hasRecommendation = true;
            }
        }
        
        if (!hasRecommendation) {
            System.out.println("Tidak ada rekomendasi saat ini.");
        }
    }
    
    // Helper methods
    private Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
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
    
    private double calculateTotal() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }
        return total;
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
    
    // Main method
    public static void main(String[] args) {
        PersonalShopper shopper = new PersonalShopper();
        System.out.println("Selamat datang di Personal Shopper!");
        System.out.println("Asisten belanja personal Anda.");
        shopper.showMainMenu();
    }
}
