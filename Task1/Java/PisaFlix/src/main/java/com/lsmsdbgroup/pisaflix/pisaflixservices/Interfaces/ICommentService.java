/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;

public interface ICommentService {
    void addFilmComment(String comment, User user, Film film);
    
    void addCinemaComment(String comment, User user, Cinema cinema);
    
    void update(Comment comment);

    Comment getById(int id);

    void delete(int id);
    }
