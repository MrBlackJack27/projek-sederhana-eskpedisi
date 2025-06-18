package projectShopMenu.Controller;

import projectShopMenu.Model.LoginUser;
import projectShopMenu.View.SellerView;
import java.util.Scanner;

public class SellerController {
    private SellerView sellerView;
    private Scanner scanner;
    private LoginUser currentUser;
    
    public SellerController(LoginUser user) {
        this.currentUser = user;
        this.sellerView = new SellerView(user);
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        while (true) {
            displaySellerMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    sellerView.manageProducts();
                    pauseScreen();
                    break;
                case 2:
                    sellerView.addNewProduct();
                    pauseScreen();
                    break;
                case 3:
                    sellerView.updateStock();
                    pauseScreen();
                    break;
                case 4:
                    sellerView.deleteProduct();
                    pauseScreen();
                    break;
                case 5:
                    sellerView.viewSalesReport();
                    pauseScreen();
                    break;
                case 6:
                    // Change password - implement if needed
                    System.out.println("Fitur ubah password belum diimplementasi.");
                    pauseScreen();
                    break;
                case 7:
                    System.out.println("Logout berhasil. Sampai jumpa, " + currentUser.getUsername() + "!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
                    pauseScreen();
            }
        }
    }
    
    private void displaySellerMenu() {
        System.out.println("\n=== MENU SELLER: " + currentUser.getUsername().toUpperCase() + " ===");
        System.out.println("1. Kelola Produk");
        System.out.println("2. Tambah Produk Baru");
        System.out.println("3. Update Stok");
        System.out.println("4. Hapus Produk");
        System.out.println("5. Laporan Penjualan");
        System.out.println("6. Ubah Password");
        System.out.println("7. Logout");
        System.out.print("\nPilih menu (1-7): ");
    }
    
    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void pauseScreen() {
        System.out.println("\nTekan Enter untuk melanjutkan...");
        scanner.nextLine();
    }
}