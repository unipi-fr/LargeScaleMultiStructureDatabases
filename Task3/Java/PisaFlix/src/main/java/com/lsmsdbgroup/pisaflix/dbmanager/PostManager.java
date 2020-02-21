package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Post;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.PostManagerDatabaseInterface;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
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
    
    private static Post getPostFromResult(StatementResult result){
        Post post = null;
        
        if(!result.hasNext())
            return null;
        
        Record record = result.next();
        Node node = record.get("p").asNode();
        
        Long idPost = node.id();
        String text = node.get("Text").asString();

        post = new Post(idPost, text);
        
        getPostFromRecord(record, post);
                
        while (result.hasNext())
        {
            record = result.next();
            
            getPostFromRecord(record, post);
        }
        
        return post;
    }
    
    private static void getPostFromRecord(Record record, Post post){
        Relationship relationship = record.get("r").asRelationship();
        
        ZonedDateTime timestamp;
        Date timestampDate;
        if(relationship.hasType("CREATE"))
        {
            timestamp = relationship.get("Timestamp").asZonedDateTime();
            timestampDate = Date.from(timestamp.toInstant());
            
            post.setTimestamp(timestampDate);
            
            Long userId = record.get("n").asNode().id();
            User user = DBManager.userManager.getById(userId);
            
            post.setUser(user);
        }else{
            Long filmId = record.get("n").asNode().id();
            Film film = DBManager.filmManager.getById(filmId);
            
            post.addFilm(film);
        }
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
        
        StatementResult result = tx.run("MATCH (p:Post) where ID(p) = $id " +
                                        "MATCH (p)-[r]-(n) RETURN p, r, n",
                                        parameters("id", idPost));
       
        post = getPostFromResult(result);
        
        return post;
    }

    @Override
    public void create(String text, User user, Set<Film> films) {
        try(Session session = driver.session())
        {
            session.writeTransaction((Transaction t) -> createPostNode(t, text, user, films));
        }
    }
    
    private static int createPostNode(Transaction t, String text, User user, Set<Film> films){
        StatementResult result = t.run("MATCH (u:User) " + 
                                        "WHERE ID(u) = $userId " + 
                                        "WITH u " +
                                        "CREATE (p:Post {Text: $text}) " +
                                        "CREATE (u)-[:CREATE {Timestamp: datetime()}]->(p) " +
                                        "RETURN ID(p) AS createdPostID",
                                        parameters("userId", user.getId(),
                                                    "text", text));
        
        if(!result.hasNext())
            return 0;
        
        Long idPost = result.next().get("createdPostID").asLong();
        
        for(Film film: films){
            t.run("MATCH (p: Post) " + 
                    "WHERE ID(p) = $idPost " +
                    "WITH p " +
                    "MATCH (f: Film) " +
                    "WHERE ID(f) = $idFilm " +
                    "WITH p, f " +
                    "CREATE (p)-[:TAGS]->(f)",
                    parameters("idPost", idPost,
                                "idFilm", film.getId()));               
        }
        
        return 1;
    }

    @Override
    public void delete(Long idPost) {
        try(Session session = driver.session())
        {
            session.writeTransaction((Transaction t) -> deletePostNode(t, idPost));
        }
    }
    
    private static int deletePostNode(Transaction t, Long idPost)
    {
        t.run("MATCH(p:Post) where ID(p) = $id " +
                "MATCH (p)-[r]-() " +
                "DELETE r, p",
                parameters("id", idPost));
        
        return 1;
    }
    
    @Override
    public void update(Long idPost, String text) {
        try(Session session = driver.session())
        {
            session.writeTransaction((Transaction t) -> updatePostNode(t, idPost, text));
        }
    }
    
    private static int updatePostNode(Transaction t, Long idPost, String text)
    {
        t.run("MATCH (p:Post) where ID(p) = $id " +
                "SET p.Text = $text " +
                "WITH p " +
                "MATCH ()-[r:CREATE]->(p) " +
                "SET r.LastModified = datetime()",
                parameters("id", idPost,
                            "text", text));
        
        return 1;
    }
}
