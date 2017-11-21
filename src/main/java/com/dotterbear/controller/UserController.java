package com.dotterbear.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dotterbear.dao.UserDao;
import com.dotterbear.model.User;
import com.dotterbear.model.UserItem;
import com.dotterbear.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserDao userDao;

	@GetMapping(path = "/test")
	@Cacheable("testCache")
	public @ResponseBody String cached() {
		return "cached";
	}

	@GetMapping(path = "/add/{name}{email}")
	public @ResponseBody String addNewUser(@MatrixVariable String name, @MatrixVariable String email) throws Exception {
		List<UserItem> userItems = new ArrayList<>();
		User userB = new User();
		userB.setName(name);
		userB.setEmail(email);
		for (String s : new String[] { "default item", "default item 2", "default item 3" }) {
			UserItem userItem = new UserItem();
			userItem.setName(s);
			userItems.add(userItem);
		}
		userB.setUserItem(userItems);
		userDao.save(userB);
		// userDao.saveException(userB);
		return "Saved";
	}

	@GetMapping(path = "/addall")
	public @ResponseBody String addUsers() {
		List<User> users = new ArrayList<>();
		User user = null;
		String[] names = { "A", "B", "C" };
		String[] emails = { "A@demo.com", "B@demo.com", "C@demo.com" };
		for (int i = 0; i < 1; i++) {
			user = new User();
			user.setName(names[i]);
			user.setEmail(emails[i]);
			users.add(user);
		}
		userRepository.batchSave(users);
		return "Saved";
	}

	// @GetMapping(path = "/add/{users}")
	// public @ResponseBody String addNewUsers(List<UserB> users) {
	// userRepository.batchSave(users);
	// return "Saved";
	// }

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		userRepository.findAll(new PageRequest(1, 20));
		return userRepository.findAll();
	}

	@GetMapping(path = "/delete")
	public @ResponseBody String deleteUser(@RequestParam Integer id) {
		userRepository.delete(id);
		return "done";
	}

	@GetMapping(path = "/exists")
	public @ResponseBody Boolean existsUser(@RequestParam Integer id) {
		return userRepository.exists(id);
	}

	@GetMapping(path = "/findone")
	public @ResponseBody User findOneUser(@RequestParam Integer id) {
		return userRepository.findOne(id);
	}

	@GetMapping(path = "/count")
	public @ResponseBody long countUser() {
		return userRepository.count();
	}

	@GetMapping(path = "/findbyemail")
	public @ResponseBody List<User> findUserByEmailIn(@RequestParam List<String> email) {
		return userRepository.findUserByEmailIn(email);
	}

	@GetMapping(path = "/findbyemailnname")
	public @ResponseBody List<User> findUserByEmailAndName(@RequestParam String email, String name) {
		return userRepository.findUserByEmailAndName(email, name);
	}

	@GetMapping(path = "/findt")
	public @ResponseBody List<User> findUserByEmailInAndIdGreaterThanEqual(@RequestParam List<String> email, @RequestParam Integer id) {
		return userRepository.findUserByEmailInAndIdGreaterThanEqual(email, id);
	}

}
