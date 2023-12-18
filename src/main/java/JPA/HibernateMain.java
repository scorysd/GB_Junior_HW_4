package JPA;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class HibernateMain {
    public static void main(String[] args) {
        final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml").buildSessionFactory();
        createData(sessionFactory);
        updateData(sessionFactory);
//        printData(sessionFactory);
        printQuery(sessionFactory);
    }

    public static void printData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            List<Book> books = session.createQuery("select b from Book b where b.id >= 1", Book.class)
                    .getResultList();
            System.out.print(books);
        }
    }

    public static void createData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Book> books = LongStream.rangeClosed(1, 10)
                    .mapToObj(it -> new Book())
                    .peek(it -> session.persist(it))
                    .collect(Collectors.toList());
            session.getTransaction().commit();
        }
    }

    public static void updateData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Book> books = session.createQuery("select b from Book b where b.id >= 1", Book.class)
                    .getResultList();
            books.stream()
                    .peek(it -> it.setName("Book #" + it.getId()))
                    .peek(it -> it.setAuthor("Author #" + (new Random().nextInt(4) + 1)))
                    .peek(it -> session.persist(it))
                    .forEach(System.out::println);
            session.getTransaction().commit();
        }
    }

    public static void printQuery(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            List<Book> books = session.createQuery("select b from Book b where b.author = 'Author #2'", Book.class)
                    .getResultList();
            System.out.print(books);
        }
    }
}
