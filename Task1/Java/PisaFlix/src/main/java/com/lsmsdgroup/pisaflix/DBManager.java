package com.lsmsdgroup.pisaflix;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

        public static void read(int userId) {
            // code to get a user
            System.out.println("Getting a User");

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                User user = entityManager.find(User.class, userId);
                if (user == null) {
                    System.out.println("User not found!");
                } else {
                    System.out.println(user.toString());
                    System.out.println("User retrieved");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }
        }

        public static void create(String username, String password, int privilegeLevel) {
            // code to create a user
            System.out.println("Creating a new user");

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPrivilegeLevel(privilegeLevel);

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(user);
                entityManager.getTransaction().commit();

                System.out.println("User Added");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the user!");
            } finally {
                entityManager.close();

            }
        }

        public static void delete(int userId) {
            // code to delete a user
            System.out.println("Removing a User");

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                User reference = entityManager.getReference(User.class, userId);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();

                System.out.println("User removed");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in removing a User!");
            } finally {
                entityManager.close();
            }
        }

        public static void update(int userId, String username, String password, int privilegeLevel) {
            // code to update a user
            System.out.println("Updating a user");

            User user = new User(userId);
            user.setUsername(username);
            user.setPassword(password);
            user.setPrivilegeLevel(privilegeLevel);

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(user);
                entityManager.getTransaction().commit();

                System.out.println("User updated");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in updating a user!");

            } finally {
                entityManager.close();
            }
        }

        public static List<User> getAllUsers() {
            // code to retrieve all users
            System.out.println("Retrieving users");

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                List<User> users = entityManager.createQuery("SELECT u FROM User u").getResultList();
                if (users == null) {
                    System.out.println("No user present!");
                } else {
                    System.out.println(users.toString());
                    System.out.println("Users retrieved");
                    return users;
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a user!");
            } finally {
                entityManager.close();
            }

            return null;
        }

    }

    public static class FilmManager {

        public static Film getById(int filmId){
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
        
        public static List<Film> getAll(){
            List<Film> films = null;
            
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                
                films = entityManager.createQuery("FROM Film").getResultList();
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            } finally {
                entityManager.close();
            }
            
            return films;
        }
        
        public static void add(String title, Date publicationDate, String description){
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
            } finally {
                entityManager.close();
            }
        }
        
        public static void update(int idFilm, String title, Date publicationDate, String description){
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
            } finally {
                entityManager.close();
            }
        }
        
        public static void delete(int idFilm){
            
            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Film reference = entityManager.getReference(Film.class, idFilm);
                entityManager.remove(reference);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                entityManager.close();
            } 
        }

    }
    
    public static class CinemaManager{
        
        public static void create(String name, String address) {
            // code to create a cinema
            System.out.println("Creating a new cinema");

            Cinema cinema = new Cinema();
            cinema.setName(name);
            cinema.setAddress(address);

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(cinema);
                entityManager.getTransaction().commit();

                System.out.println("Cinema Added");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("A problem occurred in creating the cinema!");
            } finally {
                entityManager.close();

            }
        }
        
    }

}
