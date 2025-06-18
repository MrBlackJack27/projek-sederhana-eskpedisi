package projectShopMenu.Controller;

import java.util.Scanner;
import projectShopMenu.Model.LoginUser;
import projectShopMenu.Service.LoginService;
import projectShopMenu.View.LoginView;
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
        ShoppingView shoppingView = new ShoppingView(currentUser); // UBAH INI

        while (loggedIn) {
            loginView.displayUserMenu(username);
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    shoppingView.startApplication();
                    break;
                case 2:
                    handleUserChangePassword(username);
                    break;
                case 3:
                    loginView.displayLogout(username);
                    loggedIn = false;
                    break;

                default:
                    loginView.displayError("Pilihan tidak tersedia!");
            }

            if (loggedIn && choice == 1) {
                loginView.pauseScreen();
                scan.nextLine();
            }
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

        if (loginService.registerUser(username, password, country)) {
            loginView.displayRegisterSuccess(username);
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
