package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserManager implements UserManagerDatabaseInterface {

    private static UserManager UserManager;
    private static MongoCollection<Document> UserCollection;
    //It's equal to sorting by registration date, index not needed
    private final Document sort = new Document("_id", -1);

    public static UserManager getIstance() {
        if (UserManager == null) {
            UserManager = new UserManager();
        }
        return UserManager;
    }

    public UserManager() {
        UserCollection = DBManager.getMongoDatabase().getCollection("UserCollection");
    }

    private User getUserFromDocument(Document userDocument) throws NonConvertibleDocumentException {
        User user = null;

        if (userDocument.containsKey("_id") && userDocument.containsKey("Username") && userDocument.containsKey("Password") && userDocument.containsKey("Email") && userDocument.containsKey("PrivilegeLevel")) {
            String idUser = userDocument.get("_id").toString();
            String username = userDocument.getString("Username");
            String password = userDocument.getString("Password");
            String email = userDocument.getString("Email");
            int privilegeLevel = userDocument.getInteger("PrivilegeLevel");

            String firstName = null;
            if (userDocument.containsKey("FirstName")) {
                firstName = userDocument.getString("FirstName");
            }

            String lastName = null;
            if (userDocument.containsKey("LastName")) {
                lastName = userDocument.getString("LastName");
            }

            user = new User(idUser, username, password, privilegeLevel, firstName, lastName);

            List<String> favoritesFilm = (List<String>) userDocument.get("FavoritesFilm");

            if (favoritesFilm != null) {
                Set<Film> films = new LinkedHashSet<Film>();
                for (String filmId : favoritesFilm) {
                    Film film = DBManager.filmManager.getById(filmId);
                    films.add(film);
                }
                user.setFilmSet(films);
            }
            
            List<String> favoritesCinema = (List<String>) userDocument.get("FavoritesCinema");

            if (favoritesCinema != null) {
                Set<Cinema> cinemas = new LinkedHashSet<Cinema>();
                for (String cinemaId : favoritesFilm) {
                    Cinema cinema = DBManager.cinemaMamager.getById(cinemaId);
                    cinemas.add(cinema);
                }
                user.setCinemaSet(cinemas);
            }

        } else {
            throw new NonConvertibleDocumentException("Document not-convertible in user");
        }

        return user;
    }

    @Override
    public User getById(String idUser) {
        // code to get a user
        User user = null;
        try (MongoCursor<Document> cursor = UserCollection.find(eq("_id", new ObjectId(idUser))).iterator()) {
            Document userDocument = cursor.next();

            user = getUserFromDocument(userDocument);
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

        if (lastName != null && !"".equals(lastName)) {
            userDocument.put("LastName", lastName);
        }
        if (firstName != null && !"".equals(firstName)) {
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
    public void updateFavoritesFilm(User user) {
        Set<Film> films = user.getFilmSet();
        List<String> idFilms = new ArrayList<String>();
        
        for(Film film: films)
            idFilms.add(film.getId());
        
        String idUser = user.getId();

        try {
            UserCollection.updateOne(new Document("_id", new ObjectId(idUser)), new Document("$set", new Document("FavoritesFilm", idFilms)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating favorites!");
        } finally {

        }
    }
    
    @Override
    public void updateFavoritesCinema(User user) {
        Set<Cinema> cinemas = user.getCinemaSet();
        List<String> idCinema = new ArrayList<String>();
        
        for(Cinema cinema: cinemas)
            idCinema.add(cinema.getId());
        
        String idUser = user.getId();

        try {
            UserCollection.updateOne(new Document("_id", new ObjectId(idUser)), new Document("$set", new Document("FavoritesCinema", idCinema)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating favorites!");
        } finally {

        }
    }

    @Override
    public void delete(String idUser) {
        clearCinemaSetAndFilmSet(getById(idUser));
        try {
            UserCollection.deleteOne(eq("_id", new ObjectId(idUser)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing a User!");
        }
    }

    @Override
    public void clearCinemaSetAndFilmSet(User user) {
        user.setCinemaSet(new LinkedHashSet<>());
        user.setFilmSet(new LinkedHashSet<>());
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred clearing the user's cinemaset and filmset!");
        } finally {

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

        if (username != null && !"".equals(username)) {
            userDocument.put("Username", username);
        }
        if (email != null && !"".equals(email)) {
            userDocument.put("Email", email);
        }
        if (password != null && !"".equals(password)) {
            userDocument.put("Password", password);
        }
        if (privilegeLevel >= 0) {
            userDocument.put("PrivilegeLevel", privilegeLevel);
        }
        if (lastName != null && !"".equals(lastName)) {
            userDocument.put("lastName", lastName);
        }
        if (firstName != null && !"".equals(firstName)) {
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
                Document userDocument = cursor.next();
                User user = getUserFromDocument(userDocument);

                userSet.add(user);
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
        try (MongoCursor<Document> cursor = UserCollection.find(eq("Username", username)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                Document userDocument = cursor.next();
                User user = getUserFromDocument(userDocument);

                userSet.add(user);
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
        try (MongoCursor<Document> cursor = UserCollection.find(eq("Email", email)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                Document userDocument = cursor.next();
                User user = getUserFromDocument(userDocument);

                userSet.add(user);
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
        try (MongoCursor<Document> cursor = UserCollection.find(or(new Document("Username", username), new Document("Email", email))).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                Document userDocument = cursor.next();
                User user = getUserFromDocument(userDocument);

                userSet.add(user);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a user!");
        }
        return !userSet.isEmpty();
    }

    @Override
    public Set<User> getFiltered(String usernameFilter, int limit, int skip) {
        Set<User> userSet = new LinkedHashSet<>();
        List filters = new ArrayList();

        if (usernameFilter == null) {
            usernameFilter = "";
        }

        // i flag = case insensitive
        filters.add(regex("Username", ".*" + usernameFilter + ".*", "i"));

        try (MongoCursor<Document> cursor = UserCollection.find(or(filters)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                Document userDocument = cursor.next();
                User user = getUserFromDocument(userDocument);

                userSet.add(user);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve users filtered!");
        }

        return userSet;
    }
}
