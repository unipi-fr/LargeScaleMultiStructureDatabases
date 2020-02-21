package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;
import static org.neo4j.driver.v1.Values.parameters;

public class UserManager implements UserManagerDatabaseInterface {

    private static UserManager UserManager;

    private final Driver driver;

    private final int limit = 27;

    public static UserManager getIstance() {
        if (UserManager == null) {
            UserManager = new UserManager();
        }
        return UserManager;
    }

    public UserManager() {
        driver = DBManager.getDB();
    }

    @Override
    public User getUserFromRecord(Record record) {
        try {
            Value value = record.get("n");
            Long id = value.asNode().id();

            String username = value.get("Username").asString();
            String firstName = value.get("FirstName").asString();
            String lastName = value.get("LastName").asString();
            int privilegeLevel = value.get("PrivilegeLevel").asInt();
            String email = value.get("Email").asString();
            String password = value.get("Password").asString();

            return new User(id, email, username, privilegeLevel, firstName, lastName, password);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            System.out.println(record);
        }
        return null;
    }

    @Override
    public User getById(Long idUser) {
        User user = null;

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User) WHERE ID(n) = $id RETURN n", parameters("id", idUser));

            while (result.hasNext()) {
                Record record = result.next();

                user = getUserFromRecord(record);
            }
        }

        return user;
    }

    @Override
    public void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
        try (Session session = driver.session()) {
            session.run("CREATE (u:User {Email: $email, FirtName: $firstName, LastName: $lastName, Password: $password, PrivilegeLevel: $privilegeLevel, Username: $username})",
                    parameters("username", username,
                            "password", password,
                            "firstName", firstName,
                            "lastName", lastName,
                            "email", email,
                            "privilegeLevel", privilegeLevel));
        }
    }
    
    @Override
    public void delete(Long idUser) {
        
        try(Session session = driver.session()){
            
            session.writeTransaction((Transaction t) -> deleteUserRelationships(t, idUser));
            session.writeTransaction((Transaction t) -> deleteUserNode(t, idUser));
            
        }
        
    }
    
    private static int deleteUserRelationships(Transaction t, Long idUser){
        
        t.run("MATCH (u:User)-[r]-() WHERE ID(u) = $id DELETE r",
               parameters("id", idUser));
        
        return 1;
        
    }
    
    private static int deleteUserNode(Transaction t, Long idUser){
        
        t.run("MATCH (u:User) WHERE ID(u) = $id DELETE u",
               parameters("id", idUser));
        
        return 1;
        
    }

    @Override
    public void update(User user) {
        update(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPrivilegeLevel());
    }

    @Override
    public void update(Long userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel ) {
        try (Session session = driver.session()) {
            session.run("MATCH (u:User) "
                    + "WHERE ID(u) = $id "
                    + "SET u.Email = $email, u.FirstName = $firstName, u.LastName = $lastName, u.Password = $password, u.PrivilegeLevel = $privilegeLevel, u.Username = $username ",
                    parameters("id", userId,
                            "username", username,
                            "password", password,
                            "firstName", firstName,
                            "lastName", lastName,
                            "email", email,
                            "privilegeLevel", privilegeLevel));
        }
    }

    @Override
    public Set<User> getAll(int limit) {
        Set<User> userSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User) RETURN n LIMIT " + limit);

            while (result.hasNext()) {
                Record record = result.next();

                User user = getUserFromRecord(record);

                userSet.add(user);
            }
        }

        return userSet;
    }

    @Override
    public Set<User> getByUsername(String username) {
        Set<User> userSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User) WHERE n.Username = $username RETURN n",
                    parameters("username", username));

            while (result.hasNext()) {
                Record record = result.next();

                User user = getUserFromRecord(record);

                userSet.add(user);
            }
        }

        return userSet;
    }

    @Override
    public Set<User> getByEmail(String email) {
        Set<User> userSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:User) WHERE n.email = $email RETURN n",
                    parameters("email", email));

            while (result.hasNext()) {
                Record record = result.next();

                User user = getUserFromRecord(record);

                userSet.add(user);
            }
        }

        return userSet;
    }

    @Override
    public boolean checkDuplicates(String username, String email) {
        Set<User> userSetUsername;
        Set<User> userSetEmail;

        userSetEmail = getByEmail(email);
        userSetUsername = getByUsername(username);

        return !(userSetEmail.isEmpty() && userSetUsername.isEmpty());
    }

    @Override
    public Set<User> getFiltered(String usernameFilter, int limit, int skip) {
            
        Set<User> userSet = new LinkedHashSet<>();
        
        int queryLimit = 0;
        if(limit == 0){
            queryLimit = this.limit;
        }else{
            queryLimit = limit;
        }

        try (Session session = driver.session()) {
            
            StatementResult result = session.run("MATCH (n:User) "
                                               + "WHERE n.Username =~ '.*"+usernameFilter+".*' "
                                               + "RETURN n "
                                               + "SKIP " + skip + " "
                                               + "LIMIT " + queryLimit);

            while (result.hasNext()) {
                userSet.add(getUserFromRecord(result.next()));
            }
            
        }

        return userSet;
        
    }
    
    @Override
    public void follow(User follower, User followed) {
        
        try(Session session = driver.session()){
            session.run("MATCH (u1:User),(u2:User) " 
                      + "WHERE ID(u1) = "+follower.getId()+" "
                      + "AND ID(u2) = "+followed.getId()+" " 
                      + "CREATE (u1)-[r:FOLLOWS]->(u2) " 
                      + "RETURN r");

        }
        
    }
    
    @Override
    public boolean isFollowing(User follower, User followed){
        
        StatementResult result = null;
        
        try(Session session = driver.session()){
            result = session.run("MATCH (u1:User)-[r:FOLLOWS]->(u2:User) "
                               + "WHERE ID(u1) = "+follower.getId()+" "
                               + "AND ID(u2) = "+followed.getId()+" "
                               + "RETURN r");

        }
        
        return result.hasNext();
        
    }
    
    @Override
    public void unfollow(User follower, User followed){
        
        try(Session session = driver.session()){
            session.run("MATCH (u1:User)-[r:FOLLOWS]->(u2:User) "
                               + "WHERE ID(u1) = "+follower.getId()+" "
                               + "AND ID(u2) = "+followed.getId()+" "
                               + "DELETE r");

        }
        
    }
    
    @Override
    public long countFollowers(User user){
        
        StatementResult result = null;
        
        try(Session session = driver.session()){
            result = session.run("MATCH (u1:User)-[r:FOLLOWS]->(u2:User) "
                               + "WHERE ID(u2) = "+user.getId()+" "
                               + "RETURN count(DISTINCT u1) AS followers");

        }

        return result.next().get("followers").asLong();
        
    }
    
    @Override
    public HashMap<String, Long> countFollowing(User user){
        
        HashMap<String, Long> following = new HashMap<>();
        StatementResult result = null;
        
        try(Session session = driver.session()){
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User), (u1:User)-[:FOLLOWS]->(f:Film)"
                               + "WHERE ID(u1) = "+user.getId()+" "
                               + "RETURN count(DISTINCT u2) AS followingUsers, count(DISTINCT f) AS followingFilms");

        }
        
        Record value = result.next();
        following.put("followingUsers", value.get("followingUsers").asLong());
        following.put("followingFilms", value.get("followingFilms").asLong());
        return  following;
        
    }
    
    @Override
    public Set<User> getFollowers(User user){
        
        Set<User> userSet = new LinkedHashSet<>();
        StatementResult result = null;
        
        try(Session session = driver.session()){
            result = session.run("MATCH (n:User)-[r:FOLLOWS]->(u:User) "
                               + "WHERE ID(u) = "+user.getId()+" "
                               + "RETURN n");

        }
        
        while(result.hasNext()){
            userSet.add(getUserFromRecord(result.next()));
        }
        
        return userSet;
        
    }
    
    @Override
    public Set<User> getFollowingUsers(User user){
        
        Set<User> userSet = new LinkedHashSet<>();
        StatementResult result = null;
        
        try(Session session = driver.session()){
            result = session.run("MATCH (u:User)-[:FOLLOWS]->(n:User) "
                               + "WHERE ID(u) = "+user.getId()+" "
                               + "RETURN n");

        }
        
        while(result.hasNext()){
            userSet.add(getUserFromRecord(result.next()));
        }
        
        return userSet;
        
    }
    
    @Override
    public Set<Film> getFollowingFilms(User user){
        
        Set<Film> filmSet = new LinkedHashSet<>();
        StatementResult result = null;
        
        try(Session session = driver.session()){
            result = session.run("MATCH (u:User)-[:FOLLOWS]->(n:Film) "
                               + "WHERE ID(u) = "+user.getId()+" "
                               + "RETURN n");

        }
        
        while(result.hasNext()){
            filmSet.add(DBManager.filmManager.getFilmFromRecord(result.next()));
        }
        
        return filmSet;
        
    }
    
    @Override
    public Set<User> getSuggestedUsers(User user, int limit){
        
        int queryLimit = 0;
        if(limit == 0){
            queryLimit = this.limit;
        }else{
            queryLimit = limit;
        }
        
        Set<User> userSet = new LinkedHashSet<>();
        StatementResult result = null;
        
        try(Session session = driver.session()){
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(n:User) "
                               + "WHERE ID(u1) = "+user.getId()+" "
                               + "AND NOT (u1)-[:FOLLOWS]->(n) "
                               + "RETURN n "
                               + "LIMIT " + queryLimit);

        }
        
        while(result.hasNext()){
            User suggestesUser = getUserFromRecord(result.next());
            suggestesUser.setType("SUGGESTED");
            userSet.add(suggestesUser);
        }
        
        return userSet;
        
    }

    @Override
    public int getLimit() {
        return limit;
    }
    
}
