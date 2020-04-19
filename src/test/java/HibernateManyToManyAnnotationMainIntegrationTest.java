//import main.model.Post;
//import main.model.Tag;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class HibernateManyToManyAnnotationMainIntegrationTest {
//    private static SessionFactory sessionFactory;
//    private Session session;
//
//    // ...
//
//    @Test
//    public void givenData_whenInsert_thenCreatesMtoMrelationship() {
//        String[] postData = { "Peter Oven", "Allan Norman" };
//        String[] tagData = { "IT Project", "Networking Project" };
//        Set<Tag> tags = new HashSet<>();
//
//        for (String tag : tagData) {
//            tags.add(new Tag(tag));
//        }
//
//        for (String post : postData) {
//            Post post1 = new Post(post.split(" ")[0],
//                    post.split(" ")[1]);
//
//            assertEquals(0, post1.getTags().size());
//            post1.setTags(tags);
//            session.persist(post1);
//
//            assertNotNull(post1);
//        }
//    }
//
//    @Test
//    public void givenSession_whenRead_thenReturnsMtoMdata() {
//        @SuppressWarnings("unchecked")
//        List<Post> postsList = session.createQuery("FROM posts")
//                .list();
//
//        assertNotNull(postsList);
//
//        for(Post post : postsList) {
//            assertNotNull(post.getTags());
//        }
//    }
//
//}
