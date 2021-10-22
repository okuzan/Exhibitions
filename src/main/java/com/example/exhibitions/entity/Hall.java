package com.example.exhibitions.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="halls")
@Getter @Setter @NoArgsConstructor @ToString
@EqualsAndHashCode
public class Hall {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable =  false)
    private Long id;
    private Integer number;

}
