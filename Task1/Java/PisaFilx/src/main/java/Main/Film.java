/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.Date;
/**
 *
 * @author 
 */
public class Film {
    public int id;
    public String title, description;
    public Date publicationDate;
    
    public Film(int id, String title, Date publicationDate, String description){
        
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.description = description;
    }
}
