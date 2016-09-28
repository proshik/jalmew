package ru.proshik.jalmew.auth.controller.dto;

/**
 * Created by proshik on 26.07.16.
 */
public class UserRequest {

    private String username;
    private String password;
    private String confirmPassword;

    public UserRequest() {
    }

    public UserRequest(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
