package com.dotterbear.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dotterbear.model.User;

public interface UserRepository extends BaseRepository<User, Integer> {

	public Page<User> findAll(Pageable pagable);

	public List<User> findUserByEmailIn(List<String> email);

	public List<User> findUserByEmailInAndIdGreaterThanEqual(List<String> email, Integer id);

	public List<User> findUserByEmailAndName(String email, String name);

}
