package com.klasevich;

import com.klasevich.converter.BirthdayConverter;
import com.klasevich.entity.Birthday;
import com.klasevich.entity.Role;
import com.klasevich.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Convert;
import javax.persistence.Converter;
import java.sql.SQLException;
import java.time.LocalDate;

@Converter(autoApply = true)
public class HibernateRunner {
    public static void main(String[] args) throws SQLException {
//        BlockingQueue<Connection>pool = null;
//        Connection connection = pool.take();
//        SessionFactory

//        Connection connection = DriverManager
//                .getConnection("db.url", "db.username", "db.password");
//        Session

        Configuration configuration = new Configuration();
//        configuration.addAnnotatedClass(User.class);
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("ivan1@gmail.com")
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                    .role(Role.ADMIN)
                    .build();
            session.save(user);

            session.getTransaction().commit();
        }
    }
}
