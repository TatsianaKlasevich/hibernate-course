package com.klasevich;

import com.klasevich.entity.Birthday;
import com.klasevich.entity.Company;
import com.klasevich.entity.PersonalInfo;
import com.klasevich.entity.User;
import com.klasevich.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Company company = Company.builder()
                .name("Google")
                .build();

        User user = User.builder()
                .username("petr2@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .lastname("Petrov")
                        .firstname("Petr")
                        .birthDate(new Birthday(LocalDate.of(2000, 1, 2)))
                        .build())
                .company(company)
                .build();


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();

                User user1 = session1.get(User.class, 1L);
//
//                session1.save(company);
//                session1.save(user);

                session1.getTransaction().commit();
            }
        }
    }
}
