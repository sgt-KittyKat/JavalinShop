package database.models;

import com.j256.ormlite.field.DatabaseField;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private String token;
    private Integer id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    private String phoneNumber;
    private Department department;
    private LocalDate tokenExpireTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDate getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(LocalDate tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(token, user.token) &&
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                department == user.department &&
                Objects.equals(tokenExpireTime, user.tokenExpireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, id, name, surname, login, password, email, phoneNumber, department, tokenExpireTime);
    }
}
