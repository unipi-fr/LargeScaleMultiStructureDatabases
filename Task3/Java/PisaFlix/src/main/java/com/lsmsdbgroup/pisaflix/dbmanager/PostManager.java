package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Post;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.PostManagerDatabaseInterface;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import static org.neo4j.driver.v1.Values.parameters;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

public class PostManager implements PostManagerDatabaseInterface{

    private static PostManager postManager;
    private final Driver driver;
    
    public static PostManager getIstance() {
        if (postManager == null) {
            postManager = new PostManager();
        }
        
        return postManager;
    }

    private PostManager() {
        driver = DBManager.getDB();
    }
    
    private static Post getPostFromRecord(Record record0, Record record1){
        Node post = record0.get("p").asNode();
        String text = post.get("Text").asString();
        
        Relationship relationship = record0.get("r").asRelationship();
        LocalDateTime timestamp;
        Date timestampDate;
        if(relationship.hasType("RELATIVE"))
        {
            timestamp = relationship.get("Timestamp").asLocalDateTime();
            timestampDate = Date.from( timestamp.atZone( ZoneId.systemDefault()).toInstant());
            Long userId = record0.get("n").asNode().id();
            User user = DBManager.userManager.getById(userId);
        }
        
        Long userId = record0.get("n").asNode().id();
        User user = DBManager.userManager.getById(userId);
        
        record1.get("");
        
            
        return null;
    }
    
    @Override
    public Post getById(Long idPost)
    {
        Post post = null;
        
        try ( Session session = driver.session() )
        {
            post = session.readTransaction((Transaction tx) -> getPost(tx, idPost));
        }
        
        return post;
    }

    private static Post getPost(Transaction tx, Long idPost)
    {
        Post post = null;
        
        StatementResult result = tx.run("MATCH (p:Post) where ID(p) = 5380 " +
                                        "MATCH (p)-[r]-(n) RETURN p, r, n");
        while (result.hasNext())
        {
            Record record0 = result.next();
            Record record1 = result.next();
            
            getPostFromRecord(record0, record1);
            
            int a = 5;
        }
        
        return post;
    }

    @Override
    public void create(String text, User user, Film film) {
        try(Session session = driver.session())
        {
            session.writeTransaction((Transaction t) -> createPostNode(t, text, user, film));
        }
    }
    
    private static int createPostNode(Transaction t, String text, User user, Film film){
        t.run("MATCH (u:User) " + 
                "WHERE ID(u) = $userId " + 
                "WITH u " +
                "MATCH (f:Film) WHERE ID(f) = $filmId " +
                "WITH u, f " +
                "CREATE (p:Post {Text: $texte}) " +
                "CREATE (u)-[:CREATE {Timestamp: datetime()}]->(p) " +
                "CREATE (f)-[:RELATIVE]->(p)",
                parameters("userId", user.getId(),
                            "filmId", film.getId(),
                            "text", text));
        return 1;
    }

    @Override
    public void delete(Long idPost) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Long idPost, String text, User user, Film film) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
