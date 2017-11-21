package com.dotterbear.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.dotterbear.dao.UserDao;
import com.dotterbear.model.User;
import com.dotterbear.model.UserItem;
import com.dotterbear.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserDao userDao;

	static boolean init = false;

	@Before
	public void init() {
		if (init)
			return;
		List<UserItem> userItems = new ArrayList<>();
		List<User> userBs = new ArrayList<>();
		String[] names = new String[] { "AAA", "BBB", "CCC" };
		String[] emails = new String[] { "AAA@demo.com", "BBB@demo.com", "CCC@demo.com" };
		for (String s : new String[] { "default item", "default item 2", "default item 3" }) {
			UserItem userItem = new UserItem();
			userItem.setName(s);
			userItems.add(userItem);
		}
		User userB;
		for (int i = 0; i < 16; i++) {
			userB = new User();
			userB.setName(names[i % 3]);
			userB.setEmail(emails[i % 3]);
			userB.setUserItem(userItems);
			userBs.add(userB);
		}
		userRepository.batchSave(userBs);
		init = true;
	}

	@Test
	public void evictTest() {
		userDao.findAll(1);
		userDao.findAll(1);
		userDao.evictFindAllByPage(1);
		userDao.findAll(1);
		userDao.findAll(1);
		userDao.findAll(2);
		userDao.evictFindAllByPage(1);
		userDao.findAll(2);
	}

	@Test
	public void findAllCacheTest() {
		List<User> page1 = userDao.findAll(1);
		List<User> page2 = userDao.findAll(2);

		assertEquals(page2.toString(), userRepository.findAll(new PageRequest(1, 10)).getContent().toString());
		assertEquals(page2.toString(), userRepository.findAll(new PageRequest(1, 10)).getContent().toString());
		assertEquals(page1.toString(), userRepository.findAll(new PageRequest(0, 10)).getContent().toString());
		assertEquals(page1.toString(), userRepository.findAll(new PageRequest(0, 10)).getContent().toString());
		assertEquals(page2.toString(), userRepository.findAll(new PageRequest(1, 10)).getContent().toString());
		System.out.println("\n\n\n\n\n\n\n\n\n");
		assertEquals(page1.toString(), userDao.findAll(1).toString());
		assertEquals(page2.toString(), userDao.findAll(2).toString());
	}

	@Test
	public void findAll() {
		assertEquals(userRepository.findAll(new PageRequest(1, 10)).getContent().toString(), userDao.findAll(2).toString());
	}

	@Test
	public void findAllOrderByName() {
		assertEquals(userRepository.findAll(new PageRequest(0, 10, new Sort("name"))).getContent().toString(), userDao.findAllOrderByName(1).toString());
	}

	@Test
	public void findUserByEmailAndName() {
		String email = "AAA@demo.com";
		String name = "AAA";
		assertEquals(userRepository.findUserByEmailAndName(email, name).toString(), userDao.findUserByEmailAndName(email, name).toString());
	}

	@Test
	public void findEmailIn() {
		List<String> emails = Arrays.asList(new String[] { "AAA@demo.com", "BBB@demo.com" });
		assertEquals(userRepository.findUserByEmailIn(emails).toString(), userDao.findUserByEmailIn(emails).toString());
	}
}
