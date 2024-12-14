package com.pras;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pras.account.User;
import com.pras.account.UserRepository;
import com.pras.account.UserService;
import com.pras.utils.Exceptions.AccountAccessException;

/**
 * Unit tests for @UserService
 * 
 * @author Prasanta
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Test
	public void testRegisterWithNewUser() {
		// Return user instance passed in save()
		when(userRepository.save(Mockito.any(User.class)))
        .thenAnswer(i -> i.getArguments()[0]);
		
		// No existing user
		when(userRepository.findByUserNameIgnoreCase(Mockito.any(String.class)))
        .thenReturn(new ArrayList<User>());
		
		User user = userService.register("Rito");
		assertThat(user).isNotNull();
		assertThat(user.getUserName()).isEqualTo("Rito");
		assertThat(user.getActive()).isEqualTo(true);
	}
	
	@Test
	public void testRegisterWithExistingUser() {
		// Return existing user
		User dummyUser = new User();
		dummyUser.setUserName("Rito");
		dummyUser.setActive(true);
		
		List<User> users = new ArrayList<User>();
		users.add(dummyUser);
		
		when(userRepository.findByUserNameIgnoreCase(Mockito.any(String.class)))
        .thenReturn(users);
		
		User user = userService.register("Rito");
		assertThat(user).isNotNull();
		assertThat(user.getUserName()).isEqualTo("Rito");
		assertThat(user.getActive()).isEqualTo(true);
	}
	
	@Test
	public void textInactiveUserAccessCheck() {
		User dummyUser = new User();
		dummyUser.setUserName("Rito");
		dummyUser.setActive(false); // Deactivated user
		
		assertThrows(AccountAccessException.class, () -> {
			userService.checkAccess(dummyUser);
		});
	}
}
