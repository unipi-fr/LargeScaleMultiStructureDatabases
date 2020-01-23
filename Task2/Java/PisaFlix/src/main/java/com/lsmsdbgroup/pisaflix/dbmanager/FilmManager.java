package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import static java.util.Objects.hash;
import org.bson.Document;
import org.bson.types.ObjectId;

public class FilmManager implements FilmManagerDatabaseInterface {

    private static FilmManager FilmManager;
    private static MongoCollection<Document> FilmCollection;
    private final Document sort = new Document("PublicationDate", -1);
    private final int commentPageSize = 10;
    private final int filmLimit = 27;

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
        try (MongoCursor<Document> cursor = FilmCollection.find(eq("_id", new ObjectId(filmId))).projection(Projections.exclude("RecentComments")).iterator()) {
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
        try (MongoCursor<Document> cursor = FilmCollection.find().projection(Projections.exclude("RecentComments")).sort(sort).limit(filmLimit).skip(skip).iterator()) {
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
    public boolean create(String title, Date publicationDate, String description) {
        boolean success = false;
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
            UpdateResult result = FilmCollection.updateOne(and(filmDocument), new Document("$set", filmDocument), options);
            if (result.getUpsertedId() != null) {
                success = true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the film!");
        }
        return success;
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
        try {
            FilmCollection.deleteOne(eq("_id", new ObjectId(idFilm)));
            DBManager.engageManager.deleteAllRelated(new Film(idFilm));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in deleting the film!");
        }
    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit, int skip, double adultnessMargin) {
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

        try (MongoCursor<Document> cursor = FilmCollection.find(and(or(filters), lt("Adultness", 1.0 - adultnessMargin))).projection(Projections.exclude("RecentComments")).sort(sort).limit(filmLimit).skip(skip).iterator()) {
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
                .append("_id", String.valueOf(hash(timestamp)))
                .append("User", user.getId()) //Non importa salvare il film, è scontato
                .append("Timestamp", timestamp)
                .append("Text", text);
        getRecentComments(film);
        Set<Comment> commentSet = film.getCommentSet();
        try {
            if (commentSet.size() >= commentPageSize) {
                Comment lastComment = (Comment) commentSet.toArray()[commentSet.size() - 1];
                DBManager.commentManager.createComment(lastComment);
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

    @Override
    public long getCommentPageSize() {
        return commentPageSize;
    }

    @Override
    public void getComments(Film film, int skip, int limit) {
        try {
            film.setCommentSet(new LinkedHashSet<>(DBManager.commentManager.getAll(film, skip, limit)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving the comments!");
        }
    }

    @Override
    public void deleteComment(Comment comment) {
        try {
            FilmCollection.updateOne(eq("_id", new ObjectId(comment.getFilm().getId())), Updates.pull("RecentComments", new Document("_id", comment.getId())));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in deleting the comments!");
        }
    }

    @Override
    public void updateComment(Comment comment) {
        try {
            Document update = new Document("RecentComments.$.Text", comment.getText()).append("RecentComments.$.LastModified", new Date());
            FilmCollection.updateOne(and(new Document("_id", new ObjectId(comment.getFilm().getId())), new Document("RecentComments._id", comment.getId())), new Document("$set", update));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the comments!");
        }
    }

    @Override
    public void deleteReleted(User user) {
        try {
            FilmCollection.updateMany(new Document(), Updates.pull("RecentComments", new Document("User", user.getId())));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the comments!");
        }
    }

    @Override
    public long count(Entity entity) {
        long count = 0;
        try {
            if (entity.getClass() == User.class) {
                Document auxDocument = FilmCollection.aggregate(Arrays.asList(
                        Aggregates.match(new Document("RecentComments.User", entity.getId())), //Trovo i film che contengono commenti con l'utente giusto
                        Aggregates.unwind("$RecentComments"),//Divido i commenti per ogni film in documenti singoli
                        Aggregates.match(new Document("RecentComments.User", entity.getId())), //Trovo i commenti che contengono l'utente giusto
                        Aggregates.count())).first();

                if (auxDocument != null) {
                    count = (int) auxDocument.get("count"); //Li conto
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the comments!");
        }
        return count;
    }

    /**
     * **************** DATA MINING ******************************************
     */
    @Override
    public Set<Film> getFilmToBeClassified(Date date, int limit, int skip) {
        Set<Film> filmSet = new LinkedHashSet<>();
        try (MongoCursor<Document> cursor = FilmCollection.find(or(lt("LastClassUpdate", date), new Document("LastClassUpdate", new Document("$exists", false)))).projection(Projections.include("Description")).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                filmSet.add(new Film(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieving all films!");
        }
        return filmSet;
    }

    @Override
    public void updateClass(String idFilm, double adultness) {
        Document filmDocument = new Document()
                .append("LastClassUpdate", new Date())
                .append("Adultness", adultness);
        try {
            FilmCollection.updateOne(eq("_id", new ObjectId(idFilm)), new Document("$set", filmDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the adultness of the film!");
        }
    }

    @Override
    public Set<Film> getFilmToBeClusterized(int sample, Date date) {
        Set<Film> filmSet = new LinkedHashSet<>();
        try {
            AggregateIterable<Document> resultDocuments = FilmCollection.aggregate(Arrays.asList(
                    Aggregates.match(or(lt("LastClusterUpdate", date), eq("LastClusterUpdate", null))),
                    Aggregates.sample(sample)
            ));
            for (Document filmDocument : resultDocuments) {
                filmSet.add(new Film(filmDocument));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieving a sample of the films!");
        }
        return filmSet;
    }

    @Override
    public void updateCluster(Film film) {
        Document filmDocument = new Document()
                .append("LastClusterUpdate", new Date())
                .append("Tags", new ArrayList<>(film.getTags()))
                .append("Cluster", film.getcluster());
        try {
            FilmCollection.updateOne(eq("_id", new ObjectId(film.getId())), new Document("$set", filmDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the cluster of the film!");
        }
    }

    @Override
    public void resetClusters() {
        Document filmDocument = new Document()
                .append("LastClusterUpdate", null);
        try {
            FilmCollection.updateMany(new Document(), new Document("$set", filmDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in resetting the clusters!");
        }
    }
}
