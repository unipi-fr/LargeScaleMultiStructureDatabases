package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Engage;
import com.lsmsdbgroup.pisaflix.Entities.Engage.EngageType;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserManager implements UserManagerDatabaseInterface {

    private static UserManager UserManager;
    private static MongoCollection<Document> UserCollection;
    //It's equal to sorting by registration date, index not needed
    private final Document sort = new Document("_id",-1);
    private final int UsersLimit = 27;

    public static UserManager getIstance() {
        if (UserManager == null) {
            UserManager = new UserManager();
        }
        return UserManager;
    }

    public UserManager() {
        UserCollection = DBManager.getMongoDatabase().getCollection("UserCollection");
    }

    @Override
    public User getById(String idUser) {
        // code to get a user
        User user = null;
        try (MongoCursor<Document> cursor = UserCollection.find(eq("_id", new ObjectId(idUser))).iterator()) {
            user = new User(cursor.next());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a user!");
        }
        return user;
    }

    @Override
    public void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
        // code to create a user
        Document userDocument = new Document()
                .append("Username", username)
                .append("Password", password)
                .append("Email", email)
                .append("PrivilegeLevel", privilegeLevel);
        if(lastName != null && !"".equals(lastName)){
            userDocument.put("LastName", lastName);
        }
        if(firstName != null && !"".equals(firstName)){
            userDocument.put("FirstName", firstName);
        }
        //Upsert insert if documnet does't already exists
        UpdateOptions options = new UpdateOptions().upsert(true);
        
        List uniqueFields = new ArrayList();
        uniqueFields.add(new Document("Username", username));
        uniqueFields.add(new Document("Email", email));

        try {
            UserCollection.updateOne(or(uniqueFields), new Document("$set", userDocument), options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the user!");
        } finally {

        }
    }

    @Override
    public void delete(String idUser) {
        DBManager.engageManager.deleteAllRelated(new User(idUser));
        try {
            UserCollection.deleteOne(eq("_id", new ObjectId(idUser)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing a User!");
        }
    }

    @Override
    public void update(User user) {
        update(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPrivilegeLevel());
    }

    @Override
    public void update(String userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
        // code to update a user
        Document userDocument = new Document();
        
        if(username != null && !"".equals(username)){
            userDocument.put("Username", username);
        }
        if(email != null && !"".equals(email)){
            userDocument.put("Email", email);
        }
        if(password != null && !"".equals(password)){
            userDocument.put("Password", password);
        }
        if(privilegeLevel >= 0){
            userDocument.put("PrivilegeLevel", privilegeLevel);
        }
        if(lastName != null && !"".equals(lastName)){
            userDocument.put("lastName", lastName);
        }
        if(firstName != null && !"".equals(firstName)){
            userDocument.put("lastName", firstName);
        }
        try {
            UserCollection.updateOne(eq("_id", new ObjectId(userId)), new Document("$set", userDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating a user!");
        }
    }

    @Override
    public Set<User> getAll(int limit, int skip) {
        // code to retrieve all users
        Set<User> userSet = new LinkedHashSet<>();
        try (MongoCursor<Document> cursor = UserCollection.find().sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                userSet.add(new User(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a user!");
        }
        return userSet;
    }

    @Override
    public Set<User> getByUsername(String username, int limit, int skip) {
        Set<User> userSet = new LinkedHashSet<>();
        try(MongoCursor<Document> cursor = UserCollection.find(eq("Username", username)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                userSet.add(new User(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a user!");
        }
        return userSet;
    }

    @Override
    public Set<User> getByEmail(String email, int limit, int skip) {
        Set<User> userSet = new LinkedHashSet<>();
        try(MongoCursor<Document> cursor = UserCollection.find(eq("Email", email)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                userSet.add(new User(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a user!");
        }
        return userSet;
    }

    @Override
    public boolean checkDuplicates(String username, String email, int limit, int skip) {
        Set<User> userSet = new LinkedHashSet<>();
        try(MongoCursor<Document> cursor = UserCollection.find(or(new Document("Username", username),new Document("Email", email))).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                userSet.add(new User(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a user!");
        }
        return !userSet.isEmpty();
    }
    
    @Override
    public void getFavourites(User user){
        Set<Engage> engageSet = DBManager.engageManager.getEngageSet(user, 0, 0, EngageType.FAVOURITE);
        user.getFilmSet().clear();
        engageSet.forEach((engage) -> {
            user.getFilmSet().add(engage.getFilm());
        });
    }

    @Override
    public Set<User> getFiltered(String usernameFilter, int limit, int skip) {
        Set<User> userSet = new LinkedHashSet<>();        
        List filters = new ArrayList();
        
        if (usernameFilter == null) {
            usernameFilter = "";
        }
        
        // i flag = case insensitive
        filters.add(regex("Username", ".*" + usernameFilter + ".*","i"));
        
        try (MongoCursor<Document> cursor = UserCollection.find(or(filters)).sort(sort).limit(UsersLimit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                userSet.add(new User(cursor.next()));          
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve users filtered!");
        }
        
        return userSet;
    }
}
