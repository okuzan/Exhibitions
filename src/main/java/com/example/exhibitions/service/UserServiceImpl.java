package com.example.exhibitions.service;

import com.example.exhibitions.data.UserDTO;
import com.example.exhibitions.entity.*;
import com.example.exhibitions.error.CustomErrorException;
import com.example.exhibitions.paging.Paged;
import com.example.exhibitions.paging.Paging;
import com.example.exhibitions.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HallRepository hallRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private ExhibitionRepository exhibitionRepo;

    @Transactional
    public void buyTicket(String username, Long showId) {
        Exhibition show = exhibitionRepo.findById(showId).orElseThrow(
                () -> new CustomErrorException(HttpStatus.valueOf(404), "Show is unavailable"));
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new CustomErrorException(HttpStatus.valueOf(404), "Couldn't detect acting user"));
        Double price = show.getPrice();
        BigDecimal balance = user.getBalance();
        if (balance.compareTo(BigDecimal.valueOf(price)) < 0)
            throw new CustomErrorException(HttpStatus.valueOf(500), "Not enough funds!");
        Ticket ticket = createTicket(user, show, price);
        ticketRepo.save(ticket);
        user.charge(ticket.getPrice());
        userRepository.save(user);
    }

    public Paged<User> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> postPage = userRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public Ticket createTicket(User user, Exhibition show, Double price) {
        Ticket ticket = new Ticket();
        ticket.setPrice(price);
        ticket.setUser(user);
        ticket.setExhibition(show);
        ticket.setStamp(LocalDateTime.now());
        return ticket;
    }

    public Page<User> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;
        List<User> users = userRepository.findAll();
        if (users.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), users.size());
    }

    private User mapDataToUser(final UserDTO userData) {
        User user = new User();
        user.setUsername(userData.getUsername());
        user.setEmail(userData.getEmail());
        user.setName(userData.getName());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(role));
        return user;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Override
    public User save(UserDTO dto) {
        User customerModel = mapDataToUser(dto);
        return userRepository.save(customerModel);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s).get();
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }
}
