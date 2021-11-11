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
    private TicketRepository ticketRepo;

    @Autowired
    private ExhibitionRepository exhibitionRepo;

    private final static String  DEFAULT_ROLE = "ROLE_USER";

    /**
     * Method for buying ticket for specified show and user. Returns 404 for invalid parameters,
     * 500 if user doesn't have enough funds. New Ticket is created and saved, User balance is updated.
     * Operation is transactional - user won't be charged, if something goes south.
     * @param username - username of buyer
     * @param showId - id of desired show
     *
     */
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

    /**
     * Returns paginated part of user list, retrieved from user repository based on page number & page size.
     * @param pageNumber - page ordinal number
     * @param size - how many entries are on one page
     * @return paginated part of user list
     */
    public Paged<User> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> postPage = userRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    /**
     * Creates Ticket object from User, Exhibition, and price.
     * Price is passed, not retrieved inside,
     * so that sum that was charged and one that was indicated in ticket were always the same
     * @param user - buyer
     * @param show - target show
     * @param price - price at the moment of buying
     * @return Ticket entity
     */
    public Ticket createTicket(User user, Exhibition show, Double price) {
        Ticket ticket = new Ticket();
        ticket.setPrice(price);
        ticket.setUser(user);
        ticket.setExhibition(show);
        ticket.setStamp(LocalDateTime.now());
        return ticket;
    }

    /**
     * Retrieves and transforms data from DTO, usually submitted with the registration form
     * and applies necessary operations, such as password encoding, assigning default ROLE (user)
     * @param userData - User DTO object
     * @return - User Entity Object
     */
    private User mapDataToUser(final UserDTO userData) {
        User user = new User();
        user.setUsername(userData.getUsername());
        user.setEmail(userData.getEmail());
        user.setName(userData.getName());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));
        Role role = roleRepository.findByName(DEFAULT_ROLE);
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
        User user = userRepository.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }
}
