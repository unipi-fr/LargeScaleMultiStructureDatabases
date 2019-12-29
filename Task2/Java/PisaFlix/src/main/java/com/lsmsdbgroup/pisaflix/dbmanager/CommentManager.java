package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.Entities.Entity.EntityType;
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
    public void createComment(Comment comment) {
        Document commentDocument = new Document()
                .append("Text", comment.getText())
                .append("User", comment.getUser().getId())
                .append("Film", comment.getFilm().getId())
                .append("Type", EntityType.COMMENT.toString());
                
        if(comment.getTimestamp()== null){           
            commentDocument.put("Timestamp", new Date());
        }else{
            commentDocument.put("Timestamp", comment.getTimestamp());
        }
        
        if(comment.getLastModified()!= null){           
            commentDocument.put("LastModified", comment.getTimestamp());
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
            EngageCollection.updateOne(eq("_id", new ObjectId(comment.getId())), new Document("$set", new Document("Text",text).append("LastModified", new Date())));
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
    public void delete(Comment comment){
        if(comment.isRecent() ){
           DBManager.filmManager.deleteComment(comment); 
        }else{
           super.delete(comment.getId());
        }       
    }
    
    @Override
    public void deleteAllRelated(Entity entity){
        super.deleteAllRelated(entity); //Aggiungere il tipo!!!!!!!
    }
    
    @Override
    public Set<Comment> getAll(Film film, int skip, int limit){
        Set<Comment> commentSet = new LinkedHashSet<>();
        try (MongoCursor<Document> cursor = EngageCollection.find(and(eq("Film", film.getId()), eq("Type",EntityType.COMMENT.toString()))).sort(sort).limit(limit).skip(skip).iterator()) {
            while(cursor.hasNext()){
                commentSet.add(new Comment(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving the comments!");
        } 
        return commentSet;
    }
}
