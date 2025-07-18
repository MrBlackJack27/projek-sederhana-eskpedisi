package projectShopMenu.View;

public class LoginView {
    public void displayWelcome(){
        System.out.println("Login Console");
    }
    
    public void displayMenu(){
        System.out.println("\n=== MENU UTAMA ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Ubah Password");
        System.out.println("4. Keluar");
        System.out.print("\nPilih menu (1-4): ");
    }
    
    public void displayLogin() {
        System.out.println("\n=== LOGIN ===");
    }
    
    public void displayRegister() {
        System.out.println("\n=== REGISTER ===");
    }
    
    public void displayChangePasswordHeader() {
        System.out.println("\n=== UBAH PASSWORD ===");
    }
    
    public void displayUserMenu(String username) {
        System.out.println("\n=== MENU USER: " + username.toUpperCase() + " ===");
        System.out.println("1. Shop");
        System.out.println("2. Ubah Password");
        System.out.println("3. Logout");
        System.out.print("\nPilih menu (1-3): ");
    }
    
    public void displaytUsername() {
        System.out.print("Username: ");
    }
    
    public void displayPassword() {
        System.out.print("Password: ");
    }
    
    public void displayCountry() {
    System.out.print("Domisili (default: Indonesia): ");
}
    
    public void displayNewUsername() {
        System.out.print("Username baru: ");
    }
    
    public void displayOldPassword() {
        System.out.print("Password lama: ");
    }
    
    public void displayNewPassword() {
        System.out.print("Password baru: ");
    }
    
    public void displayConfirmPassword() {
        System.out.print("Konfirmasi password: ");
    }
    
     public void displayLoginSuccess(String username) {
        System.out.println("\nLOGIN BERHASIL!");
        System.out.println("Selamat datang, " + username + "!");
    }
    
    public void displayLoginFailed() {
        System.out.println("\nLOGIN GAGAL!");
        System.out.println("Username atau password salah!");
    }
    
    public void displayRegisterSuccess(String username) {
        System.out.println("\nREGISTRASI BERHASIL!");
        System.out.println("Akun " + username + " telah dibuat. Silakan login.");
    }
    
    public void displayLogout(String username) {
        System.out.println("\nLogout berhasil. Sampai jumpa, " + username + "!");
    }
    
    public void displaySuccess(String message) {
        System.out.println(message);
    }
    
    public void displayError(String message) {
        System.out.println(message);
    }
    
    public void displayGoodbye() {
        System.out.println("\nTerima kasih telah menggunakan sistem login!");
    }
    
    public void pauseScreen() {
        System.out.println("\nTekan Enter untuk melanjutkan...");
    }

    public void displayReturnToPrevious() {
        System.out.println("\nTekan Enter untuk kembali ke menu sebelumnya...");
    }
}
