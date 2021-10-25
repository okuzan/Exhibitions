package com.example.exhibitions.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String email;
    private String username;
    private String password;
    //    @Column(precision = 8, scale = 2)
    @Column(columnDefinition = "Numeric(10,2) default '0.00'")
    private BigDecimal balance;
    private boolean enabled = true;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId")
//    private Collection<Ticket> tickets;

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String name, String username, String email, String password, Collection<Role> roles) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void charge(Double price) {
        this.balance = this.balance.subtract(BigDecimal.valueOf(price));
    }

    public void changeStatus() {
        this.setEnabled(!this.enabled);
    }
}