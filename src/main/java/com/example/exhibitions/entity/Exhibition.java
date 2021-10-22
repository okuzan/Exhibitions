package com.example.exhibitions.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name="exhibitions")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable =  false)
    private Long id;
    private String name;
    private Double price;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "exhibitions_halls",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "hall_id")
    )
    private Collection<Hall> halls;

    public Exhibition(String name) {
        this.name= name;
    }
}
