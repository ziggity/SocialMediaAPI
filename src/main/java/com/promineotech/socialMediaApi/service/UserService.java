package com.promineotech.socialMediaApi.service;

import com.promineotech.socialMediaApi.entity.User;
import com.promineotech.socialMediaApi.repository.UserRepository;
import com.promineotech.socialMediaApi.view.Following;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	public User createUser(User user) {
		return repo.save(user);
	}
	
	public User login(User user) throws Exception{
		User foundUser = repo.findByUsername(user.getUsername());
		if(foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
			return foundUser;
		}else {
			throw new Exception("Invalid username or password");
		}
	}
	
	public Following follow(Long userId, Long followId) throws Exception{
		User user = repo.findOne(userId);
		User follow = repo.findOne(followId);
		if (user == null || follow == null) {
			throw new Exception("user does not exist");
		}
		user.getFollowing().add(follow);
		repo.save(user);
		return new Following(user);
	}
	public User updateProfilePicture(Long userId, String url) throws Exception{
		User user = repo.findOne(userId);
		if(user == null) {
			throw new Exception("user does not exist");
		}
		user.setProfilePictureUrl(url);
		return repo.save(user);
	}

	public Following getFollowedUsers(Long id) throws Exception {
		return follow(null, null);
	}
}
