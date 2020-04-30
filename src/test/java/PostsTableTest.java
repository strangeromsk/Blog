import junit.framework.TestCase;
import main.model.Post;
import main.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Date;


public class PostsTableTest extends TestCase {

    public void test() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        for (int i = 0; i < 5000; i++){
            User user = new User();
            user.setPassword("234324" + i);
            user.setName("Ivan" + i);
            user.setEmail(i + "Ivan@mail.com");
            user.setRegTime(new Date());
            user.setId(i);

            Post post = new Post();
            post.setId(1L + i);
            post.setIsActive(1);
            post.setModeratorId(1);
            post.setStatus(Post.Status.ACCEPTED);
            post.setText("bla" + i);
            post.setTitle("ewr" + i);
            post.setViewCount(22);
            post.setTime(new Date());
            post.setUser(user);

            session.persist(post);
        }
        session.getTransaction().commit();
    }
}
