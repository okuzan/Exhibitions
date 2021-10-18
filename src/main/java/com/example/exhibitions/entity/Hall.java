package com.example.exhibitions.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="halls")
@Getter @Setter @NoArgsConstructor @ToString
public class Hall {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable =  false)
    private Long id;
    private Integer number;

}
