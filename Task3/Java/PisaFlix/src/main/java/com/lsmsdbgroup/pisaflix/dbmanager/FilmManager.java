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

        Film film = null;

        try {

            Value value = record.get("f");

            Long id = value.asNode().id();
            String title = value.get("Title").asString();
            String publicationDateStr = value.get("PublicationDate").asString();
            String wikiPage = value.get("WikiPage").asString();
            Date publicationDate = DateConverter.StringToDate(publicationDateStr);

            film = new Film(id, title, publicationDate, wikiPage);

        } catch (Exception ex) {
            System.out.println("Film from record error: " + ex.getLocalizedMessage());
            System.out.println("Record not convertible: " + record);
        }

        return film;
    }

    @Override
    public Film getById(Long filmId) {
        Film film = null;

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (f:Film) WHERE ID(f) = $id RETURN f", parameters("id", filmId));

            while (result.hasNext()) {
                Record record = result.next();

                film = getFilmFromRecord(record);
            }
        } catch (Exception ex) {
            System.out.println("Post retrieval error: " + ex.getLocalizedMessage());
        }

        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> filmSet = new LinkedHashSet<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (f:Film) RETURN f LIMIT " + limit);

            while (result.hasNext()) {
                Record record = result.next();

                Film film = getFilmFromRecord(record);

                filmSet.add(film);
            }
        } catch (Exception ex) {
            System.out.println("All films retrieval error: " + ex.getLocalizedMessage());
        }

        return filmSet;
    }

    @Override
    public boolean create(String title, Date publicationDate) {
        boolean success = false;

        try (Session session = driver.session()) {
            session.run("CREATE (f: Film {Title: $title, PublicationDate: $publicationDate})", parameters("title", title, "publicationDate", publicationDate.toString()));
            success = true;
        } catch (Exception ex) {
            System.out.println("Create film error: " + ex.getLocalizedMessage());
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
        } catch (Exception ex) {
            System.out.println("Update film error: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public void delete(Long idFilm) {

        try (Session session = driver.session()) {

            session.writeTransaction((Transaction t) -> deleteFilmRelationships(t, idFilm));
            session.writeTransaction((Transaction t) -> deleteFilmNode(t, idFilm));

        } catch (Exception ex) {
            System.out.println("Delete film error: " + ex.getLocalizedMessage());
        }

    }

    private static int deleteFilmRelationships(Transaction transaction, Long idFilm) {

        transaction.run("MATCH (f:Film)-[r]-() WHERE ID(f) = $id DELETE r",
                parameters("id", idFilm));

        return 1;

    }

    private static int deleteFilmNode(Transaction transaction, Long idFilm) {

        transaction.run("MATCH (f:Film) WHERE ID(f) = $id DELETE f",
                parameters("id", idFilm));

        return 1;

    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit, int skip) {
        Set<Film> filmSet = new LinkedHashSet<>();

        int queryLimit;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        try (Session session = driver.session()) {

            StatementResult result;

            if (startDateFilter == null || endDateFilter == null) {
                result = session.run("MATCH (f:Film) "
                        + "WHERE f.Title =~ $titleFilter "
                        + "RETURN f "
                        + "ORDER BY f.PublicationDate DESC "
                        + "SKIP $skip "
                        + "LIMIT $limit",
                        parameters("skip", skip, "limit", queryLimit, "titleFilter", ".*" + titleFilter + ".*"));
            } else {
                if (titleFilter == null) {
                    result = session.run("MATCH (f:Film) "
                            + "WHERE f.PublicationDate <= $endDateFilter "
                            + "AND f.PublicationDate >= $startDateFilter "
                            + "RETURN f "
                            + "ORDER BY f.PublicationDate DESC "
                            + "SKIP $skip "
                            + "LIMIT $limit",
                            parameters("skip", skip, "limit", queryLimit, "endDateFilter", endDateFilter, "$startDateFilter", startDateFilter));
                } else {
                    result = session.run("MATCH (f:Film) "
                            + "WHERE f.Title =~ $titleFilter "
                            + "AND f.PublicationDate <= $endDateFilter "
                            + "AND f.PublicationDate >= $startDateFilter "
                            + "RETURN f "
                            + "ORDER BY f.PublicationDate DESC "
                            + "SKIP $skip "
                            + "LIMIT $limit",
                            parameters("skip", skip, "limit", queryLimit, "endDateFilter", endDateFilter, "$startDateFilter", startDateFilter, "titleFilter", ".*" + titleFilter + ".*"));
                }
            }

            while (result.hasNext()) {
                Record record = result.next();

                Film film = getFilmFromRecord(record);

                filmSet.add(film);
            }
        } catch (Exception ex) {
            System.out.println("Filtered films retrieval error: " + ex.getLocalizedMessage());
        }

        return filmSet;
    }

    @Override
    public void follow(Film film, User user) {
        try (Session session = driver.session()) {
            session.run("MATCH (u:User),(f:Film) "
                    + "WHERE ID(u) = $userId "
                    + "AND ID(f) = $filmId "
                    + "CREATE (u)-[r:FOLLOWS]->(f) "
                    + "RETURN r",
                    parameters("userId", user.getId(), "filmId", film.getId()));

        } catch (Exception ex) {
            System.out.println("Follow error: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public boolean isFollowing(Film film, User user) {
        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[r:FOLLOWS]->(f:Film) "
                    + "WHERE ID(u) = $userId "
                    + "AND ID(f) = $filmId "
                    + "RETURN r",
                    parameters("userId", user.getId(), "filmId", film.getId()));

        } catch (Exception ex) {
            System.out.println("Is Following? error: " + ex.getLocalizedMessage());
            return false;
        }

        return result.hasNext();
    }

    @Override
    public void unfollow(Film film, User user) {

        try (Session session = driver.session()) {
            session.run("MATCH (u:User)-[r:FOLLOWS]->(f:Film) "
                    + "WHERE ID(u) = $userId "
                    + "AND ID(f) = $filmId "
                    + "DELETE r",
                    parameters("userId", user.getId(), "filmId", film.getId()));
        } catch (Exception ex) {
            System.out.println("Unfollow error: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public long countFollowers(Film film) {

        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[r:FOLLOWS]->(f:Film) "
                    + "WHERE ID(f) = $filmId "
                    + "RETURN count(DISTINCT u) AS followers",
                    parameters("filmId", film.getId()));

        } catch (Exception ex) {
            System.out.println("Followers count error: " + ex.getLocalizedMessage());
            return (long)0;
        }

        return result.next().get("followers").asLong();

    }

    @Override
    public Set<User> getFollowers(Film film) {

        Set<User> userSet = new LinkedHashSet<>();
        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u:User)-[r:FOLLOWS]->(f:User) "
                    + "WHERE ID(f) = $filmId "
                    + "RETURN u",
                    parameters("filmId", film.getId()));

        } catch (Exception ex) {
            System.out.println("Followers retrieval error: " + ex.getLocalizedMessage());
            return userSet; //EMPTY!!!
        }

        while (result.hasNext()) {
            userSet.add(DBManager.userManager.getUserFromRecord(result.next()));
        }

        return userSet;

    }

    @Override
    public Set<Film> getFriendCommentedFilms(User user, int limit) {

        int queryLimit;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<Film> filmSet = new LinkedHashSet<>();
        StatementResult result;

        try (Session session = driver.session()) {

            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:CREATED]->(:Post)-[:TAGS]->(f:Film) "
                    + "WHERE ID(u1) = $userId "
                    + "AND NOT (u1)-[:FOLLOWS]->(f) "
                    + "AND NOT (u2)-[:FOLLOWS]->(f) "
                    + "RETURN f "
                    + "ORDER BY f.PublicationDate DESC "
                    + "LIMIT $limit",
                    parameters("userId", user.getId(), "limit", queryLimit));

        } catch (Exception ex) {
            System.out.println("Films commented by friends retrieval error: " + ex.getLocalizedMessage());
        return filmSet; //EMPTY!!!
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

        int queryLimit;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<Film> filmSet = new LinkedHashSet<>();
        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(f:Film) "
                    + "WHERE ID(u1) = $userId "
                    + "AND NOT (u1)-[:FOLLOWS]->(f) "
                    + "RETURN f "
                    + "ORDER BY f.PublicationDate DESC "
                    + "LIMIT $limit",
                    parameters("userId", user.getId(), "limit", queryLimit));

        } catch (Exception ex) {
            System.out.println("Suggested films retrieval error: " + ex.getLocalizedMessage());
            return filmSet; //EMPTY!!!
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

        int queryLimit;
        if (limit == 0) {
            queryLimit = this.limit;
        } else {
            queryLimit = limit;
        }

        Set<Film> filmSet = new LinkedHashSet<>();
        StatementResult result;

        try (Session session = driver.session()) {
            result = session.run("MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(f:Film) "
                    + "WHERE ID(u1) = $userId "
                    + "AND NOT (u1)-[:FOLLOWS]->(f) "
                    + "AND (u2)-[:CREATED]->(:Post)-[:TAGS]->(f) "
                    + "RETURN f "
                    + "ORDER BY f.PublicationDate DESC "
                    + "LIMIT $limit",
                    parameters("userId", user.getId(), "limit", queryLimit));

        } catch (Exception ex) {
            System.out.println("Very suggested films retrieval error: " + ex.getLocalizedMessage());
            return filmSet; //EMPTY!!!
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
        StatementResult result;

        try (Session session = driver.session()) {

            result = session.run("MATCH (p:Post)-[:TAGS]->(f:Film) "
                    + "WHERE ID(f) = $filmId "
                    + "RETURN p "
                    + "SKIP $skip "
                    + "LIMIT $limit",
                    parameters("filmId", film.getId(), "limit", limit, "skip", skip));

        } catch (Exception ex) {
            System.out.println("Related films retrieval error: " + ex.getLocalizedMessage());
            return postSet; //EMPTY!!!
        }

        while (result.hasNext()) {
            postSet.add(DBManager.postManager.getPostFromRecord(result.next()));
        }

        return postSet;
    }

}
