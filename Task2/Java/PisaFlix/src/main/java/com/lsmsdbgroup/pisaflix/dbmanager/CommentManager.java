package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

public class CommentManager implements CommentManagerDatabaseInterface {

private static CommentManager CommentManager;
    private static MongoCollection<Document> CommentCollection;
    //It's equal to sorting by publication date, index not needed
    private final Document sort = new Document("_id",-1);

    public static CommentManager getIstance() {
        if (CommentManager == null) {
            CommentManager = new CommentManager();
        }
        return CommentManager;
    }

    public CommentManager() {
        CommentCollection = DBManager.getMongoDatabase().getCollection("CommentCollection");
    }

    
@Override
    public void createComment(String text, User user, Entity entity) {
        Document commentDocument = new Document()
                .append("Text", text)
                .append("User", user.getId())
                .append("Timestamp", new Date());
        if(entity.getClass().equals(Film.class)){
            commentDocument.put("Film", entity.getId());
        }else{
            commentDocument.put("Cinema", entity.getId());
        }
        //Upsert insert if documnet does't already exists
        UpdateOptions options = new UpdateOptions().upsert(true);

        try {
            CommentCollection.updateOne(and(commentDocument), new Document("$set", commentDocument), options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the comment!");
        }
    }

    @Override
    public void update(Comment comment, String text) {
        try {
            CommentCollection.updateOne(eq("_id", new ObjectId(comment.getId())), new Document("$set", new Document("Text",text)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the Comment!");
        }
    }

    @Override
    public void delete(String idComment) {
        try {
            CommentCollection.deleteOne(eq("_id", new ObjectId(idComment)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        }
    }
    
@Override
    public void deleteAll(Entity entity) {
        try {
            if(entity.getClass().equals(Film.class)){
                CommentCollection.deleteMany(eq("Film", entity.getId()));
            }else{
                CommentCollection.deleteMany(eq("Cinema", entity.getId()));
                System.out.println(entity.getId());
            }           
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        }
    }


    @Override
    public Comment getById(String commentId) {
        Comment comment = null;
        try (MongoCursor<Document> cursor = CommentCollection.find(eq("_id", new ObjectId(commentId))).iterator()) {
            comment = new Comment(cursor.next());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a comment!");
        } 
        return comment;
    }
    
@Override
    public Set<Comment> getCommentSet(Entity entity, int limit, int skip) {
        Set<Comment> commentSet = new LinkedHashSet<>();
        List filters = new ArrayList();
        
        if(entity.getClass().equals(Film.class)){
            filters.add(new Document("Film", entity.getId()));
        }else{
            filters.add(new Document("Cinema", entity.getId()));
        }

        try (MongoCursor<Document> cursor = CommentCollection.find(and(filters)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                commentSet.add(new Comment(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve comments!");
        }

        return commentSet;
    }

}
