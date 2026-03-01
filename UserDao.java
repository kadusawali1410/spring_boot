package com.vedkdu.smartpresence.dao;

import java.util.List;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vedkdu.smartpresence.entity.User;
import com.vedkdu.smartpresence.model.LoginRequest;

@Repository
public class UserDao {

    @Autowired
    private SessionFactory factory;

    public User loginUser(LoginRequest request) {
        try (Session session = factory.openSession()) {
            User user = session.get(User.class, request.getUsername());
            if (user != null && user.getPassword().equals(request.getPassword())) {
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteUserById(String username) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            User user = session.get(User.class, username);
            if (user != null) {
                transaction = session.beginTransaction();
                session.delete(user);
                transaction.commit();
                return "Deleted";
            }
            return "User not found";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public User updateUser(User user) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUser() {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            query.from(User.class);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserByName(String username) {
        try (Session session = factory.openSession()) {
            return session.get(User.class, username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User registerUser(User user) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            if (session.get(User.class, user.getUsername()) == null) {
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();
                return user;
            }
            return null; // User already exists
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllAdmins() {
        return getUsersByRole("admin");
    }

    public List<User> getAllFaculties() {
        return getUsersByRole("faculty");
    }

    private List<User> getUsersByRole(String role) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            Predicate rolePredicate = builder.equal(root.get("role"), role);
            query.where(rolePredicate);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
