package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class KeyValueDBManager {

    DB KeyValueDB;
    Options options = new Options();
    
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH.mm.ss");

    
    public void start() { //Sono obbligato a metterle non statiche
        try {
            KeyValueDB = factory.open(new File("KeyValueDB"), options);
        } catch (IOException ex) {
            System.out.println("Errore non si è aperto il keyValueDB");
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        settings();
    }

    
    private void settings() {
        String value = get("settingsPresents");
        if (value == null || "false".equals(value)) {
            put("settingsPresents", "true");
            put("setting:lastCommentKey", "0");
            put("setting:lastProjectionKey", "0");
        }
    }

    
    public void stop() {
        try {
            KeyValueDB.close();
        } catch (IOException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void put(String key, String value) {
        KeyValueDB.put(bytes(key), bytes(value));
    }

    
    private void delete(String key) {
        KeyValueDB.delete(bytes(key));
    }

    
    private String get(String key) {
        byte[] value = KeyValueDB.get(bytes(key));
        if (value != null) {
            return Iq80DBFactory.asString(value);
        } else {
            //System.out.println("Key not found");
            return null;
        }
    }

 
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
    
  
    public void updateComment(int commentId, String text) {
        // esiste il testo del commento da modificare?
        String oldText = get("comment:" + commentId + ":text");
        if(text == null || oldText == null) {
            System.out.println("Comment: " + commentId + " not updated");
            return;
        }
        
        put("comment:" + commentId + ":text", text);
    }

 
    public void deleteComment(int commentId) {
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
    
    
    public Comment getCommentById(int commentId) {
        
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
    
    
    // serve a differenziare il caso del commenti di un film dal caso di commento di un cinema
    private Comment getFilmCommentById(int commentId, String idUser, String idFilm, String text, String timestamp){
    
        User user = PisaFlixServices.userService.getUserById(Integer.parseInt(idUser));
        Film film = PisaFlixServices.filmService.getById(Integer.parseInt(idFilm));
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
    
        User user = PisaFlixServices.userService.getUserById(Integer.parseInt(idUser));
        Cinema cinema = PisaFlixServices.cinemaService.getById(Integer.parseInt(idCinema));
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
    
    
    // piccola funzioncina per mostrare la vera utilità della lista dei commenti
    public String getCommentsOfFilm(int filmId){
    
        return get("film:" + filmId + ":comments");
    }
    
     // piccola funzioncina per mostrare la vera utilità della lista dei commenti
    public String getCommentsOfCinema(int cinemaId){
    
        return get("cinema:" + cinemaId + ":comments");
    }
    
    // parte projection 

    public void createProjection(Date dateTime, int room, Cinema cinema, Film film){
        
        if(dateTime == null || cinema == null || film == null){
            System.err.println("Error: the data given to createProjection is invalid");
            return;
        }
        
        String s_room = String.valueOf(room);
        String s_date = dateFormat.format(dateTime);
        String s_cinema = cinema.getIdCinema().toString();
        String s_film = film.getIdFilm().toString();
        
        
        int idProjection = Integer.parseInt(get("setting:lastProjectionKey")) + 1;
        put("projection:" + idProjection + ":dateTime", s_date);
        put("projection:" + idProjection + ":room", s_room);
        put("projection:" + idProjection + ":cinema", s_cinema);
        put("projection:" + idProjection + ":film", s_film);
        
        
        String listaProjection = get("cinema:" + s_cinema + ":projections");
        
        if(listaProjection == null){// me la creo
            put("cinema:" + s_cinema + ":projections", String.valueOf(idProjection));
            put("setting:lastProjectionKey", String.valueOf(idProjection));
            return;
        }
        
        listaProjection = listaProjection.concat(":" + String.valueOf(idProjection));
        
        
        put("cinema:" + s_cinema + ":projections", listaProjection);
        put("setting:lastProjectionKey", String.valueOf(idProjection));
    }
    
    
    public void updateProjection(int projectionId, Date dateTime, int room){
        
        String oldRoom = get("projection:" + projectionId + ":room");
        String oldDate = get("projection:" + projectionId + ":dateTime");
        
        if(oldRoom == null || oldDate == null || dateTime == null){
            System.err.println("Error: the data given to updateProjection is invalid");
            return;
        }
        
        put("projection:" + projectionId + ":room", String.valueOf(room));
        put("projection:" + projectionId + ":dateTime", dateFormat.format(dateTime));
        
    }
    
    
    public void deleteProjection(int projectionId){
        
        String cinemaId = get("projection:" + projectionId + ":cinema");
        
        if(cinemaId == null){
            System.err.println("Error: impossible to delete projection: " + projectionId);
            return;
        }
        
         try{
            delete("projection:" + projectionId + ":dateTime");
            delete("projection:" + projectionId + ":room");
            delete("projection:" + projectionId + ":cinema");
            delete("projection:" + projectionId + ":film");
        }catch (DBException ex){
            System.err.println("Error: impossible to delete one or more tuple of "
                    + "projection: " + projectionId);
            return;
        }
        // prendo la lista di projection associata a quel cinema
        
        String oldProjectionString = get("cinema:" + cinemaId + ":projections");
        
        if(oldProjectionString == null){
        
            System.err.println("Error: impossible to retreive the list of projection "
                    + "of cinema: " + cinemaId);
            return;
        }
        // faccio la split
        
        String[] projectionArray = oldProjectionString.split(":");
        
        // controllo se ha lunghezza 1 -> butto via tutto 
        if(projectionArray.length == 1){
            delete("cinema:" + cinemaId + ":projections");
            return;
        }
        // altrimenti converto in una lista di stringhe, rimuovo l'id in questione, ricorstuisco la stringa da salvare
        
        List<String> listaProjections = new ArrayList<>(Arrays.asList(projectionArray));
        listaProjections.remove(String.valueOf(projectionId));
        
        String newProjectionString = "";
        for(int i=0; i<listaProjections.size();i++){
        
            if(i==0) newProjectionString = listaProjections.get(i);
            else newProjectionString = newProjectionString.concat(listaProjections.get(i));
        }        
                
        // aggiorno la lista.
        put("cinema:" + cinemaId + ":projections", newProjectionString);
    }
    

    public Projection getProjectionById(int projectionId){
        
        String s_room = get("projection:" + projectionId + ":room");
        String s_date = get("projection:" + projectionId + ":dateTime");
        String s_cinema = get("projection:" + projectionId + ":cinema");
        String s_film = get("projection:" + projectionId + ":film");
        
        if(s_room == null || s_date == null || s_cinema == null || s_film == null){
            System.err.println("Error: impossible to retreive projection");
            return null;
        }
        
        // recupero oggetto film, oggetto cinema
        Film film = PisaFlixServices.filmService.getById(Integer.parseInt(s_film));
        Cinema cinema = PisaFlixServices.cinemaService.getById(Integer.parseInt(s_cinema));
        Date date;
        try{
            date = dateFormat.parse(s_date);
        }catch(ParseException ex){ex.printStackTrace(System.out); return null;}
        
        // li controllo
        if(film == null || cinema == null){
            System.err.println("Error: impossible to retreive data");
            return null;
        }
        
        return new Projection(projectionId, date, Integer.parseInt(s_room), cinema, film);
    }
    
    // piccola funzioncina per mostrare la vera utilità della lista delle projection
    public String getProjectionsOfCinema(int cinemaId){
    
        return get("cinema:" + cinemaId + ":projections");
    }
}
