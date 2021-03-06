package com.klasevich;

import com.klasevich.entity.Chat;
import com.klasevich.entity.Company;
import com.klasevich.entity.User;
import com.klasevich.entity.UserChat;
import com.klasevich.util.HibernateTestUtil;
import com.klasevich.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class HibernateRunnerTest {

    @Test
    void checkHql() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //    HQL / JPQL
            //select * from users where u.firstname = 'Ivan'
            String name = "Ivan";
            List<User> result = session.createNamedQuery(
//                            "select u from User u where u. personalInfo.firstname = ?1",
                            "findUserByName", User.class)
//                    .setParameter(1, name)
                    .setParameter("firstname", name)
                    .setParameter("companyName", "Google")
                    .setFlushMode(FlushMode.COMMIT)
                    .setHint(QueryHints.HINT_FETCH_SIZE, "50")
                    .list();

            int countRows = session.createQuery("update User u set u.role = 'ADMIN'")
                    .executeUpdate();

            session.createNativeQuery("select * from users where u.firstname = 'Ivan'", User.class);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkH2() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company google = Company.builder()
                    .name("Google")
                    .build();
            session.save(google);

//            Programmer programmer = Programmer.builder()
//                    .username("ivan@gmail.com")
////                    .language(Language.JAVA)
//                    .company(google)
//                    .build();
//            session.save(programmer);
//
//            Manager manager = Manager.builder()
//                    .username("sveta@gmail.com")
////                    .projectName("Starter")
//                    .company(google)
//                    .build();
//            session.save(manager);
//            session.flush();
//
//            session.clear();
//            Programmer programmer1 = session.get(Programmer.class, 1L);
//            User manager1 = session.get(User.class, 2L);
//            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void localeInfo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
//            company.getLocales().add(LocaleInfo.of("ru", "???????????????? ???? ??????????????"));
//            company.getLocales().add(LocaleInfo.of("en", "English description"));
//            System.out.println(company.getLocales());
            company.getUsers().forEach((k, v) -> System.out.println(v));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToMany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 6L);

            Chat chat = session.get(Chat.class, 1L);

            UserChat userChat = UserChat.builder()
//                    .createdAt(null)
//                    .createdBy(user.getUsername())
                    .build();
            userChat.setUser(user);
            userChat.setChat(chat);

            session.save(userChat);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 1L);

//            User user = User.builder()
//                    .username("test@gmai.com")
//                    .build();
//            Profile profile = Profile.builder()
//                    .language("ru")
//                    .street("Kolasa 18")
//                    .build();
//            profile.setUser(user);
//            session.save(user);

//            profile.setUser(user);
////            session.save(profile);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrphanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.getReference(Company.class, 2);
//            company.getUsers().removeIf(user -> user.getId().equals(3L));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company = null;
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.getReference(Company.class, 1);

            session.getTransaction().commit();
        }
//        Set<User> users = company.getUsers();
//        System.out.println(users.size());
    }

    @Test
    void deleteCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 3);
        session.delete(company);


        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Facebook")
                .build();

//        User user = User.builder()
//                .username("sveta@gmail.com")
//                .build();
//        user.setCompany(company);
//        company.getUsers().add(user);
//        company.addUser(user);

        session.save(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);
        Hibernate.initialize(company.getUsers());
        System.out.println(company.getUsers());

        session.getTransaction().commit();
    }

    @Test
    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("lastname");
        resultSet.getString("username");

        Class<User> clazz = User.class;
        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));
    }


    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
//        User user = User.builder()
//                .build();

        String sql = """
                insert
                into
                %s
                (%s)
                values
                (%s)
                """;

//        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
//                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
//                .orElse(user.getClass().getName());
//
//        Field[] declaredFields = user.getClass().getDeclaredFields();
//
//        String columnNames = Arrays.stream(user.getClass().getDeclaredFields())
//                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
//                        .map(Column::name)
//                        .orElse(field.getName()))
//                .collect(joining(", "));
//
//        String columnValues = Arrays.stream(user.getClass().getDeclaredFields())
//                .map(field -> "?")
//                .collect(joining(", "));
//
//        System.out.println(sql.formatted(tableName, columnNames, columnValues));
//
//        Connection connection = null;
//        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
//        for (Field declaredField : declaredFields) {
//            declaredField.setAccessible(true);
//            preparedStatement.setObject(1, declaredField.get(user));
//        }
    }
}