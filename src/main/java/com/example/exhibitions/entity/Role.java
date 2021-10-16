package com.example.exhibitions.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="roles")
@Getter @Setter @NoArgsConstructor @ToString
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable =  false)
    private Long id;
    private String name;

    public Role(String name) {
        this.name= name;
    }
}
