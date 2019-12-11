package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
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

    
    @Override
    public User getById(int userId, boolean retreiveComments) {
        User user = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            user = entityManager.find(User.class, userId);
            if (user == null) {
                System.out.println("User not found!");
            }
            else{
                if(retreiveComments)
                    user.setCommentSet(retreiveCommentsOfUser(user.getIdUser()));
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

    // recupera tutti i commenti associati all'utente
    private Set<Comment> retreiveCommentsOfUser(int idUser){
    
         Set<Comment> returnSet = new LinkedHashSet<>();
        
        int lastCommentId = Integer.parseInt(get("setting:lastCommentKey"));
    
        for(int i = 1; i <= lastCommentId; i++){
        
            String stringUserId = get("comment:" + i + ":user");
            
            if(stringUserId == null) continue;
            
            int idUserRed = Integer.parseInt(stringUserId);
            
            if(idUserRed == idUser){
                Comment commentToAdd = CommentManagerKV.getIstance().getById(i);
                if (commentToAdd != null)
                    returnSet.add(commentToAdd);
            }
        }
        
        return returnSet;
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

    // penso che vada bene
    @Override
    public void delete(int userId){
        clearCinemaSetAndFilmSet(getById(userId, false));
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

    // penso che vada bene
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

    // Da Controllare
    @Override
    public Set<User> getAll() {
        System.out.println("Retrieving users");
        Set<User> users = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            users = new LinkedHashSet<>(entityManager.createQuery("FROM User").getResultList());
            
            for(User user: users){
                user.setCommentSet(retreiveCommentsOfUser(user.getIdUser()));
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
            for(User user: users){
                user.setCommentSet(retreiveCommentsOfUser(user.getIdUser()));
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
            for(User user: users){
                user.setCommentSet(retreiveCommentsOfUser(user.getIdUser()));
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
            for(User user: users){
                user.setCommentSet(retreiveCommentsOfUser(user.getIdUser()));
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
