package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Entity.EntityType;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import static java.util.Objects.hash;
import static java.util.Set.of;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class FilmManager implements FilmManagerDatabaseInterface {

    private static FilmManager FilmManager;
    private static MongoCollection<Document> FilmCollection;
    private final Document sort = new Document("_id", -1);
    private final int commentsMaxSize = 5;

    public static FilmManager getIstance() {
        if (FilmManager == null) {
            FilmManager = new FilmManager();
        }
        return FilmManager;
    }

    private FilmManager() {
        FilmCollection = DBManager.getMongoDatabase().getCollection("FilmCollection");
    }

    @Override
    public Film getById(String filmId) {
        Film film = null;
        try (MongoCursor<Document> cursor = FilmCollection.find(eq("_id", new ObjectId(filmId))).iterator()) {
            film = new Film(cursor.next());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a film!");
        }
        return film;
    }

    @Override
    public Set<Film> getAll(int limit, int skip) {
        Set<Film> filmSet = new LinkedHashSet<>();
        try (MongoCursor<Document> cursor = FilmCollection.find().sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                filmSet.add(new Film(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all films!");
        }
        return filmSet;
    }

    @Override
    public void create(String title, Date publicationDate, String description) {
        Document filmDocument = new Document()
                .append("Title", title)
                .append("PublicationDate", publicationDate)
                .append("RecentComments", new ArrayList<Document>());
        if (description != null) {
            filmDocument.put("Description", description);
        }
        //Upsert insert if documnet does't already exists
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            FilmCollection.updateOne(and(filmDocument), new Document("$set", filmDocument), options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the film!");
        }
    }

    @Override
    public void update(String idFilm, String title, Date publicationDate, String description) {
        Document filmDocument = new Document()
                .append("Title", title)
                .append("PublicationDate", publicationDate);
        if (description != null) {
            filmDocument.put("Description", description);
        }
        try {
            FilmCollection.updateOne(eq("_id", new ObjectId(idFilm)), new Document("$set", filmDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the film!");
        }
    }

    @Override
    public void delete(String idFilm) {
        //clearUserSet(getById(idFilm));
        try {
            FilmCollection.deleteOne(eq("_id", new ObjectId(idFilm)));
            DBManager.commentManager.deleteAllRelated(new Film(idFilm));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in deleting the film!");
        }
    }

    @Override
    public void clearUserSet(Film film) {
        film.setUserSet(new LinkedHashSet<>());
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred clearing the film's userset!");
        } finally {

        }
    }

    @Override
    public void updateFavorites(Film film) {
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred updating favorite films!");
        } finally {

        }
    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit, int skip) {
        Set<Film> filmSet = new LinkedHashSet<>();
        List filters = new ArrayList();

        if ((startDateFilter == null || endDateFilter == null) && titleFilter == null) {
            return getAll(limit, skip);
        }

        if (titleFilter != null) {
            filters.add(regex("Title", ".*" + titleFilter + ".*", "i"));
        }

        if (startDateFilter != null && endDateFilter != null) {
            filters.add(and(gte("PublicationDate", startDateFilter), lt("PublicationDate", endDateFilter)));
        }

        try (MongoCursor<Document> cursor = FilmCollection.find(or(filters)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                filmSet.add(new Film(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve films filtered!");
        }

        return filmSet;
    }

    @Override
    public void addComment(Film film, User user, String text) {
        Date timestamp = new Date();
        Document commentDocument = new Document()
                .append("_id", hash(timestamp))
                .append("User", new ObjectId(user.getId())) //Non importa salvare il film, è scontato
                .append(EntityType.COMMENT + "-" + "Timestamp", timestamp)
                .append("Text", text);
        getRecentComments(film);
        Set<Comment> commentSet = film.getCommentSet();
        try {
            if (commentSet.size() >= commentsMaxSize) {
                Comment lastComment = (Comment) commentSet.toArray()[commentSet.size() - 1];
                DBManager.commentManager.createComment(lastComment.getText(), lastComment.getUser(), film, lastComment.getTimestamp());
                FilmCollection.updateOne(eq("_id", new ObjectId(film.getId())), Updates.popFirst("RecentComments"));
            }
            FilmCollection.updateOne(eq("_id", new ObjectId(film.getId())), Updates.push("RecentComments", commentDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in adding the comment!");
        }
    }

    @Override
    public void getRecentComments(Film film) {
        try (MongoCursor<Document> cursor = FilmCollection.find(eq("_id", new ObjectId(film.getId()))).iterator()) {
            ArrayList<Document> DocumentSet = (ArrayList<Document>) cursor.next().get("RecentComments");
            List<Comment> CommentSet = new ArrayList<>();
            DocumentSet.forEach((commentDocument) -> {
                Comment comment = new Comment(commentDocument);
                comment.setFilm(film);
                CommentSet.add(0, comment);   
            });
            film.setCommentSet(new LinkedHashSet<>(CommentSet));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving recent comments!");
        }
    }
}
