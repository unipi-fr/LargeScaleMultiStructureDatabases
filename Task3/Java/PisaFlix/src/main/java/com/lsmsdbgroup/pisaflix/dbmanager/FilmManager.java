package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.DateConverter;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;
import static org.neo4j.driver.v1.Values.parameters;

public class FilmManager implements FilmManagerDatabaseInterface {

    private static FilmManager FilmManager;
    private final Driver driver;

    public final int limit = 27;

    public static FilmManager getIstance() {
        if (FilmManager == null) {
            FilmManager = new FilmManager();
        }

        return FilmManager;
    }

    private FilmManager() {
        driver = DBManager.getDB();
    }

    @Override
    public Film getFilmFromRecord(Record record) {
        Value value = record.get("n");
        Long id = value.asNode().id();

        String title = value.get("Title").asString();
        String publicationDateStr = value.get("PublicationDate").asString();
        String wikiPage = value.get("WikiPage").asString();

        Date publicationDate = DateConverter.StringToDate(publicationDateStr);
        return new Film(id, title, publicationDate, wikiPage);
    }

    @Override
    public Film getById(Long filmId) {
        Film film = null;

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:Film) WHERE ID(n) = $id RETURN n", parameters("id", filmId));

            while (result.hasNext()) {
                Record record = result.next();

                film = getFilmFromRecord(record);
            }
        }

        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> filmSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (n:Film) RETURN n LIMIT " + limit);

            while (result.hasNext()) {
                Record record = result.next();

                Film film = getFilmFromRecord(record);

                filmSet.add(film);
            }
        }

        return filmSet;
    }

    @Override
    public boolean create(String title, Date publicationDate) {
        boolean success = false;

        try (Session session = driver.session()) {
            session.run("CREATE (f: Film {Title: $title, PublicationDate: $publicationDate})", parameters("title", title, "publicationDate", publicationDate.toString()));
            success = true;
        }

        return success;
    }

    @Override
    public void update(Long idFilm, String title, Date publicationDate) {
        try (Session session = driver.session()) {
            session.run("MATCH (f:Film) "
                    + "WHERE ID(f) = $id"
                    + "SET f.Title = $title, f.PublicationDate = $publicationDate "
                    + "RETURN f",
                    parameters("id", idFilm,
                            "title", title,
                            "publicationDate", publicationDate.toString()));
        }
    }

    @Override
    public void delete(Long idFilm) {

        try (Session session = driver.session()) {

            session.writeTransaction((Transaction t) -> deleteFilmRelationships(t, idFilm));
            session.writeTransaction((Transaction t) -> deleteFilmNode(t, idFilm));

        }

    }

    private static int deleteFilmRelationships(Transaction t, Long idFilm) {

        t.run("MATCH (f:Film)-[r]-() WHERE ID(f) = $id DELETE r",
                parameters("id", idFilm));

        return 1;

    }

    private static int deleteFilmNode(Transaction t, Long idFilm) {

        t.run("MATCH (f:Film) WHERE ID(f) = $id DELETE f",
                parameters("id", idFilm));

        return 1;

    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit, int skip) {
        Set<Film> filmSet = new LinkedHashSet<>();

        int queryLimit = 0;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        try (Session session = driver.session()) {

            StatementResult result = null;

            if (startDateFilter == null || endDateFilter == null) {
                result = session.run("MATCH (n:Film) "
                        + "WHERE n.Title =~ '.*" + titleFilter + ".*' "
                        + "RETURN n "
                        + "ORDER BY n.PublicationDate DESC "
                        + "SKIP " + skip + " "
                        + "LIMIT " + queryLimit);
            } else {
                if (titleFilter == null) {
                    result = session.run("MATCH (n:Film) "
                            + "AND n.PublicationDate <= " + endDateFilter + " "
                            + "AND n.PublicationDate >= " + startDateFilter + " "
                            + "RETURN n "
                            + "ORDER BY n.PublicationDate DESC "
                            + "SKIP " + skip + " "
                            + "LIMIT " + queryLimit);
                } else {
                    result = session.run("MATCH (n:Film) "
                            + "WHERE n.Title =~ '.*" + titleFilter + ".*' "
                            + "AND n.PublicationDate <= " + endDateFilter + " "
                            + "AND n.PublicationDate >= " + startDateFilter + " "
                            + "RETURN n "
                            + "ORDER BY n.PublicationDate DESC "
                            + "SKIP " + skip + " "
                            + "LIMIT " + queryLimit);
                }
            }

            while (result.hasNext()) {
                Record record = result.next();

                Film film = getFilmFromRecord(record);

                filmSet.add(film);
            }
        }

        return filmSet;
    }

    @Override
    public void follow(Film film, User user) {
        try (Session session = driver.session()) {
            session.run("MATCH (u:User),(f:Film) "
                    + "WHERE ID(u) = " + user.getId() + " "
                    + "AND ID(f) = " + film.getId() + " "
                    + "CREATE (u)-[r:FOLLOWS]->(f) "
                    + "RETURN r");

        }
    }

    @Override
    public boolean isFollowing(Film film, User user) {
        StatementResult result = null;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[r:FOLLOWS]->(f:Film) "
                    + "WHERE ID(u) = " + user.getId() + " "
                    + "AND ID(f) = " + film.getId() + " "
                    + "RETURN r");

        }

        return result.hasNext();
    }

    @Override
    public void unfollow(Film film, User user) {

        try (Session session = driver.session()) {
            session.run("MATCH (u:User)-[r:FOLLOWS]->(f:Film) "
                    + "WHERE ID(u) = " + user.getId() + " "
                    + "AND ID(f) = " + film.getId() + " "
                    + "DELETE r");
        }
    }

    @Override
    public long countFollowers(Film film) {

        StatementResult result = null;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[r:FOLLOWS]->(f:Film) "
                    + "WHERE ID(f) = " + film.getId() + " "
                    + "RETURN count(DISTINCT u) AS followers");

        }

        return result.next().get("followers").asLong();

    }

    @Override
    public Set<User> getFollowers(Film film) {

        Set<User> userSet = new LinkedHashSet<>();
        StatementResult result = null;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[r:FOLLOWS]->(f:User) "
                    + "WHERE ID(f) = " + film.getId() + " "
                    + "RETURN u");

        }

        while (result.hasNext()) {
            userSet.add(DBManager.userManager.getUserFromRecord(result.next()));
        }

        return userSet;

    }

    @Override
    public Set<Film> getFriendCommentedFilms(User user, int limit) {

        int queryLimit = 0;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<Film> filmSet = new LinkedHashSet<>();
        StatementResult result = null;

        try (Session session = driver.session()) {

            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:CREATED]->(:Post)-[:TAGS]->(n:Film) "
                    + "WHERE ID(u1) = " + user.getId() + " "
                    + "AND NOT (u1)-[:FOLLOWS]->(n) "
                    + "AND NOT (u2)-[:FOLLOWS]->(n) "
                    + "RETURN n "
                    + "ORDER BY n.PublicationDate DESC "
                    + "LIMIT " + queryLimit);

        }

        while (result.hasNext()) {
            Film film = getFilmFromRecord(result.next());
            film.setType("FRIEND COMMENTED");
            filmSet.add(film);
        }

        return filmSet;

    }

    @Override
    public Set<Film> getSuggestedFilms(User user, int limit) {

        int queryLimit = 0;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<Film> filmSet = new LinkedHashSet<>();
        StatementResult result = null;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(n:Film) "
                    + "WHERE ID(u1) = " + user.getId() + " "
                    + "AND NOT (u1)-[:FOLLOWS]->(n) "
                    + "RETURN n "
                    + "ORDER BY n.PublicationDate DESC "
                    + "LIMIT " + queryLimit);

        }

        while (result.hasNext()) {
            Film film = getFilmFromRecord(result.next());
            film.setType("SUGGESTED");
            filmSet.add(film);
        }

        return filmSet;

    }

    @Override
    public Set<Film> getVerySuggestedFilms(User user, int limit) {

        int queryLimit = 0;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<Film> filmSet = new LinkedHashSet<>();
        StatementResult result = null;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(n:Film) "
                    + "WHERE ID(u1) = " + user.getId() + " "
                    + "AND NOT (u1)-[:FOLLOWS]->(n) "
                    + "AND (u2)-[:CREATED]->(:Post)-[:TAGS]->(n) "
                    + "RETURN n "
                    + "ORDER BY n.PublicationDate DESC "
                    + "LIMIT " + queryLimit);

        }

        while (result.hasNext()) {
            Film film = getFilmFromRecord(result.next());
            film.setType("VERY SUGGESTED");
            filmSet.add(film);
        }

        return filmSet;

    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public Set<Post> getRelatedPosts(Film film, int limit, long skip) {

        Set<Post> postSet = new LinkedHashSet<>();
        StatementResult result = null;

        try (Session session = driver.session()) {

            result = session.run("MATCH (n:Post)-[:TAGS]->(f:Film) "
                    + "WHERE ID(f) = " + film.getId() + " "
                    + "RETURN n "
                    + "SKIP " + skip + " "
                    + "LIMIT " + limit);

        } catch (Exception ex) {
            System.out.println("Related Posts retrieval error: " + ex.getLocalizedMessage());
        }

        while (result.hasNext()) {
            postSet.add(DBManager.postManager.getPostFromRecord(result.next()));
        }

        return postSet;
    }

}
