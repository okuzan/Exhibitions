package com.example.exhibitions.repository;

import com.example.exhibitions.entity.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long>, JpaSpecificationExecutor<Exhibition> {
    Page<Exhibition> getAllByStartDateAfterAndEndDateBefore(LocalDateTime start, LocalDateTime end, Pageable pageable);
//    Exhibition getTopByPrice();
    Exhibition findFirstByPrice(Boolean required);

    @Query(value = "SELECT max(price) FROM Exhibition")
    Double getMaxPrice();
    @Query(value = "SELECT min(price) FROM Exhibition")
    Double getMinPrice();
}
