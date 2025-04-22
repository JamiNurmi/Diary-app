package com.example.diary.domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "app_users") // Selkeämpi taulunnimi
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50) // Lisätty pituusrajoitus
    private String username;

    @Column(nullable = false, length = 100) // BCrypt-hash on pitkä
    private String password;

    @Column(nullable = false, unique = true, length = 100) // Sähköpostin pituusrajoitus
    private String email;

    @ElementCollection(fetch = FetchType.EAGER) // Haetaan aina roolit
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    // Oletuskonstruktori JPA:lle
    public AppUser() {
    }

    // Konstruktori ilman roolia (oletusarvoisesti USER)
    public AppUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles.add("USER"); // Oletusrooli
    }

    // Konstruktori yhdellä rollilla
    public AppUser(String username, String password, String email, String role) {
        this(username, password, email);
        this.roles.add(role);
    }

    // Konstruktori useilla rooleilla
    public AppUser(String username, String password, String email, Set<String> roles) {
        this(username, password, email);
        this.roles.addAll(roles);
    }

    // UserDetails-metodit
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getterit ja setterit
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
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

    public Set<String> getRoles() {
        return new HashSet<>(roles); // Palauta kopio
    }

    public void setRoles(Set<String> roles) {
        this.roles = new HashSet<>(roles); // Varmista ettei null
    }

    // Apumetodit roolien käsittelyyn
    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }

    public boolean hasRole(String role) {
        return this.roles.contains(role);
    }
}