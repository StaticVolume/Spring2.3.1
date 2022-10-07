package spring.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spring.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }


    @Override
    @Transactional
    public void addUserToDatabase(User user) {
    Session session = entityManager.unwrap(Session.class);
    session.save(user);
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
        //entityManager.unwrap(Session.class).delete(getUserByIdFromDatabase(id));
        Session session = entityManager.unwrap(Session.class);
        session.beginTransaction();
        session.delete(getUserByIdFromDatabase(id));
        session.getTransaction().commit();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByIdFromDatabase(long id) {
        return entityManager.unwrap(Session.class).get(User.class,id);
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