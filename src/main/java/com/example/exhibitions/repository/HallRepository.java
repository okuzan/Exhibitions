package com.example.exhibitions.repository;

import com.example.exhibitions.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall,Long> {
    Hall getByNumber(Integer number);
}
