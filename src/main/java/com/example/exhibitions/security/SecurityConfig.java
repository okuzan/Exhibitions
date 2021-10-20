package com.example.exhibitions.security;

import com.example.exhibitions.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserService userService;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
//    @Resource(name = "userDetailsService")
//    private UserDetailsService userDetailsService;

//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        lci.setParamName("lang");
//        return lci;
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserService();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/shows/{id}/edit")
                .hasRole("ADMIN")
                .antMatchers("/api/shows/add")
                .hasRole("ADMIN")
                .antMatchers("/api/users")
                .hasRole("ADMIN")
                .antMatchers("/api/shows/{id}/buy")
                .hasRole("USER")
                .antMatchers("/api/shows/{id}")
                .permitAll()
                .antMatchers("/register**").anonymous()
                .antMatchers("/login**").anonymous()
                .antMatchers("/api/shows/").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome",true)
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }
}