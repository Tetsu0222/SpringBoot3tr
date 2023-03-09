package com.example.samplesecurityapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SampleSecurityConfig {
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http)
      throws Exception {
    http.csrf().disable();
    http.authorizeHttpRequests(authorize -> {
      authorize
        .requestMatchers("/").permitAll()
        .requestMatchers("/js/**").permitAll()
        .requestMatchers("/css/**").permitAll()
        .requestMatchers("/img/**").permitAll()
        .anyRequest().authenticated();
    });
    http.formLogin(form -> {
      form.defaultSuccessUrl("/secret");
    });
    return http.build();
   }

  @Bean
  public InMemoryUserDetailsManager userDetailsManager(){
    String username = "user";
    String password = "pass";

    UserDetails user = User.withUsername(username)
      .password(
        PasswordEncoderFactories
          .createDelegatingPasswordEncoder()
          .encode(password))
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }
}