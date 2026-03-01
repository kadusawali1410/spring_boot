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

import com.vedkdu.smartpresence.entity.AttendanceRecord;

@Repository
public class AttendanceRecordDao {
    
    @Autowired
    private SessionFactory factory;

    public List<AttendanceRecord> getAllAttendanceRecords() {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AttendanceRecord> query = builder.createQuery(AttendanceRecord.class);
            Root<AttendanceRecord> root = query.from(AttendanceRecord.class);
            query.select(root);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<AttendanceRecord> getAllAttendanceRecords(String date, long subjectId) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AttendanceRecord> query = builder.createQuery(AttendanceRecord.class);
            Root<AttendanceRecord> root = query.from(AttendanceRecord.class);

            Predicate datePredicate = builder.equal(root.get("date"), date);
            Predicate subjectPredicate = builder.equal(root.get("subject").get("id"), subjectId);
            query.where(builder.and(datePredicate, subjectPredicate));

            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AttendanceRecord saveAttendance(AttendanceRecord attendanceRecord) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.save(attendanceRecord);
            transaction.commit();
            return attendanceRecord;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }
}
