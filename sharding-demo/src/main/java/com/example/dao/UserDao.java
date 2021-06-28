package com.example.dao;

import com.example.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hdf
 */
@Repository
public interface UserDao {
    public User findById(String userId);
    public List<User> find(User u);
    public int insert(User u);
    public int update(User u);
    public int delete(String userId);
}
