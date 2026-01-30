package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    UserDetails findByEmail(String email);
    UserDetails findByMobileNumber(String mobileNumber);
}
