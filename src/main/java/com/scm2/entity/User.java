package com.scm2.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.scm2.enums.Provider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phonenumber;
    private String password;
    @Column(length = 10000)
    private String about;
    @Column(length = 1000)
    private String profilePicUrl;

    // information
    @Column(nullable = false)
    private boolean enabled = false;
    @Column(nullable = false)
    private boolean emailVerified = false;
    @Column(nullable = false)
    private boolean phoneVerified = false;
    private String emailToken;

    // provider
    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.SELF;
    private String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<Contact>();

    // roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.setUser(this);

    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
        contact.setUser(null);
    }

}
