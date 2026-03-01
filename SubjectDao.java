package com.vedkdu.smartpresence.dao;

import java.util.List;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vedkdu.smartpresence.entity.Subject;

@Repository
public class SubjectDao {

    @Autowired
    private SessionFactory factory;

    public Subject getSubjectById(long subjectId) {
        try (Session session = factory.openSession()) {
            return session.get(Subject.class, subjectId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Subject> getAllSubjects() {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
            query.from(Subject.class);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Subject createSubject(Subject subject) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            // Check for duplicate subject name
            if (isSubjectNameUnique(subject.getName(), session)) {
                transaction = session.beginTransaction();
                session.save(subject);
                transaction.commit();
                return subject;
            }
            return null; // Subject with the same name exists
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public Subject updateSubject(Subject subjectDetails) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.update(subjectDetails);
            transaction.commit();
            return subjectDetails;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public String deleteSubject(long id) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            Subject subject = session.get(Subject.class, id);
            if (subject != null) {
                transaction = session.beginTransaction();
                session.delete(subject);
                transaction.commit();
                return "Deleted";
            }
            return "Subject not found";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    // Helper Method to Check for Duplicate Subject Name
    private boolean isSubjectNameUnique(String name, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
        Root<Subject> root = query.from(Subject.class);
        query.select(root).where(builder.equal(root.get("name"), name));
        List<Subject> existingSubjects = session.createQuery(query).getResultList();
        return existingSubjects.isEmpty();
    }
}
