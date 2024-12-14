package com.pras.account;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pras.utils.Exceptions.AccountAccessException;

/**
 * User service to manage registration and access check
 * 
 * @author Prasanta
 *
 */
@Service
public class UserService {

	@Autowired
	UserRepository usersRepository;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Register or return existing instance
	 * 
	 * @param userName
	 * @return
	 */
	public User register(String userName) {
		User user = findUser(userName);
		
		if(user == null) {
			// Register new user
			logger.info("Register new user {}", userName);
			user = new User();
			user.setUserName(userName);
			user.setActive(true);
			return usersRepository.save(user);
		}
		
		// Existing user
		logger.info("{} is existing user", userName);
		return user;
	}
	
	/**
	 * Find User account instance
	 * 
	 * @param userName
	 * @return
	 */
	public User findUser(String userName) {
		List<User> users = usersRepository.findByUserNameIgnoreCase(userName);
		if(users == null || users.size() == 0)
			return null;
		return users.get(0);
	}
	
	/**
	 * Check account active state
	 * 
	 * @param user
	 */
	public void checkAccess(User user) {
		if(user == null)
			return;
		
		if(user.getActive() == null || !user.getActive())
			throw new AccountAccessException("User "+ user.getUserName() +" is not allowed");
	}
}
