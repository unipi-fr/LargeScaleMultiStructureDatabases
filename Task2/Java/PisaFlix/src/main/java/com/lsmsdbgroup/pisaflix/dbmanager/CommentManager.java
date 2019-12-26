package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

public class CommentManager extends EngageManager implements CommentManagerDatabaseInterface  {
    private static CommentManager CommentManager;

    public static CommentManager getIstance() {
        if (CommentManager == null) {
            CommentManager = new CommentManager();
        }
        return CommentManager;
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
            EngageCollection.updateOne(and(commentDocument), new Document("$set", commentDocument), options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the comment!");
        }
    }

    @Override
    public void update(Comment comment, String text) {
        try {
            EngageCollection.updateOne(eq("_id", new ObjectId(comment.getId())), new Document("$set", new Document("Text",text)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the Comment!");
        }
    }

    @Override
    public Comment getById(String idComment) {
        Comment comment = null;
        try (MongoCursor<Document> cursor = EngageCollection.find(eq("_id", new ObjectId(idComment))).iterator()) {
            comment = new Comment(cursor.next());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a comment!");
        } 
        return comment;
    }
    
    @Override
    public void delete(String idComment){
        super.delete(idComment);
    }
    
    @Override
    public void deleteAllRelated(Entity entity){
        super.deleteAllRelated(entity);
    }
}
