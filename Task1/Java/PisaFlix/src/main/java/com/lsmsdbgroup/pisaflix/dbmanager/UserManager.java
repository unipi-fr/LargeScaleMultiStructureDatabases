package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import javax.persistence.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;

public class UserManager implements UserManagerDatabaseInterface {

    private final EntityManagerFactory factory;
    private EntityManager entityManager;

    private static UserManager userManager;

    public static UserManager getIstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }

        return userManager;
    }

    private UserManager() {
        factory = DBManager.getEntityManagerFactory();
    }

    @Override
    public User getById(int userId) {
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

    @Override
    public void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
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

    @Override
    public void updateFavorites(User user) {
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

    @Override
    public void delete(int userId) {
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

    @Override
    public void clearCinemaSetAndFilmSet(User user) {
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

    @Override
    public void update(User u) {
        update(u.getIdUser(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(), u.getPrivilegeLevel());
    }

    @Override
    public void update(int userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
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

    @Override
    public Set<User> getAll() {
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

    @Override
    public Set<User> getByUsername(String username) {
        Set<User> users = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
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

    @Override
    public Set<User> getByEmail(String email) {
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

    @Override
    public boolean checkDuplicates(String username, String email) {
        return !(getByUsername(username).isEmpty() && getByEmail(email).isEmpty());
    }

    @Override
    public Set<User> getFiltered(String nameFilter) {
        Set<User> users = null;
        String name = "";

        if (nameFilter != null) {
            name = nameFilter;
        }

        String query = "SELECT u "
                + "FROM User u "
                + "WHERE ('" + name + "'='' OR u.username LIKE '%" + name + "%') ";

        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            users = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
            if (users == null) {
                System.out.println("Users are empty!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve users filtered!");
        } finally {
            entityManager.close();
        }
        return users;
    }
}
