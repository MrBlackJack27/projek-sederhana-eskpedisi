package login;

public class LoginUser {
    private String username;
    private String password;
    
    public LoginUser(String username, String password){
        this.username = username;
        this.password = password;
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
}
