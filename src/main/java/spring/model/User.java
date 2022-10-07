package spring.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    @NotEmpty(message = "name must be not null")
    @Size(min = 2, max = 128, message = "name must be greater 2 characters and less 128 characters")
    private String name;

    @Column(name = "second_name")
    @NotEmpty(message = "surname must be not null")
    @Size(min = 2, max = 128, message = "surname must be greater 2 characters and less 128 characters")
    private String surname;
    @Column(name = "age")
    @Min(value = 5, message = "age should be greater than 5 years")
    @Max(value = 100, message = "age should be less then 100 years ")
    private int age;
    @Column(name = "email")
    @NotEmpty(message = "email must be not null")
    @Email(message = "email is not correct")
    private String email;
    public User() {

    }


    public User(String name, String surname, int age, String email) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
