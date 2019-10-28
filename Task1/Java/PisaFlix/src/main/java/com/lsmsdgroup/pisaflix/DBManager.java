package com.lsmsdgroup.pisaflix;

import com.lsmsdgroup.pisaflix.Entities.Comment;
import com.lsmsdgroup.pisaflix.Entities.Projection;
import com.lsmsdgroup.pisaflix.Entities.Cinema;
import com.lsmsdgroup.pisaflix.Entities.Film;
import com.lsmsdgroup.pisaflix.Entities.User;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.hibernate.Session;

public class DBManager {

    private static EntityManagerFactory factory;
    private static EntityManager entityManager;

    public static void setup() {
        factory = Persistence.createEntityManagerFactory("PisaFlix");

    }

    public static void exit() {
        factory.close();
    }

    public static class UserManager {

        public static User getById(int userId) {
            // code to get a user
            User user = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                user = entityManager.find(User.class, userId);
                if (user == null) {
                    System.out.println("User not found!");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }
            return user;
        }

        public static void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
            // code to create a user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPrivilegeLevel(privilegeLevel);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(user);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the user!");
            } finally {
                entityManager.close();
            }
        }

        public static void updateFavorites(User user) {
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(user);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in updating favorites!");
            } finally {
                entityManager.close();
            }
        }

        public static void delete(int userId) {
            // code to delete a user
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                User reference = entityManager.getReference(User.class, userId);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in removing a User!");
            } finally {
                entityManager.close();
            }
        }

        public static void update(int userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
            // code to update a user
            User user = new User(userId);
            user.setUsername(username);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPrivilegeLevel(privilegeLevel);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(user);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in updating a user!");

            } finally {
                entityManager.close();
            }
        }

        public static List<User> getAll() {
            // code to retrieve all users
            System.out.println("Retrieving users");
            List<User> users = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                users = entityManager.createQuery("FROM User").getResultList();
                if (users == null) {
                    System.out.println("User is empty!");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }
            return users;
        }

        public static List<User> getByUsername(String username) {
            List<User> users = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                //TODO: da vedere se è sicuro <- SQLInjection
                users = entityManager.createQuery("SELECT u FROM User u WHERE u.username = '" + username + "'").getResultList();
                if (users == null) {
                    System.out.println("Users is empty!");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }
            return users;
        }

    }

    public static class FilmManager {

        public static Film getById(int filmId) {
            Film film = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                film = entityManager.find(Film.class, filmId);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a film!");
            } finally {
                entityManager.close();
            }
            return film;
        }

        public static List<Film> getAll() {
            List<Film> films = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                films = entityManager.createQuery("FROM Film").getResultList();
                if (films == null) {
                    System.out.println("Film is empty!");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retrieve all films!");
            } finally {
                entityManager.close();
            }
            return films;
        }

        public static void create(String title, Date publicationDate, String description) {
            Film film = new Film();
            film.setTitle(title);
            film.setDescription(description);
            film.setPublicationDate(publicationDate);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(film);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the film!");
            } finally {
                entityManager.close();
            }
        }

        public static void update(int idFilm, String title, Date publicationDate, String description) {
            Film film = new Film(idFilm);
            film.setTitle(title);
            film.setDescription(description);
            film.setPublicationDate(publicationDate);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(film);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in updating the film!");
            } finally {
                entityManager.close();
            }
        }

        public static void delete(int idFilm) {
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Film reference = entityManager.getReference(Film.class, idFilm);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in deleting the film!");
            } finally {
                entityManager.close();
            }
        }

        public static void updateFavorites(Film film) {
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(film);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred updating favorites!");
            } finally {
                entityManager.close();
            }
        }

    }

    public static class CinemaManager {

        public static void create(String name, String address) {
            Cinema cinema = new Cinema();
            cinema.setName(name);
            cinema.setAddress(address);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(cinema);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the cinema!");
            } finally {
                entityManager.close();
            }
        }

        public static Cinema getById(int cinemaId) {
            Cinema cinema = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                cinema = entityManager.find(Cinema.class, cinemaId);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a film!");
            } finally {
                entityManager.close();
            }
            return cinema;
        }

        public static void delete(int idCinema) {
            // code to delete a cinema
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Cinema reference = entityManager.getReference(Cinema.class, idCinema);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in removing a Cinema!");
            } finally {
                entityManager.close();
            }
        }

        public static void update(int idCinema, String name, String address) {
            Cinema cinema = new Cinema(idCinema);
            cinema.setName(name);
            cinema.setAddress(address);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(cinema);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in updating the film!");
            } finally {
                entityManager.close();
            }
        }

        public static List<Cinema> getAll() {
            List<Cinema> cinemas = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                cinemas = entityManager.createQuery("FROM Film").getResultList();
                if (cinemas == null) {
                    System.out.println("Film is empty!");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retrieve all films!");
            } finally {
                entityManager.close();
            }
            return cinemas;
        }

        public static void updateFavorites(Cinema cinema) {
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(cinema);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred updating favorites!");
            } finally {
                entityManager.close();
            }
        }

    }

    public static class CommentManager {

        public static void createFilmComment(String text, User user, Film film) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setTimestamp(new Date());

            user.getCommentCollection().add(comment);
            film.getCommentCollection().add(comment);
            Collection<Film> filmCollection = new ArrayList<>();
            filmCollection.add(film);
            comment.setFilmCollection(filmCollection);
            comment.setIdUser(user);

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(comment);
                entityManager.merge(user);
                entityManager.merge(film);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the comment!");
            } finally {
                entityManager.close();
            }
        }

        public static void createCinemaComment(String text, User user, Cinema cinema) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setTimestamp(new Date());

            user.getCommentCollection().add(comment);
            cinema.getCommentCollection().add(comment);
            Collection<Cinema> cinemaCollection = new ArrayList<>();
            cinemaCollection.add(cinema);
            comment.setCinemaCollection(cinemaCollection);
            comment.setIdUser(user);

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(comment);
                entityManager.merge(user);
                entityManager.merge(cinema);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the comment!");
            } finally {
                entityManager.close();
            }
        }

        public static void update(int idComment, String text) {
            Comment comment = new Comment(idComment);
            comment.setText(text);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(comment);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in updating the Comment!");
            } finally {
                entityManager.close();
            }
        }

        public static void delete(int idComment) {
            // code to delete a cinema
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Comment reference = entityManager.getReference(Comment.class, idComment);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in removing the Comment!");
            } finally {
                entityManager.close();
            }
        }

        public static Comment getById(int commentId) {
            Comment comment = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                comment = entityManager.find(Comment.class, commentId);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a comment!");
            } finally {
                entityManager.close();
            }
            return comment;
        }

    }

    public static class ProjectionManager {

        public static void create(Date dateTime, int room, Film film, Cinema cinema) {
            Projection projection = new Projection();
            projection.setDateTime(dateTime);
            projection.setRoom(room);
            projection.setIdCinema(cinema);
            projection.setIdFilm(film);
            cinema.getProjectionCollection().add(projection);
            film.getProjectionCollection().add(projection);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(projection);
                entityManager.merge(cinema);
                entityManager.merge(film);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the projection!");
            } finally {
                entityManager.close();
            }
        }

        public static void delete(int idProjection) {
            // code to delete a cinema
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Projection reference = entityManager.getReference(Projection.class, idProjection);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in removing the Projection!");
            } finally {
                entityManager.close();
            }
        }

        public static void update(int idProjection, Date dateTime, int room) {
            Projection projection = new Projection(idProjection);
            projection.setDateTime(dateTime);
            projection.setRoom(room);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(projection);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in updating the projection!");
            } finally {
                entityManager.close();
            }
        }

        public static List<Projection> getAll() {
            List<Projection> projections = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                projections = entityManager.createQuery("FROM projection").getResultList();
                if (projections == null) {
                    System.out.println("Projection is empty!");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retrieve all projections!");
            } finally {
                entityManager.close();
            }
            return projections;
        }

        public static Projection getById(int projectionId) {
            Projection projection = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                projection = entityManager.find(Projection.class, projectionId);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a projection!");
            } finally {
                entityManager.close();
            }
            return projection;
        }

    }
}
