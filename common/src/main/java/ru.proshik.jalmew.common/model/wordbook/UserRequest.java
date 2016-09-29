package ru.proshik.jalmew.common.model.wordbook;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by proshik on 26.07.16.
 */
public class UserRequest {

    @NotNull
    @Length(min = 3, max = 20)
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 6, max = 40)
    private String password;

    @NotNull
    @Length(min = 6, max = 40)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
