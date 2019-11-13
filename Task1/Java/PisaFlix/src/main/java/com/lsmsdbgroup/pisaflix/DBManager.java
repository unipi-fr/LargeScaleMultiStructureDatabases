package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import javax.persistence.*;

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
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in updating favorites!");
            } finally {
                entityManager.close();
            }
        }

        public static void delete(int userId) {
            clearCinemaSetAndFilmSet(getById(userId));
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                User reference = entityManager.getReference(User.class, userId);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in removing a User!");
            } finally {
                entityManager.close();
            }
        }

        public static void clearCinemaSetAndFilmSet(User user) {
            user.setCinemaSet(new LinkedHashSet<>());
            user.setFilmSet(new LinkedHashSet<>());
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(user);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred clearing the user's cinemaset and filmset!");
            } finally {
                entityManager.close();
            }
        }

        static void update(User u) {
            update(u.getIdUser(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(), u.getPrivilegeLevel());
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
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in updating a user!");
            } finally {
                entityManager.close();
            }
        }

        public static Set<User> getAll() {
            // code to retrieve all users
            System.out.println("Retrieving users");
            Set<User> users = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                users = new LinkedHashSet<>(entityManager.createQuery("FROM User").getResultList());
                if (users == null) {
                    System.out.println("User is empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }
            return users;
        }

        public static Set<User> getByUsername(String username) {
            Set<User> users = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                //TODO: da vedere se è sicuro <- SQLInjection
                users = new LinkedHashSet<>(entityManager.createQuery("SELECT u FROM User u WHERE u.username = '" + username + "'").getResultList());
                if (users == null) {
                    System.out.println("Users is empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }
            return users;
        }
        
        public static Set<User> getByEmail(String email) {
            Set<User> users = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();

                users = new LinkedHashSet<>(entityManager.createQuery("SELECT u FROM User u WHERE u.email = '" + email + "'").getResultList());
                if (users == null) {
                    //System.out.println("Users is empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }
            return users;
        }
        
        public static boolean checkDuplicates(String username, String email){
            if(getByUsername(username).isEmpty() && getByEmail(email).isEmpty()){
                return false;
            }
                return true;
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
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a film!");
            } finally {
                entityManager.close();
            }
            return film;
        }

        public static Set<Film> getAll() {
            Set<Film> films = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                films = new LinkedHashSet<>(entityManager.createQuery("FROM Film").getResultList());
                if (films == null) {
                    System.out.println("Film is empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in updating the film!");
            } finally {
                entityManager.close();
            }
        }

        public static void delete(int idFilm) {
            clearUserSet(getById(idFilm));
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Film reference = entityManager.getReference(Film.class, idFilm);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in deleting the film!");
            } finally {
                entityManager.close();
            }
        }

        public static void clearUserSet(Film film) {
            film.setUserSet(new LinkedHashSet<>());
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(film);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred clearing the film's userset!");
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
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred updating favorite films!");
            } finally {
                entityManager.close();
            }
        }

        static Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter) {
            Set<Film> films = null;
            String title = "";
            Calendar calendar = Calendar.getInstance();
            String startDate = "0000-01-01";
            String endDate = "9999-12-31";
            if (titleFilter != null) {
                title = titleFilter;
            }
            if (startDateFilter != null) {
                startDate = startDateFilter.toString();
            }
            if (endDateFilter != null) {
                endDate = endDateFilter.toString();
            }

            String query = "SELECT f "
                    + "FROM Film f "
                    + "WHERE ('" + title + "'='' OR f.title LIKE '%" + title + "%') "
                    + "AND (publicationDate between '" + startDate + " 00:00:00' and '" + endDate + " 23:59:59') ";

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                films = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
                if (films == null) {
                    System.out.println("Films are empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retrieve films filtered!");
            } finally {
                entityManager.close();
            }
            return films;
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
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a film!");
            } finally {
                entityManager.close();
            }
            return cinema;
        }
        
        static Set<Cinema> getFiltered(String nameFilter, String addressFilter) {
            Set<Cinema> cinemas = null;
            String name = "";
            String address = "";
            if (nameFilter != null) {
                name = nameFilter;
            }
            if (addressFilter != null) {
                address = addressFilter;
            }

            String query = "SELECT c "
                    + "FROM Cinema c "
                    + "WHERE ('" + name + "'='' OR c.name LIKE '%" + name + "%') "
                    + "AND ('" + address + "'='' OR c.address LIKE '%" + address + "%') ";

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                cinemas = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
                if (cinemas == null) {
                    System.out.println("cinemas are empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retrieve cinemas filtered!");
            } finally {
                entityManager.close();
            }
            return cinemas;
        }

        public static void delete(int idCinema) {
            clearUserSet(getById(idCinema));
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Cinema reference = entityManager.getReference(Cinema.class, idCinema);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in removing a Cinema!");
            } finally {
                entityManager.close();
            }
        }

        public static void clearUserSet(Cinema cinema) {
            cinema.setUserSet(new LinkedHashSet<>());
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(cinema);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred clearing the cinema's userset!");
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
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in updating the film!");
            } finally {
                entityManager.close();
            }
        }

        public static Set<Cinema> getAll() {
            Set<Cinema> cinemas = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                cinemas = new LinkedHashSet<>(entityManager.createQuery("FROM Cinema").getResultList());
                if (cinemas == null) {
                    System.out.println("Cinema is empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retrieve all cinemas!");
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
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred updating favorite cinemas!");
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

            user.getCommentSet().add(comment);
            film.getCommentSet().add(comment);
            Set<Film> filmSet = new LinkedHashSet<>();
            filmSet.add(film);
            comment.setFilmSet(filmSet);
            comment.setIdUser(user);

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(comment);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in creating the comment!");
            } finally {
                entityManager.close();
            }
        }

        public static void createCinemaComment(String text, User user, Cinema cinema) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setTimestamp(new Date());

            user.getCommentSet().add(comment);
            cinema.getCommentSet().add(comment);
            Set<Cinema> cinemaSet = new LinkedHashSet<>();
            cinemaSet.add(cinema);
            comment.setCinemaSet(cinemaSet);
            comment.setIdUser(user);

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(comment);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in creating the comment!");
            } finally {
                entityManager.close();
            }
        }

        public static void update(Comment comment, String text) {
            comment.setText(text);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(comment);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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
                if(!reference.getCinemaSet().isEmpty()){
                    reference.getCinemaSet().iterator().next().getCommentSet().remove(reference);
                }   
                if(!reference.getFilmSet().isEmpty()){
                    reference.getFilmSet().iterator().next().getCommentSet().remove(reference);
                }              
                reference.getIdUser().getCommentSet().remove(reference);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
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
            cinema.getProjectionSet().add(projection);
            film.getProjectionSet().add(projection);
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(projection);
                entityManager.merge(cinema);
                entityManager.merge(film);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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
                reference.getIdCinema().getProjectionSet().remove(reference);
                reference.getIdFilm().getProjectionSet().remove(reference);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in updating the projection!");
            } finally {
                entityManager.close();
            }
        }

        public static Set<Projection> getAll() {
            Set<Projection> projections = null;
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                projections = new LinkedHashSet<>(entityManager.createQuery("FROM projection").getResultList());
                if (projections == null) {
                    System.out.println("Projection is empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a projection!");
            } finally {
                entityManager.close();
            }
            return projection;
        }

        public static Set<Projection> queryProjection(int cinemaId, int filmId, String date) {
            Set<Projection> projections = null;

            String query = "SELECT p "
                    + "FROM Projection p "
                    + "WHERE ((" + cinemaId + " = -1) OR ( " + cinemaId + " = p.idCinema)) "
                    + "AND ((" + filmId + " = -1) OR ( " + filmId + " = p.idFilm)) "
                    + "AND (('" + date + "' = 'all') OR dateTime between '" + date + " 00:00:00' and '" + date + " 23:59:59')";

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                projections = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
                if (projections == null) {
                    System.out.println("Users is empty!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }

            return projections;
        }

    }
}
