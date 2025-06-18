package projectShopMenu.View;

import projectShopMenu.Model.*;
import projectShopMenu.Service.AdminService;
import java.util.*;

public class AdminView {

    private AdminService adminService;
    private Scanner scanner;
    private LoginUser currentUser;

    public AdminView(LoginUser user) {
        this.adminService = new AdminService();
        this.scanner = new Scanner(System.in);
        this.currentUser = user;
    }

    public void monitorTransactions() {
        System.out.println("\n=== PANTAU TRANSAKSI ===");
        List<String> transactions = adminService.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("Belum ada transaksi yang tercatat.");
            return;
        }

        System.out.println("Total Transaksi: " + transactions.size());
        System.out.println("\nDaftar Transaksi Terbaru:");
        for (int i = Math.max(0, transactions.size() - 10); i < transactions.size(); i++) {
            System.out.println((i + 1) + ". " + transactions.get(i));
        }

        System.out.println("\nStatistik Harian:");
        Map<String, Integer> dailyStats = adminService.getDailyTransactionStats();
        for (Map.Entry<String, Integer> entry : dailyStats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " transaksi");
        }
    }

    public void manageUsers() {
        while (true) {
            System.out.println("\n=== MANAJEMEN USER ===");
            System.out.println("1. Lihat Semua User");
            System.out.println("2. Cari User");
            System.out.println("3. Ubah Role User");
            System.out.println("4. Suspend User");
            System.out.println("5. Aktivasi User");
            System.out.println("6. Hapus User");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu (0-6): ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    searchUser();
                    break;
                case 3:
                    changeUserRole();
                    break;
                case 4:
                    suspendUser();
                    break;
                case 5:
                    activateUser();
                    break;
                case 6:
                    deleteUser();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n=== DAFTAR SEMUA USER ===");
        List<LoginUser> users = adminService.getAllUsers();

        System.out.printf("%-15s %-15s %-15s %-10s\n", "Username", "Role", "Country", "Status");
        System.out.println("-".repeat(60));

        for (LoginUser user : users) {
            String status = adminService.isUserSuspended(user.getUsername()) ? "Suspended" : "Active";
            System.out.printf("%-15s %-15s %-15s %-10s\n",
                    user.getUsername(), user.getRole(), user.getCountry(), status);
        }
    }

    private void searchUser() {
        System.out.print("Masukkan username yang dicari: ");
        String username = scanner.nextLine().trim();

        LoginUser user = adminService.getUserByUsername(username);
        if (user != null) {
            System.out.println("\n=== DETAIL USER ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Role: " + user.getRole());
            System.out.println("Country: " + user.getCountry());
            System.out.println("Status: " + (adminService.isUserSuspended(username) ? "Suspended" : "Active"));

            if (user.getRole().equals("seller")) {
                int productCount = adminService.getSellerProductCount(username);
                System.out.println("Jumlah Produk: " + productCount);
            }
        } else {
            System.out.println("User tidak ditemukan!");
        }
    }

    private void changeUserRole() {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine().trim();

        if (username.equals(currentUser.getUsername())) {
            System.out.println("Tidak dapat mengubah role sendiri!");
            return;
        }

        LoginUser user = adminService.getUserByUsername(username);
        if (user == null) {
            System.out.println("User tidak ditemukan!");
            return;
        }

        System.out.println("Role saat ini: " + user.getRole());
        System.out.println("1. Customer");
        System.out.println("2. Seller");
        System.out.println("3. Admin");
        System.out.print("Pilih role baru (1-3): ");

        int choice = getIntInput();
        String newRole;

        switch (choice) {
            case 1:
                newRole = "customer";
                break;
            case 2:
                newRole = "seller";
                break;
            case 3:
                newRole = "admin";
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }

        if (adminService.changeUserRole(username, newRole)) {
            System.out.println("Role berhasil diubah menjadi: " + newRole);
        } else {
            System.out.println("Gagal mengubah role!");
        }
    }

    private void suspendUser() {
        System.out.print("Masukkan username yang akan disuspend: ");
        String username = scanner.nextLine().trim();

        if (username.equals(currentUser.getUsername())) {
            System.out.println("Tidak dapat menyuspend diri sendiri!");
            return;
        }

        if (adminService.suspendUser(username)) {
            System.out.println("User berhasil disuspend!");
        } else {
            System.out.println("Gagal menyuspend user atau user tidak ditemukan!");
        }
    }

    private void activateUser() {
        System.out.print("Masukkan username yang akan diaktivasi: ");
        String username = scanner.nextLine().trim();

        if (adminService.activateUser(username)) {
            System.out.println("User berhasil diaktivasi!");
        } else {
            System.out.println("Gagal mengaktivasi user atau user tidak ditemukan!");
        }
    }

    private void deleteUser() {
        System.out.print("Masukkan username yang akan dihapus: ");
        String username = scanner.nextLine().trim();

        if (username.equals(currentUser.getUsername())) {
            System.out.println("Tidak dapat menghapus akun sendiri!");
            return;
        }

        System.out.print("Yakin ingin menghapus user '" + username + "'? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            if (adminService.deleteUser(username)) {
                System.out.println("User berhasil dihapus!");
            } else {
                System.out.println("Gagal menghapus user atau user tidak ditemukan!");
            }
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    public void manageProducts() {
        while (true) {
            System.out.println("\n=== MANAJEMEN PRODUK ===");
            System.out.println("1. Lihat Semua Produk");
            System.out.println("2. Cari Produk");
            System.out.println("3. Edit Produk");
            System.out.println("4. Hapus Produk");
            System.out.println("5. Statistik Produk");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu (0-5): ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAllProducts();
                    break;
                case 2:
                    searchProduct();
                    break;
                case 3:
                    editProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    showProductStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void viewAllProducts() {
        System.out.println("\n=== DAFTAR SEMUA PRODUK ===");
        List<Product> products = adminService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Belum ada produk yang terdaftar.");
            return;
        }

        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void searchProduct() {
        System.out.print("Masukkan ID atau nama produk: ");
        String keyword = scanner.nextLine().trim();

        List<Product> results = adminService.searchProducts(keyword);

        if (results.isEmpty()) {
            System.out.println("Produk tidak ditemukan!");
        } else {
            System.out.println("\n=== HASIL PENCARIAN ===");
            for (Product product : results) {
                System.out.println(product);
            }
        }
    }

    private void editProduct() {
        System.out.print("Masukkan ID produk yang akan diedit: ");
        int id = getIntInput();

        Product product = adminService.getProductById(id);
        if (product == null) {
            System.out.println("Produk tidak ditemukan!");
            return;
        }

        System.out.println("Data produk saat ini:");
        System.out.println(product);

        System.out.print("Nama baru (enter untuk tidak mengubah): ");
        String name = scanner.nextLine().trim();

        System.out.print("Kategori baru (enter untuk tidak mengubah): ");
        String category = scanner.nextLine().trim();

        System.out.print("Harga baru (0 untuk tidak mengubah): ");
        double price = getDoubleInput();

        System.out.print("Deskripsi baru (enter untuk tidak mengubah): ");
        String description = scanner.nextLine().trim();

        if (adminService.updateProduct(id, name, category, price, description)) {
            System.out.println("Produk berhasil diupdate!");
        } else {
            System.out.println("Gagal mengupdate produk!");
        }
    }

    private void deleteProduct() {
        System.out.print("Masukkan ID produk yang akan dihapus: ");
        int id = getIntInput();

        Product product = adminService.getProductById(id);
        if (product == null) {
            System.out.println("Produk tidak ditemukan!");
            return;
        }

        System.out.println("Produk yang akan dihapus:");
        System.out.println(product);

        System.out.print("Yakin ingin menghapus produk ini? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            if (adminService.deleteProduct(id)) {
                System.out.println("Produk berhasil dihapus!");
            } else {
                System.out.println("Gagal menghapus produk!");
            }
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    private void showProductStatistics() {
        System.out.println("\n=== STATISTIK PRODUK ===");
        Map<String, Object> stats = adminService.getProductStatistics();

        System.out.println("Total Produk: " + stats.get("totalProducts"));
        System.out.println("Total Kategori: " + stats.get("totalCategories"));
        System.out.println("Produk Termahal: " + stats.get("mostExpensive"));
        System.out.println("Produk Termurah: " + stats.get("cheapest"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> categoryStats = (Map<String, Integer>) stats.get("categoryBreakdown");

        System.out.println("\nBreakdown per Kategori:");
        for (Map.Entry<String, Integer> entry : categoryStats.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " produk");
        }
    }

    public void showSystemReport() {
        System.out.println("\n=== LAPORAN SISTEM ===");
        Map<String, Object> report = adminService.getSystemReport();

        System.out.println("Total User: " + report.get("totalUsers"));
        System.out.println("Total Produk: " + report.get("totalProducts"));
        System.out.println("Total Transaksi: " + report.get("totalTransactions"));
        System.out.println("Pendapatan Total: Rp " + report.get("totalRevenue"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> userStats = (Map<String, Integer>) report.get("userRoleBreakdown");

        System.out.println("\nBreakdown User per Role:");
        for (Map.Entry<String, Integer> entry : userStats.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " user");
        }
    }

    public void systemSettings() {
        while (true) {
            System.out.println("\n=== PENGATURAN SISTEM ===");
            System.out.println("1. Backup Data");
            System.out.println("2. Restore Data");
            System.out.println("3. Clear Cache");
            System.out.println("4. Reset System");
            System.out.println("5. Export Report");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu (0-5): ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    backupData();
                    break;
                case 2:
                    restoreData();
                    break;
                case 3:
                    clearCache();
                    break;
                case 4:
                    resetSystem();
                    break;
                case 5:
                    exportReport();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void backupData() {
        System.out.println("\nMemulai backup data...");
        if (adminService.backupData()) {
            System.out.println("Backup berhasil disimpan!");
        } else {
            System.out.println("Gagal melakukan backup!");
        }
    }

    private void restoreData() {
        System.out.print("Yakin ingin restore data? Semua perubahan akan hilang! (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            System.out.println("\nMemulai restore data...");
            if (adminService.restoreData()) {
                System.out.println("Restore berhasil!");
            } else {
                System.out.println("Gagal melakukan restore!");
            }
        } else {
            System.out.println("Restore dibatalkan.");
        }
    }

    private void clearCache() {
        System.out.println("\nMembersihkan cache...");
        adminService.clearCache();
        System.out.println("Cache berhasil dibersihkan!");
    }

    private void resetSystem() {
        System.out.print("PERINGATAN: Ini akan menghapus SEMUA data! Yakin? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            System.out.print("Ketik 'RESET' untuk konfirmasi: ");
            String confirmation = scanner.nextLine();

            if (confirmation.equals("RESET")) {
                System.out.println("\nMereset sistem...");
                adminService.resetSystem();
                System.out.println("Sistem berhasil direset!");
            } else {
                System.out.println("Konfirmasi salah. Reset dibatalkan.");
            }
        } else {
            System.out.println("Reset dibatalkan.");
        }
    }

    private void exportReport() {
        System.out.println("\nMengexport laporan...");
        String filename = adminService.exportSystemReport();
        System.out.println("Laporan berhasil diexport ke: " + filename);
    }

    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return -1;
        }
    }

    private double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void viewAllTransactions() {
        monitorTransactions(); // Gunakan method yang sudah ada
    }

    public void viewUserManagement() {
        manageUsers(); // Gunakan method yang sudah ada
    }

    public void viewProductManagement() {
        manageProducts(); // Gunakan method yang sudah ada
    }

    public void viewReports() {
        showSystemReport(); // Gunakan method yang sudah ada
    }

    public void manageSystem() {
        systemSettings(); // Gunakan method yang sudah ada
    }
}
