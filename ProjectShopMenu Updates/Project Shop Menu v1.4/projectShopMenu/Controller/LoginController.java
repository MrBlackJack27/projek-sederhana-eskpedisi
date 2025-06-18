package projectShopMenu.Controller;

import java.util.Scanner;
import projectShopMenu.Model.LoginUser;
import projectShopMenu.Service.LoginService;
import projectShopMenu.View.AdminView;
import projectShopMenu.View.LoginView;
import projectShopMenu.View.SellerView;
import projectShopMenu.View.ShoppingView;

public class LoginController {

    private LoginService loginService;
    private LoginView loginView;
    private Scanner scan;

    public LoginController() {
        this.loginService = new LoginService();
        this.loginView = new LoginView();
        this.scan = new Scanner(System.in);
    }

    public void start() {
        loginView.displayWelcome();

        boolean running = true;

        while (running) {
            loginView.displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    handleChangePassword();
                    break;
                case 4:
                    loginView.displayGoodbye();
                    System.exit(0);
                default:
                    loginView.displayError("Pilihan tidak valid, Silahkan coba lagi!");
            }
        }
    }

    private int getMenuChoice() {
        try {
            return Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void handleLogin() {
        loginView.displayLogin();

        loginView.displaytUsername();
        String username = scan.nextLine().trim();

        if (username.isEmpty()) {
            loginView.displayError("Username tidak boleh kosong!");
            return;
        }

        loginView.displayPassword();
        String password = scan.nextLine().trim();

        if (password.isEmpty()) {
            loginView.displayError("Password tidak boleh kosong!");
            return;
        }

        if (loginService.authenticateUser(username, password)) {
            loginView.displayLoginSuccess(username);
            handleUserSession(username);
        } else {
            loginView.displayLoginFailed();
        }
    }

    private void handleUserSession(String username) {
        boolean loggedIn = true;
        LoginUser currentUser = loginService.getUser(username);

        while (loggedIn) {
            // UBAH: tampilkan menu berdasarkan role
            displayRoleBasedMenu(currentUser);
            int choice = getMenuChoice();

            switch (currentUser.getRole().toLowerCase()) {
                case "customer":
                    loggedIn = handleCustomerMenu(currentUser, choice);
                    break;
                case "seller":
                    loggedIn = handleSellerMenu(currentUser, choice);
                    break;
                case "admin":
                    loggedIn = handleAdminMenu(currentUser, choice);
                    break;
                default:
                    loginView.displayError("Role tidak dikenali!");
                    loggedIn = false;
            }

            if (loggedIn && needsPause(choice, currentUser.getRole())) {
                loginView.pauseScreen();
                scan.nextLine();
            }
        }
    }

    private void displayRoleBasedMenu(LoginUser user) {
        switch (user.getRole().toLowerCase()) {
            case "customer":
                loginView.displayCustomerMenu(user.getUsername());
                break;
            case "seller":
                loginView.displaySellerMenu(user.getUsername());
                break;
            case "admin":
                loginView.displayAdminMenu(user.getUsername());
                break;
        }
    }

    private boolean handleCustomerMenu(LoginUser user, int choice) {
        ShoppingView shoppingView = new ShoppingView(user);

        switch (choice) {
            case 1:
                shoppingView.startApplication();
                return true;
            case 2:
                handleUserChangePassword(user.getUsername());
                return true;
            case 3:
                loginView.displayLogout(user.getUsername());
                return false;
            default:
                loginView.displayError("Pilihan tidak tersedia!");
                return true;
        }
    }

    private boolean handleSellerMenu(LoginUser user, int choice) {
        SellerView sellerView = new SellerView(user);

        switch (choice) {
            case 1:
                sellerView.manageProducts();
                return true;
            case 2:
                sellerView.addNewProduct();
                return true;
            case 3:
                sellerView.updateStock();
                return true;
            case 4:
                sellerView.deleteProduct();
                return true;
            case 5:
                sellerView.viewSalesReport();
                return true;
            case 6:
                handleUserChangePassword(user.getUsername());
                return true;
            case 7:
                loginView.displayLogout(user.getUsername());
                return false;
            default:
                loginView.displayError("Pilihan tidak tersedia!");
                return true;
        }
    }

    private boolean handleAdminMenu(LoginUser user, int choice) {
        AdminView adminView = new AdminView(user);

        switch (choice) {
            case 1:
                adminView.monitorTransactions();
                return true;
            case 2:
                adminView.manageUsers();
                return true;
            case 3:
                adminView.manageProducts();
                return true;
            case 4:
                adminView.showSystemReport();
                return true;
            case 5:
                adminView.systemSettings();
                return true;
            case 6:
                handleUserChangePassword(user.getUsername());
                return true;
            case 7:
                loginView.displayLogout(user.getUsername());
                return false;
            default:
                loginView.displayError("Pilihan tidak tersedia!");
                return true;
        }
    }

    private boolean needsPause(int choice, String role) {
        switch (role.toLowerCase()) {
            case "customer":
                return choice == 1;
            case "seller":
                return choice >= 1 && choice <= 5;
            case "admin":
                return choice >= 1 && choice <= 5;
            default:
                return false;
        }
    }

    private void handleUserChangePassword(String username) {
        loginView.displayChangePasswordHeader();

        loginView.displayOldPassword();
        String oldPassword = scan.nextLine().trim();

        if (oldPassword.isEmpty()) {
            loginView.displayError("Password Lama Tidak Boleh Kosong!");
            return;
        }

        loginView.displayNewPassword();
        String newPassword = scan.nextLine().trim();

        if (newPassword.isEmpty()) {
            loginView.displayError("Password Lama Tidak Boleh Kosong!");
            return;
        }

        loginView.displayConfirmPassword();
        String confirmPassword = scan.nextLine().trim();

        if (!newPassword.equals(confirmPassword)) {
            loginView.displayError("Konfirmasi Password tidak cocok dengan new Password!");
            return;
        }

        if (loginService.changePassword(username, oldPassword, newPassword)) {
            loginView.displaySuccess("Password Berhasil Diubah!");
        } else {
            loginView.displayError("Password lama salah!");
        }
    }

    private void handleRegister() {
        loginView.displayRegister();

        loginView.displayNewUsername();
        String username = scan.nextLine().trim();

        if (username.isEmpty()) {
            loginView.displayError("Username tidak boleh kosong");
            return;
        }

        if (loginService.userExists(username)) {
            loginView.displayError("Username sudah terdaftar");
            return;
        }

        loginView.displayPassword();
        String password = scan.nextLine().trim();

        if (password.isEmpty()) {
            loginView.displayError("Password tidak boleh kosong!");
            return;
        }

        loginView.displayConfirmPassword();
        String confirmPassword = scan.nextLine();

        if (confirmPassword.isEmpty()) {
            loginView.displayError("Password tidak boleh kosong!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            loginView.displayError("Password tidak cocok!");
            return;
        }

        loginView.displayCountry();
        String country = scan.nextLine().trim();

        if (country.isEmpty()) {
            country = "Indonesia"; // default domisili
        }
        loginView.displayRoleSelection();
        int roleChoice = getMenuChoice();

        String role;
        switch (roleChoice) {
            case 1:
                role = "customer";
                break;
            case 2:
                role = "seller";
                break;
            default:
                loginView.displayError("Pilihan role tidak valid!");
                return;
        }

        if (loginService.registerUser(username, password, country, role)) {
            loginView.displayRegisterSuccess(username);
            System.out.println("Role: " + role.toUpperCase());
        } else {
            loginView.displayError("Register gagal!");
        }
    }

    private void handleChangePassword() {
        loginView.displayChangePasswordHeader();

        loginView.displaytUsername();
        String username = scan.nextLine().trim();

        if (username.isEmpty()) {
            loginView.displayError("Username tidak Boleh kosong");
            return;
        }

        if (!loginService.userExists(username)) {
            loginView.displayError("Username tidak ditemukan");
            return;
        }

        loginView.displayOldPassword();
        String oldPassword = scan.nextLine().trim();

        if (oldPassword.isEmpty()) {
            loginView.displayError("Password lama tidak boleh kosong!");
            return;
        }

        loginView.displayNewPassword();
        String newPassword = scan.nextLine().trim();

        if (newPassword.isEmpty()) {
            loginView.displayError("Password baru tidak boleh kosong!");
            return;
        }

        loginView.displayConfirmPassword();
        String confirmPassword = scan.nextLine();

        if (confirmPassword.isEmpty()) {
            loginView.displayError("Password baru tidak boleh kosong!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            loginView.displayError("Konfirmasi password tidak cocok!");
            return;
        }

        if (loginService.changePassword(username, oldPassword, newPassword)) {
            loginView.displaySuccess("Password untuk user " + username + " Berhasil diubah");
        } else {
            loginView.displayError("Password lama salah");
        }
    }
}
