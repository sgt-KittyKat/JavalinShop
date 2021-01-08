package database.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable
public class Employee extends User {
    @DatabaseField
    private String token;
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
    private Department department;
    @DatabaseField(dataType = DataType.SERIALIZABLE)@JsonIgnore
    private LocalDate tokenExpireTime;

    public Employee() {

    }

    public LocalDate getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(LocalDate tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
        Employee employee = (Employee) o;
        return Objects.equals(token, employee.token) &&
                Objects.equals(id, employee.id) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(surname, employee.surname) &&
                Objects.equals(login, employee.login) &&
                Objects.equals(password, employee.password) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(phoneNumber, employee.phoneNumber) &&
                department == employee.department &&
                Objects.equals(tokenExpireTime, employee.tokenExpireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token, id, name, surname, login, password, email, phoneNumber, department, tokenExpireTime);
    }
}
