/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.PisaFlix;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.IUserManagerDB;



public class UserManager implements IUserManagerDB {
    private EntityManagerFactory factory;
    private EntityManager entityManager;
    
    private static UserManager um;
    
    public static UserManager getIstance(){
        if(um==null){
            um = new UserManager();
        }
        
        return um;
    }
    
    private UserManager(){
        factory = PisaFlix.getEntityManagerFactory();
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

        public void update(User u) {
            update(u.getIdUser(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(), u.getPrivilegeLevel());
        }

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

    }
