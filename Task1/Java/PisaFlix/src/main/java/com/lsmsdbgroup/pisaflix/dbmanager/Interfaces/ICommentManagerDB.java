/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public interface ICommentManagerDB {

    void createFilmComment(String text, User user, Film film);

    void createCinemaComment(String text, User user, Cinema cinema);

    void update(Comment comment, String text);

    void delete(int idComment);

    Comment getById(int commentId);

    }
