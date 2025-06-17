package projectShopMenu.Service;

import java.util.Map;
import java.util.HashMap;
import projectShopMenu.Model.LoginUser;

public class LoginService {
    private Map<String, LoginUser> userData;
    
    public LoginService(){
        this.userData = new HashMap(); 
        accountUser();
    }
    
    private void accountUser(){
        userData.put("admin", new LoginUser("admin", "123"));
        userData.put("Faris Khadafi", new LoginUser("faris", "123"));
    }
    
    public boolean authenticateUser(String username, String password){
        LoginUser user = userData.get(username);
        return user != null && user.getPassword().equals(password);
    }
    
    public boolean registerUser(String username, String password){
        if(userData.containsKey(username)){
            return false;
        }
        userData.put(username, new LoginUser(username, password));
        return true;
    }
    
    public boolean changePassword(String username, String oldPassword, String newPassword){
        LoginUser user = userData.get(username);
        if(user != null && user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    
    public boolean userExists(String username){
        return userData.containsKey(username);
    }
}
