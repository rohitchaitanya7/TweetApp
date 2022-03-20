package com.iiht.tweetapp.seviceimpl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iiht.tweetapp.exception.UnauthorizedException;
import com.iiht.tweetapp.model.UserData;
import com.iiht.tweetapp.repository.UserRepository;

/**Service class*/
@Service
public class CustomerDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userdao;

	/**
	 * @param String
	 * @return User 
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String uname) {
		
		try
		{
			Optional<UserData> user=userdao.findById(uname);
			if(user.isPresent()) {
				return new User(user.get().getUserName(), user.get().getPassword(), new ArrayList<>());
			}
			else {
				throw new UsernameNotFoundException("User id not found");
			}
		}
		catch (Exception e) {
			
			throw new UnauthorizedException("Username Not Found Exception");
		}	
		
		
	}

}
