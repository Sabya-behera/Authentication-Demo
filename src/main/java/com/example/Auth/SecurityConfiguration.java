package com.example.Auth;


/*---------CUSTOMIZING THE SECURITY BY WRITTING OUR OWN AUTHORIZATION AND AUTHENTICATION----------*/


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity //(Contains COMPONENT and Configuration)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //to configure authentication this method is used (SETTING UP THE AUTHENTICATION)
        auth.userDetailsService(userDetailsService);
    }
    //this authentication must be connecting through JPA and getting the data from the database using mysql
    @Override
    protected void configure(HttpSecurity http) throws Exception   //it helps to allow which url to permit or private, filter etc(SETTING UP THE AUTHORIZATION)
    {
        http.authorizeRequests()//Authorize all mentioned requests
                        .antMatchers("/admin").hasRole("ADMIN")  //allow admin api if the user is allowed (access control)
                        .antMatchers("/user").hasAnyRole("ADMIN", "USER")  //allow user api if it had admin as well as user
                        .antMatchers("/").permitAll()  //permit everybody
                        .and().formLogin();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){return NoOpPasswordEncoder.getInstance();
    }//clear text password no encoding required
      //encoder.encode(String rawPassword) – converts a given plaintext password into an encoded password. And how it converts is up to the implementation. This part happens at the time when the password is stored in the DB. Usually when registering a user or changing the password.
    //encoder.matches(rawPassword, encodedPassword) – Used whenever login happens. Security context will load the encrypted password from the database and use this method to compare against the raw password
}