package spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.dao.UserDao;
import spring.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Autowired // можно и не писать, Spring сам подцепит
    public UserServiceImpl(UserDao usersDao) {
        this.userDao = usersDao;
    }

    @Override
    @Transactional
    public void addUser(User user) {

        userDao.addUserToDatabase(user);

    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {

        return userDao.getAllUsersFromDatabase();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(long id) {
        return userDao.getUserByIdFromDatabase(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
         userDao.updateUserInDatabase(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {

        userDao.deleteUserFromDatabase(id);
    }
}
