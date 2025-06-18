package projectShopMenu.View;

import projectShopMenu.Model.*;
import projectShopMenu.Service.SellerService;
import java.util.*;

public class SellerView {
    private SellerService sellerService;
    private Scanner scanner;
    private LoginUser currentUser;

    public SellerView(LoginUser user) {
        this.sellerService = new SellerService();
        this.scanner = new Scanner(System.in);
        this.currentUser = user;
    }

    public void manageProducts() {
        System.out.println("\n=== KELOLA PRODUK ANDA ===");
        List<Product> sellerProducts = sellerService.getSellerProducts(currentUser.getUsername());
        
        if (sellerProducts.isEmpty()) {
            System.out.println("Anda belum memiliki produk yang terdaftar.");
            return;
        }
        
        for (int i = 0; i < sellerProducts.size(); i++) {
            System.out.println((i + 1) + ". " + sellerProducts.get(i));
        }
    }

    public void addNewProduct() {
        System.out.println("\n=== TAMBAH PRODUK BARU ===");
        
        System.out.print("Nama produk: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Nama produk tidak boleh kosong!");
            return;
        }

        System.out.print("Kategori: ");
        String category = scanner.nextLine().trim();
        
        System.out.print("Harga: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Harga harus berupa angka!");
            return;
        }

        System.out.print("Deskripsi: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Negara asal: ");
        String country = scanner.nextLine().trim();
        
        if (country.isEmpty()) {
            country = currentUser.getCountry();
        }

        System.out.print("Stok awal: ");
        int stock;
        try {
            stock = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Stok harus berupa angka!");
            return;
        }

        if (sellerService.addProduct(currentUser.getUsername(), name, category, price, description, country, stock)) {
            System.out.println("Produk berhasil ditambahkan!");
        } else {
            System.out.println("Gagal menambahkan produk!");
        }
    }

    public void updateStock() {
        System.out.println("\n=== UPDATE STOK PRODUK ===");
        
        List<Product> sellerProducts = sellerService.getSellerProducts(currentUser.getUsername());
        
        if (sellerProducts.isEmpty()) {
            System.out.println("Anda belum memiliki produk yang terdaftar.");
            return;
        }
        
        for (int i = 0; i < sellerProducts.size(); i++) {
            Product product = sellerProducts.get(i);
            int currentStock = sellerService.getProductStock(product.getId());
            System.out.println((i + 1) + ". " + product.getName() + " | Stok: " + currentStock);
        }
        
        System.out.print("Pilih produk (1-" + sellerProducts.size() + "): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return;
        }
        
        if (choice < 1 || choice > sellerProducts.size()) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        
        Product selectedProduct = sellerProducts.get(choice - 1);
        
        System.out.print("Stok baru: ");
        int newStock;
        try {
            newStock = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Stok harus berupa angka!");
            return;
        }
        
        if (sellerService.updateProductStock(selectedProduct.getId(), newStock)) {
            System.out.println("Stok berhasil diupdate!");
        } else {
            System.out.println("Gagal mengupdate stok!");
        }
    }

    public void deleteProduct() {
        System.out.println("\n=== HAPUS PRODUK ===");
        
        List<Product> sellerProducts = sellerService.getSellerProducts(currentUser.getUsername());
        
        if (sellerProducts.isEmpty()) {
            System.out.println("Anda belum memiliki produk yang terdaftar.");
            return;
        }
        
        for (int i = 0; i < sellerProducts.size(); i++) {
            System.out.println((i + 1) + ". " + sellerProducts.get(i));
        }
        
        System.out.print("Pilih produk yang akan dihapus (1-" + sellerProducts.size() + "): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return;
        }
        
        if (choice < 1 || choice > sellerProducts.size()) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        
        Product selectedProduct = sellerProducts.get(choice - 1);
        
        System.out.print("Yakin ingin menghapus produk '" + selectedProduct.getName() + "'? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            if (sellerService.deleteProduct(selectedProduct.getId())) {
                System.out.println("Produk berhasil dihapus!");
            } else {
                System.out.println("Gagal menghapus produk!");
            }
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    public void viewSalesReport() {
        System.out.println("\n=== LAPORAN PENJUALAN ===");
        
        Map<String, Object> salesData = sellerService.getSalesReport(currentUser.getUsername());
        
        System.out.println("Total Produk Terjual: " + salesData.get("totalSold"));
        System.out.println("Total Pendapatan: Rp " + salesData.get("totalRevenue"));
        System.out.println("Produk Terlaris: " + salesData.get("bestSeller"));
        
        @SuppressWarnings("unchecked")
        List<String> recentSales = (List<String>) salesData.get("recentSales");
        
        if (!recentSales.isEmpty()) {
            System.out.println("\nPenjualan Terbaru:");
            for (String sale : recentSales) {
                System.out.println("- " + sale);
            }
        }
    }
}