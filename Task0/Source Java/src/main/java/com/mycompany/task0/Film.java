
package com.mycompany.task0;

import java.util.Date;


public class Film {
    
    public int id;
    public String title, category;
    public Date publishDate;
    
    public Film(int id, String title, Date publishDate, String category){
        
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.category = category;
    }
    
}
