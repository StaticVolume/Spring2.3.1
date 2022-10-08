package spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spring.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final  EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }


    @Override
    @Transactional
    public void addUserToDatabase(User user) {
        entityManager.persist(user);
    }

    @Override
   @Transactional(readOnly = true)
    public List<User> getAllUsersFromDatabase() {
        TypedQuery<User> query = entityManager.createQuery("FROM User", User.class);
        return query.getResultList();

    }

    @Override
    @Transactional
    public void deleteUserFromDatabase(long id) {
        entityManager.remove(getUserByIdFromDatabase(id));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByIdFromDatabase(long id) {

        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public User updateUserInDatabase(long id, User user) {
        User buffer = getUserByIdFromDatabase(id);
        buffer.setName(user.getName());
        buffer.setSurname(user.getSurname());
        buffer.setAge(user.getAge());
        buffer.setEmail(user.getEmail());
        return buffer;
    }
}
