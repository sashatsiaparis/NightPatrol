package com.example.nightpatrol.api.model;

public class Login {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Login(String iemail, String ipassword) {
        this.email = iemail;
        this.password = ipassword;
    }

    public Login() {
    }

    private String email;
    private String password;

}
