package ru.naumen.practice;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import jakarta.persistence.Query;
import ru.naumen.practice.entities.Student;

/**
 * @author vmikolyuk
 * @since 26.05.2023
 */
public class HibernateExample
{
    public static void main(String[] args)
    {
        String queryString = "FROM Student"
                + " WHERE name LIKE '%Ivan%'"
                + " ORDER BY id";
        Configuration config = new Configuration();
        config.addAnnotatedClass(Student.class);

        SessionFactory sessionFactory = config.buildSessionFactory();

        Session session = sessionFactory.openSession();

        Query query = session.createQuery(queryString, Student.class);

        List<Student> students = query.getResultList();
        students.forEach(student ->
                System.out.println(student.getName()));
        session.close();
    }
}
