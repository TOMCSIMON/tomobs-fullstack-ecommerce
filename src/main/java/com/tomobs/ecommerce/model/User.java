package com.tomobs.ecommerce.model;

// THIS IS THE DATABASE MODEL(Table = users) FOR STORING USER DETAILS FROM THE SIGNUP PAGE.
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_name")
    private String userName;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private Role role;


}
