package com.pras.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	public List<User> findByUserNameIgnoreCase(String userName);
}
