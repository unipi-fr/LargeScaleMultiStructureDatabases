package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UserManagerKV extends KeyValueDBManager implements UserManagerDatabaseInterface{
    
    private final EntityManagerFactory factory;
    private EntityManager entityManager;

    private static UserManagerKV userManager;

    public static UserManagerKV getIstance() {
        if (userManager == null) {
            userManager = new UserManagerKV();
        }
        return userManager;
    }

    private UserManagerKV() {
        factory = DBManager.getEntityManagerFactory();
        super.settings();
    }

    // Da Controllare
    @Override
    public User getById(int userId) {
        User user = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            user = entityManager.find(User.class, userId);
            if (user == null) {
                System.out.println("User not found!");
            }
            // devo implementare un nuovo metodo nel comment manager
            // che recupera il set di cinema preferiti dell'utente
            // probabilmente dovrò estendere la parte KV per aggiungere
            // altri indici associati ad ogni id utente.
            
            //user.setCommentSet();
            
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

    // penso che vada bene così
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

    // Da Controllare
    @Override
    public void delete(int userId){
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

    // Da Controllare
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

    // Da Controllare
    @Override
    public void update(User u) {
        update(u.getIdUser(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(), u.getPrivilegeLevel());
    }

    // Da Controllare
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

    // Da Controllare
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

    // Da Controllare
    @Override
    public Set<User> getByUsername(String username) {
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

    // Da Controllare
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

    // Da Controllare
    @Override
    public boolean checkDuplicates(String username, String email) {
        return !(getByUsername(username).isEmpty() && getByEmail(email).isEmpty());
    }

    // Da Controllare
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
