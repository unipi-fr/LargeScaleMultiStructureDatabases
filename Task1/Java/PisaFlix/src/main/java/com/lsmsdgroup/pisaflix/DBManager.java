package com.lsmsdgroup.pisaflix;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {

    private static EntityManagerFactory factory;
    private static EntityManager entityManager;

    public static void setup() {
        factory = Persistence.createEntityManagerFactory("pisaflixdb");

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
                List<User> users = entityManager.createQuery("SELECT u FROM com.lsmsdgroup.pisaflix.User u").getResultList();
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

        public static void read(long filmId) {
            // code to get a user
            System.out.println("Getting a User");

            try {
                entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Film film = entityManager.find(Film.class, filmId);
                if (film == null) {
                    System.out.println("film not found!");
                } else {
                    System.out.println(film.toString());
                    System.out.println("Film retrieved");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                System.out.println("A problem occurred in retriving a film!");
            } finally {
                entityManager.close();
            }
        }

    }

}
