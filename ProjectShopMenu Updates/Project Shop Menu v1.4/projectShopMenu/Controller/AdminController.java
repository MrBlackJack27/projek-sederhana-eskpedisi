package projectShopMenu.Controller;

import projectShopMenu.Model.LoginUser;
import projectShopMenu.View.AdminView;
import java.util.Scanner;

public class AdminController {
    private AdminView adminView;
    private Scanner scanner;
    private LoginUser currentUser;
    
    public AdminController(LoginUser user) {
        this.currentUser = user;
        this.adminView = new AdminView(user);
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        while (true) {
            displayAdminMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    adminView.monitorTransactions();
                    pauseScreen();
                    break;
                case 2:
                    adminView.manageUsers();
                    pauseScreen();
                    break;
                case 3:
                    adminView.manageProducts();
                    pauseScreen();
                    break;
                case 4:
                    adminView.showSystemReport();
                    pauseScreen();
                    break;
                case 5:
                    adminView.systemSettings();
                    pauseScreen();
                    break;
                case 6:
                    // Change password - implement if needed
                    System.out.println("Fitur ubah password akan diimplementasi oleh LoginController.");
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
    
    private void displayAdminMenu() {
        System.out.println("\n=== MENU ADMIN: " + currentUser.getUsername().toUpperCase() + " ===");
        System.out.println("1. Pantau Transaksi");
        System.out.println("2. Manajemen User");
        System.out.println("3. Manajemen Produk");
        System.out.println("4. Laporan Sistem");
        System.out.println("5. Pengaturan Sistem");
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