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
            Value value = record.get("u");
            Long id = value.asNode().id();

            String username = value.get("Username").asString();
            String firstName = value.get("FirstName").asString();
            String lastName = value.get("LastName").asString();
            int privilegeLevel = value.get("PrivilegeLevel").asInt();
            String email = value.get("Email").asString();
            String password = value.get("Password").asString();

            return new User(id, email, username, privilegeLevel, firstName, lastName, password);
        } catch (Exception ex) {
            System.out.println("User from record error: " + ex.getLocalizedMessage());
            System.out.println("Record not convertible: " + record);
        }
        return null;
    }

    @Override
    public User getById(Long idUser) {
        User user = null;

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (u:User) WHERE ID(u) = $id RETURN u", parameters("id", idUser));

            while (result.hasNext()) {
                Record record = result.next();

                user = getUserFromRecord(record);
            }
        } catch (Exception ex) {
            System.out.println("User retrieval error: " + ex.getLocalizedMessage());
        }

        return user;
    }

    @Override
    public void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
        try (Session session = driver.session()) {
            session.run("CREATE (u:User {Email: $email, FirstName: $firstName, LastName: $lastName, Password: $password, PrivilegeLevel: $privilegeLevel, Username: $username})",
                    parameters("username", username,
                            "password", password,
                            "firstName", firstName,
                            "lastName", lastName,
                            "email", email,
                            "privilegeLevel", privilegeLevel));
        } catch (Exception ex) {
            System.out.println("User registration error: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public void delete(Long idUser) {

        try (Session session = driver.session()) {

            session.writeTransaction((Transaction t) -> deleteUserRelationships(t, idUser));
            session.writeTransaction((Transaction t) -> deleteUserNode(t, idUser));

        } catch (Exception ex) {
            System.out.println("Delete user error: " + ex.getLocalizedMessage());
        }

    }

    private static int deleteUserRelationships(Transaction transaction, Long idUser) {

        transaction.run("MATCH (u:User)-[r]-() WHERE ID(u) = $id DELETE r",
                parameters("id", idUser));

        return 1;

    }

    private static int deleteUserNode(Transaction transaction, Long idUser) {

        transaction.run("MATCH (u:User) WHERE ID(u) = $id DELETE u",
                parameters("id", idUser));

        return 1;

    }

    @Override
    public void update(User user) {
        update(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPrivilegeLevel());
    }

    @Override
    public void update(Long userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
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
        } catch (Exception ex) {
            System.out.println("Update user error: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public Set<User> getAll(int limit) {
        Set<User> userSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (u:User) RETURN u LIMIT $limit",
                    parameters("limit", limit));

            while (result.hasNext()) {
                Record record = result.next();

                User user = getUserFromRecord(record);

                userSet.add(user);
            }
        } catch (Exception ex) {
            System.out.println("All users retrieval error: " + ex.getLocalizedMessage());
        }

        return userSet;
    }

    @Override
    public Set<User> getByUsername(String username) {
        Set<User> userSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (u:User) WHERE u.Username = $username RETURN u",
                    parameters("username", username));

            while (result.hasNext()) {
                Record record = result.next();

                User user = getUserFromRecord(record);

                userSet.add(user);
            }
        } catch (Exception ex) {
            System.out.println("User retrieval error: " + ex.getLocalizedMessage());
        }

        return userSet;
    }

    @Override
    public Set<User> getByEmail(String email) {
        Set<User> userSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (u:User) WHERE u.email = $email RETURN u",
                    parameters("email", email));

            while (result.hasNext()) {
                Record record = result.next();

                User user = getUserFromRecord(record);

                userSet.add(user);
            }
        } catch (Exception ex) {
            System.out.println("User retrieval error: " + ex.getLocalizedMessage());
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

        int queryLimit;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        try (Session session = driver.session()) {

            StatementResult result = session.run("MATCH (u:User) "
                    + "WHERE u.Username =~ $usernameFilter "
                    + "RETURN u "
                    + "SKIP $skip "
                    + "LIMIT $limit",
                    parameters("skip", skip, "limit", queryLimit, "usernameFilter", ".*" + usernameFilter + ".*"));

            while (result.hasNext()) {
                userSet.add(getUserFromRecord(result.next()));
            }

        } catch (Exception ex) {
            System.out.println("Filtered users retrieval error: " + ex.getLocalizedMessage());
        }

        return userSet;

    }

    @Override
    public void follow(User follower, User followed) {

        try (Session session = driver.session()) {
            session.run("MATCH (u1:User),(u2:User) "
                    + "WHERE ID(u1) = $followerId "
                    + "AND ID(u2) = $followedId "
                    + "CREATE (u1)-[r:FOLLOWS]->(u2) "
                    + "RETURN r",
                    parameters("followerId", follower.getId(), "followedId", followed.getId()));

        } catch (Exception ex) {
            System.out.println("Follow error: " + ex.getLocalizedMessage());
        }

    }

    @Override
    public boolean isFollowing(User follower, User followed) {

        StatementResult result;

        try (Session session = driver.session()) {

            result = session.run("MATCH (u1:User)-[r:FOLLOWS]->(u2:User) "
                    + "WHERE ID(u1) = $followerId "
                    + "AND ID(u2) = $followedId "
                    + "RETURN r",
                    parameters("followerId", follower.getId(), "followedId", followed.getId()));

        } catch (Exception ex) {
            System.out.println("Is following? error: " + ex.getLocalizedMessage());
            return false;
        }
        
        return result.hasNext();

    }

    @Override
    public void unfollow(User follower, User followed) {

        try (Session session = driver.session()) {
            session.run("MATCH (u1:User)-[r:FOLLOWS]->(u2:User) "
                    + "WHERE ID(u1) = $followerId "
                    + "AND ID(u2) = $followedId "
                    + "DELETE r",
                    parameters("followerId", follower.getId(), "followedId", followed.getId()));

        } catch (Exception ex) {
            System.out.println("Unfollow error: " + ex.getLocalizedMessage());
        }

    }

    @Override
    public long countFollowers(User user) {

        StatementResult result;

        try (Session session = driver.session()) {

            result = session.run("MATCH (u1:User)-[r:FOLLOWS]->(u2:User) "
                    + "WHERE ID(u2) = $userId "
                    + "RETURN count(DISTINCT u1) AS followers",
                    parameters("userId", user.getId()));

        } catch (Exception ex) {
            System.out.println("Followers count error: " + ex.getLocalizedMessage());
            return (long)0;
        }
        
        return result.next().get("followers").asLong();

    }

    @Override
    public HashMap<String, Long> countFollowing(User user) {

        HashMap<String, Long> following = new HashMap<>();

        StatementResult result;

        try (Session session = driver.session()) {

            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User), (u1:User)-[:FOLLOWS]->(f:Film)"
                    + "WHERE ID(u1) = $userId "
                    + "RETURN count(DISTINCT u2) AS followingUsers, count(DISTINCT f) AS followingFilms",
                    parameters("userId", user.getId()));

        } catch (Exception ex) {
            System.out.println("Following count error: " + ex.getLocalizedMessage());
            // next 3 lines to prevent the usage of result as it is null
            following.put("followingUsers", (long)0);
            following.put("followingFilms", (long)0);
            return following;
        }
        
        Record value = result.next();
        following.put("followingUsers", value.get("followingUsers").asLong());
        following.put("followingFilms", value.get("followingFilms").asLong());
        return following;

    }

    @Override
    public Set<User> getFollowers(User user) {

        Set<User> userSet = new LinkedHashSet<>();

        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[r:FOLLOWS]->(followed:User) "
                    + "WHERE ID(followed) = $userId "
                    + "RETURN u",
                    parameters("userId", user.getId()));

        } catch (Exception ex) {
            System.out.println("Followers retrieval error: " + ex.getLocalizedMessage());
            return userSet; //EMPTY!!!
        }
        
        while (result.hasNext()) {
            userSet.add(getUserFromRecord(result.next()));
        }

        return userSet;

    }

    @Override
    public Set<User> getFollowingUsers(User user) {

        Set<User> userSet = new LinkedHashSet<>();

        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (follower:User)-[:FOLLOWS]->(u:User) "
                    + "WHERE ID(follower) = $userId "
                    + "RETURN u",
                    parameters("userId", user.getId()));

        } catch (Exception ex) {
            System.out.println("Following users retrieval error: " + ex.getLocalizedMessage());
            return userSet; //EMPTY!!!
        }
        
        while (result.hasNext()) {
            userSet.add(getUserFromRecord(result.next()));
        }

        return userSet;

    }

    @Override
    public Set<Film> getFollowingFilms(User user) {

        Set<Film> filmSet = new LinkedHashSet<>();

        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[:FOLLOWS]->(f:Film) "
                    + "WHERE ID(u) = $userId "
                    + "RETURN f",
                    parameters("userId", user.getId()));

        } catch (Exception ex) {
            System.out.println("Following films retrieval error: " + ex.getLocalizedMessage());
            return filmSet; //EMPTY!!!
        }
        
        while (result.hasNext()) {
            filmSet.add(DBManager.filmManager.getFilmFromRecord(result.next()));
        }

        return filmSet;

    }

    @Override
    public Set<User> getSuggestedUsers(User user, int limit) {

        int queryLimit;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<User> userSet = new LinkedHashSet<>();

        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(:User)-[:FOLLOWS]->(u:User) "
                    + "WHERE ID(u1) = $userId "
                    + "AND NOT (u1)-[:FOLLOWS]->(u) "
                    + "AND NOT (u2)-[:FOLLOWS]->(u) "
                    + "RETURN u "
                    + "LIMIT $limit",
                    parameters("userId", user.getId(), "limit", queryLimit));

        } catch (Exception ex) {
            System.out.println("Suggested users retrieval error: " + ex.getLocalizedMessage());
            return userSet; //EMPTY!!!
        }
        
        while (result.hasNext()) {
            User suggestesUser = getUserFromRecord(result.next());
            suggestesUser.setType("SUGGESTED");
            userSet.add(suggestesUser);
        }

        return userSet;

    }

    @Override
    public Set<User> getVerySuggestedUsers(User user, int limit) {

        int queryLimit;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<User> userSet = new LinkedHashSet<>();
        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(u:User) "
                    + "WHERE ID(u1) = $userId "
                    + "AND NOT (u1)-[:FOLLOWS]->(u) "
                    + "RETURN u "
                    + "LIMIT $limit",
                    parameters("userId", user.getId(), "limit", queryLimit));

        } catch (Exception ex) {
            System.out.println("Very suggested users retrieval error: " + ex.getLocalizedMessage());
            return userSet; //EMPTY!!!
        }
        
        while (result.hasNext()) {
            User suggestesUser = getUserFromRecord(result.next());
            suggestesUser.setType("VERY SUGGESTED");
            userSet.add(suggestesUser);
        }

        return userSet;

    }

    @Override
    public int getLimit() {
        return limit;
    }

}
