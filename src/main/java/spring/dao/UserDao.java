package spring.dao;

import spring.model.User;

import java.util.List;

public interface UserDao {
    public void addUserToDatabase(User user);

    public List<User> getAllUsersFromDatabase();

    public void deleteUserFromDatabase(long id);

    public User getUserByIdFromDatabase(long id);

     public void updateUserInDatabase(User user);
}

