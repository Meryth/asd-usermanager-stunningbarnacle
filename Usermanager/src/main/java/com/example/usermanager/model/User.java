package com.example.usermanager.model;

import javax.persistence.*;

@Entity
@Table(name = "Stunningbarnacle_Users")
public class User {
    //Table columns
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "firstname", nullable = false, length = 255)
    private String firstname;

    @Column(name = "surname", nullable = false, length = 255)
    private String surname;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    //Constructors
    public User(){}

    public User(String firstname, String surname, String username, String password) {
        this.firstname = firstname;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public User(Integer id, String firstname, String surname, String username, String password) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    //Getter and Setter

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
