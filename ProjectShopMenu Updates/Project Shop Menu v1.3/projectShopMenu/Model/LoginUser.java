package projectShopMenu.Model;

public class LoginUser {
    private String username;
    private String password;
    private String country;
    
    public LoginUser(String username, String password, String country){
        this.username = username;
        this.password = password;
        this.country = country;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    public String getCountry(){
        return country;
    }
    public void setCountry(String country){
        this.country = country;
    }
}
