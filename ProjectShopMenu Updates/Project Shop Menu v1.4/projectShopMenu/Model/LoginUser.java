package projectShopMenu.Model;

public class LoginUser {
    private String username;
    private String password;
    private String country;
    private String role;
    
    public LoginUser(String username, String password, String country, String role){
        this.username = username;
        this.password = password;
        this.country = country;
        this.role = role;
    }
    
     public LoginUser(String username, String password, String country){
        this.username = username;
        this.password = password;
        this.country = country;
        this.role = "customer"; // default role adalah customer
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
     public String getRole(){
        return role;
    }
    
    public void setRole(String role){
        this.role = role;
    }
}
