package database.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@DatabaseTable
public class Customer extends User{
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String surname;
    @DatabaseField
    private String login;
    @DatabaseField
    private String password;
    @DatabaseField
    private String email;
    @DatabaseField
    private String phoneNumber;
    @DatabaseField
    private String token;
    @DatabaseField(dataType = DataType.SERIALIZABLE)@JsonIgnore
    private LocalDate tokenExpireTime;

    public Customer() {

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
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonDeserialize
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

    public LocalDate getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(LocalDate tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(surname, customer.surname) &&
                Objects.equals(login, customer.login) &&
                Objects.equals(password, customer.password) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(phoneNumber, customer.phoneNumber) &&
                Objects.equals(token, customer.token) &&
                Objects.equals(tokenExpireTime, customer.tokenExpireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, surname, login, password, email, phoneNumber, token, tokenExpireTime);
    }
}
