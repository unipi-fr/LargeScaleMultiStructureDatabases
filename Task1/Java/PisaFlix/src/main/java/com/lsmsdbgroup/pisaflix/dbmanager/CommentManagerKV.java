package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.iq80.leveldb.DBException;


public class CommentManagerKV extends KeyValueDBManager implements CommentManagerDatabaseInterface{
    
    
    private static CommentManagerKV commentManager;
    
    public static CommentManagerKV getIstance(){
    
        if(commentManager == null)
            commentManager = new CommentManagerKV();
        return commentManager;
    }
    
    
    private CommentManagerKV(){
        super.settings();
    }
    
    @Override
    public void createFilmComment(String text, User user, Film film) {
        
        // controllo dati inseriti
        if(text == null || user == null || film == null){
            System.err.println("the data given to createFilmComment is not valid");
            return;
        }
        // prendo l'id del prossimo commento da inserire
        int idComment = Integer.parseInt(get("setting:lastCommentKey")) + 1;
       
        // la chiave per accedere ai vari campi è, ad es "comment:2:user"
        put("comment:" + idComment + ":user", user.getIdUser().toString());
        put("comment:" + idComment + ":film", film.getIdFilm().toString());
        put("comment:" + idComment + ":text", text);
        put("comment:" + idComment + ":timestamp", dateFormat.format(new Date()));
        
        // controllo se esiste la lista dei commenti associati al film in questione
        String listaIdCommenti = get("film:" + film.getIdFilm().toString() + ":comments");
        
        if(listaIdCommenti == null){ // non esiste, me la creo
            put("film:" + film.getIdFilm().toString() + ":comments", String.valueOf(idComment));
            put("setting:lastCommentKey", String.valueOf(idComment));
            return;
        }
        
        // altrimenti mi limito ad appendere ":idComment"
        listaIdCommenti = listaIdCommenti.concat(":" + String.valueOf(idComment));
        put("film:" + film.getIdFilm().toString() + ":comments", listaIdCommenti);
        
        // aggiorno il contatore dell'id commento
        put("setting:lastCommentKey", String.valueOf(idComment));
    }

 
    @Override
    public void createCinemaComment(String text, User user, Cinema cinema) {
        
         // controllo dati inseriti
        if(text == null || user == null || cinema == null){
            System.err.println("the data given to createCinemaComment is not valid");
            return;
        }
        // prendo l'id del prossimo commento da inserire
        int idComment = Integer.parseInt(get("setting:lastCommentKey")) + 1;
       
        // la chiave per accedere ai vari campi è, ad es "comment:2:user"
        put("comment:" + idComment + ":user", user.getIdUser().toString());
        put("comment:" + idComment + ":cinema", cinema.getIdCinema().toString());
        put("comment:" + idComment + ":text", text);
        put("comment:" + idComment + ":timestamp", dateFormat.format(new Date()));
        
        // controllo se esiste la lista dei commenti associati al cinema in questione
        String listaIdCommenti = get("cinema:" + cinema.getIdCinema().toString() + ":comments");
        
        if(listaIdCommenti == null){ // non esiste, me la creo
            put("cinema:" + cinema.getIdCinema().toString() + ":comments", String.valueOf(idComment));
            put("setting:lastCommentKey", String.valueOf(idComment));
            return;
        }
        
        // altrimenti mi limito ad appendere ":idComment"
        listaIdCommenti = listaIdCommenti.concat(":" + String.valueOf(idComment));
        put("cinema:" + cinema.getIdCinema().toString() + ":comments", listaIdCommenti);
        
        // aggiorno il contatore dell'id commento
        put("setting:lastCommentKey", String.valueOf(idComment));
      
    }
    
  
    @Override
    public void update(Comment comment, String text) {
        
        if(comment == null)
            return;
        
        int commentId = comment.getIdComment();
        // esiste il testo del commento da modificare?
        String oldText = get("comment:" + commentId + ":text");
        if(text == null || oldText == null) {
            System.out.println("Comment: " + commentId + " not updated");
            return;
        }
        
        put("comment:" + commentId + ":text", text);
    }

 
    @Override
    public void delete(int commentId) {
        // recopero l'id del film del commento da eliminare perchè dovrò 
        // aggiornare la lista dei commenti assiciati ad esso
        String idFilm = get("comment:" + commentId + ":film");
        String idCinema = get("comment:" + commentId + ":cinema");
        if(idFilm != null) {
            deleteFilmComment(commentId, idFilm);
            return;
        }
        if(idCinema != null){
            deleteCinemaComment(commentId, idCinema);
            return;
        }
        
        System.err.println("Impossibile cancellare commento: " + commentId);
    }


    private void deleteFilmComment(int commentId, String idFilm){
        //il fallimento di anche solo una di queste indica inconsistenza del DB!!!
        try{
            delete("comment:" + commentId + ":user");
            delete("comment:" + commentId + ":film");
            delete("comment:" + commentId + ":text");
            delete("comment:" + commentId + ":timestamp");
        }catch (DBException ex){
            System.err.println("Error: impossible to delete one or more tuple of "
                    + "comment: " + commentId);
            return;
        }
        
        // prendo la lista dei commenti associati al film
        String commentiConcatenati = get("film:" + idFilm + ":comments");
     
        if(commentiConcatenati == null){
            System.err.println("Error: impossible to retreive the list of comments");
            return;
        }
        // lavoro solo sugli indici
        String[] arrayCommentiVecchi = commentiConcatenati.split(":");
        // non mi complico la vita, butto via tutto. Tanto nella createComment
        // ho già gestito il caso in cui non esista una lista dei commenti
        if(arrayCommentiVecchi.length == 1){ 
            delete("film:" + idFilm + ":comments");
            return;
        }
        // voglio eliminare l'id semplicemente -> serve una List
        List<String> ListaCommentiNuovi = new ArrayList<>(Arrays.asList(arrayCommentiVecchi));
        ListaCommentiNuovi.remove(String.valueOf(commentId));
        
        // ricreo la lista nel formato concatenato
        for(int i=0; i < ListaCommentiNuovi.size(); i++){
            
            if(i==0) commentiConcatenati = ListaCommentiNuovi.get(i);
            else commentiConcatenati = commentiConcatenati.concat(":" + ListaCommentiNuovi.get(i));
        }
        
        put("film:" + idFilm + ":comments", commentiConcatenati);
        
    }
    
   
    private void deleteCinemaComment(int commentId, String idCinema){
        //il fallimento di anche solo una di queste indica inconsistenza del DB!!!
        try{
            delete("comment:" + commentId + ":user");
            delete("comment:" + commentId + ":cinema");
            delete("comment:" + commentId + ":text");
            delete("comment:" + commentId + ":timestamp");
        }catch (DBException ex){
            System.err.println("Error: impossible to delete one or more tuple of "
                    + "comment: " + commentId);
            return;
        }
        // prendo la lista dei commenti associati al cinema
        String commentiConcatenati = get("cinema:" + idCinema + ":comments");
     
        if(commentiConcatenati == null){
            System.err.println("Error: impossible to retreive the list of comments");
            return;
        }
        // lavoro solo sugli indici
        String[] arrayCommentiVecchi = commentiConcatenati.split(":");
        // non mi complico la vita, butto via tutto. Tanto nella createComment
        // ho già gestito il caso in cui non esista una lista dei commenti
        if(arrayCommentiVecchi.length == 1){ 
            delete("cinema:" + idCinema + ":comments");
            return;
        }
        // voglio eliminare l'id semplicemente -> serve una List
        List<String> ListaCommentiNuovi = new ArrayList<>(Arrays.asList(arrayCommentiVecchi));
        ListaCommentiNuovi.remove(String.valueOf(commentId));
        
        // ricreo la lista nel formato concatenato
        for(int i=0; i < ListaCommentiNuovi.size(); i++){
            
            if(i==0) commentiConcatenati = ListaCommentiNuovi.get(i);
            else commentiConcatenati = commentiConcatenati.concat(":" + ListaCommentiNuovi.get(i));
        }
        
        put("cinema:" + idCinema + ":comments", commentiConcatenati);
    }
    
    
    @Override
    public Comment getById(int commentId) {
        
        String idUser, idFilm, text, timestamp, idCinema;
        
        idUser = get("comment:" + commentId + ":user");
        idFilm = get("comment:" + commentId + ":film");
        idCinema = get("comment:" + commentId + ":cinema");
        text = get("comment:" + commentId + ":text");
        timestamp = get("comment:" + commentId + ":timestamp");
        
        if(timestamp == null || text == null || (idFilm == null && idCinema == null) || idUser == null)
        {
            System.err.println("Impossible to retrive comment");
            return null;
        }
        
        if(idFilm != null) return getFilmCommentById(commentId, idUser, idFilm, text, timestamp);
        else return getCinemaCommentById(commentId, idUser, idCinema, text, timestamp);
    }
    
    // da rivedere
    // serve a differenziare il caso del commenti di un film dal caso di commento di un cinema
    private Comment getFilmCommentById(int commentId, String idUser, String idFilm, String text, String timestamp){
    
        User user = UserManager.getIstance().getById(Integer.parseInt(idUser));
        Film film = FilmManagerKV.getIstance().getById(Integer.parseInt(idFilm), false);
        Date date;
        try{
            date = dateFormat.parse(timestamp);
        }catch(ParseException ex){ex.printStackTrace(System.out); return null;}

        // controllo oggetti ottenuti....
        if(user == null || film == null){
            System.err.println("Error: impossible to retireve data");
            return null;
        }
        
        return new Comment(commentId, user, film, text, date);
    }
    
    
    private Comment getCinemaCommentById(int commentId, String idUser, String idCinema, String text, String timestamp){
    
        User user = UserManager.getIstance().getById(Integer.parseInt(idUser));
        Cinema cinema = CinemaManager.getIstance().getById(Integer.parseInt(idCinema));
        Date date;
        try{
            date = dateFormat.parse(timestamp);
        }catch(ParseException ex){ex.printStackTrace(System.out); return null;}

        // controllo oggetti ottenuti....
        if(user == null || cinema == null){
            System.err.println("Error: impossible to retrieve data");
            return null;
        }
        
        return new Comment(commentId, user, cinema, text, date);
    }
    
    /*
    // piccola funzioncina per mostrare la vera utilità della lista dei commenti
    public String getCommentsOfFilm(int filmId){
    
        return get("film:" + filmId + ":comments");
    }
    
     // piccola funzioncina per mostrare la vera utilità della lista dei commenti
    public String getCommentsOfCinema(int cinemaId){
    
        return get("cinema:" + cinemaId + ":comments");
    }
    
    */
        
    public Set<Comment> getCommentsFilm(Film film){
        
        int idFilm = film.getIdFilm();
        
        String commentiConcatenatiFilm = get("film:" + idFilm + ":comments");
    
        if(commentiConcatenatiFilm == null)
            return new LinkedHashSet<>();
        
        return getSet(commentiConcatenatiFilm);
    }
    
    
    public Set<Comment> getCommentsCinema(int idCinema){
        
        String commentiConcatenatiCinema = get("cinema:" + idCinema + ":comments");
    
        if(commentiConcatenatiCinema == null)
            return new LinkedHashSet<>();
        
        return getSet(commentiConcatenatiCinema);
    }
    
    
    public Set<Comment> getCommentsUser(int idUser){
    
        String commentiConcatenatiUser = get("user:" + idUser + ":comments");
    
        if(commentiConcatenatiUser == null)
            return new LinkedHashSet<>();
        
        return getSet(commentiConcatenatiUser);    
    
    }
    
    // data una stringa di idcommenti concatenati
    private Set<Comment> getSet(String commentiConcatenati){
        
        String[] arrayCommentiVecchi = commentiConcatenati.split(":");
        Comment[] arrayCommenti = new Comment[arrayCommentiVecchi.length];
        int i = 0;
        
        for(String idCommento: arrayCommentiVecchi){
            arrayCommenti[i] = getById(Integer.valueOf(idCommento));
        }
         
        return new LinkedHashSet<>(Arrays.asList(arrayCommenti));
    }
    
    
    
}
