package com.example.exhibitions.repository;

import com.example.exhibitions.entity.Ticket;
import com.example.exhibitions.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("select t from Ticket t where t.user.id = ?1")
    List<Ticket> selectUsersTickets(Long id);
//    @Query("select t from Ticket t where t.user.id = ?1")
    List<Ticket> findAllByUser(User user);
}
