package com.lsmsdbgroup.pisaflix;

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

    
    public void settings() {
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

    
    public void put(String key, String value) {
        KeyValueDB.put(bytes(key), bytes(value));
    }

    
    public void delete(String key) {
        KeyValueDB.delete(bytes(key));
    }

    
    public String get(String key) {
        byte[] value = KeyValueDB.get(bytes(key));
        if (value != null) {
            return Iq80DBFactory.asString(value);
        } else {
            //System.out.println("Key not found");
            return null;
        }
    }

    // OK
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
        delete("film:" + film.getIdFilm().toString() + ":comments");
        put("film:" + film.getIdFilm().toString() + ":comments", listaIdCommenti);
        
        // aggiorno il contatore dell'id commento
        put("setting:lastCommentKey", String.valueOf(idComment));
    }

    // OK
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
        delete("cinema:" + cinema.getIdCinema().toString() + ":comments");
        put("cinema:" + cinema.getIdCinema().toString() + ":comments", listaIdCommenti);
        
        // aggiorno il contatore dell'id commento
        put("setting:lastCommentKey", String.valueOf(idComment));
      
    }
    
    // OK
    public void updateComment(int commentId, String text) {
        // esiste il testo del commento da modificare?
        String oldText = get("comment:" + commentId + ":text");
        if(text == null || oldText == null) {
            System.out.println("Comment: " + commentId + " not updated");
            return;
        }
        
        delete("comment:" + commentId + ":text");
        put("comment:" + commentId + ":text", text);
    }

    // OK
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

    // OK
    private void deleteFilmComment(int commentId, String idFilm){
        //il fallimento di anche solo una di queste indica inconsistenza del DB!!!
        try{
            delete("comment:" + commentId + ":user");
            delete("comment:" + commentId + ":film");
            delete("comment:" + commentId + ":text");
            delete("comment:" + commentId + ":timestamp");
        }catch (DBException ex){
            System.err.println("Una parte del commento: "
            + commentId + " da cancellare non era presente");
            return;
        }
        // prendo la lista dei commenti associati al film
        String commentiConcatenati = get("film:" + idFilm + ":comments");
     
        if(commentiConcatenati == null){
            System.err.println("Errore: lista commenti collegati al film non esistente");
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
        
        delete("film:" + idFilm + ":comments");
        put("film:" + idFilm + ":comments", commentiConcatenati);
        
    }
    
    // OK
    private void deleteCinemaComment(int commentId, String idCinema){
        //il fallimento di anche solo una di queste indica inconsistenza del DB!!!
        try{
            delete("comment:" + commentId + ":user");
            delete("comment:" + commentId + ":cinema");
            delete("comment:" + commentId + ":text");
            delete("comment:" + commentId + ":timestamp");
        }catch (DBException ex){
            System.err.println("Una parte del commento: "
            + commentId + " da cancellare non era presente");
            return;
        }
        // prendo la lista dei commenti associati al cinema
        String commentiConcatenati = get("cinema:" + idCinema + ":comments");
     
        if(commentiConcatenati == null){
            System.err.println("Errore: lista commenti collegati al cinema non esistente");
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
        
        delete("cinema:" + idCinema + ":comments");
        put("cinema:" + idCinema + ":comments", commentiConcatenati);
    }
    
    // OK
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
    
    // OK
    // serve a differenziare il caso del commenti di un film dal caso di commento di un cinema
    private Comment getFilmCommentById(int commentId, String idUser, String idFilm, String text, String timestamp){
    
        User user = PisaFlixServices.UserManager.getUserById(Integer.parseInt(idUser));
        Film film = PisaFlixServices.FilmManager.getById(Integer.parseInt(idFilm));
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
    
    // OK
    private Comment getCinemaCommentById(int commentId, String idUser, String idCinema, String text, String timestamp){
    
        User user = PisaFlixServices.UserManager.getUserById(Integer.parseInt(idUser));
        Cinema cinema = PisaFlixServices.CinemaManager.getById(Integer.parseInt(idCinema));
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
    
    // da fare
    public void createProjection(Date dateTime, int room, Cinema cinema, Film film){
    // VECCHIA IMPLEMENTAZIONE
        /*
        int idProjection = Integer.parseInt(get("setting:lastProjectionKey")) + 1;
        put("projection:" + String.valueOf(idProjection), "dateTime:" + dateFormat.format(dateTime) + ":room:" + String.valueOf(room) + ":cinema:" + cinema.getIdCinema().toString() + ":film:" + film.getIdFilm().toString());
        put("setting:lastProjectionKey", String.valueOf(idProjection));
    */
    }
    
    // da fare
    public void updateProjection(int projectionId, Date dateTime, int room){
    
        // VECCHIA IMPLEMENTAZIONE
        /*
        Projection projection = getProjectionById(projectionId);
        String content = get(String.valueOf("projection:" + projectionId));
        
        if(projection == null || dateTime == null || content == null) {
            System.out.println("Projection: " + projectionId + " not updated");
            return;
        }
        
        deleteProjection(projectionId);
        String[] field = content.split(":");
        field[3] = String.valueOf(room);
        field[1] = dateFormat.format(dateTime);
        
        content = "dateTime:" + field[1] + ":room:" + field[3] + ":cinema:" + field[5] + ":film:" + field[7];

        put("projection:" + String.valueOf(projectionId), content);
        */
    }
    
    // da fare
    public void deleteProjection(int projectionId){
       // VECCHIA IMPLEMENTAZIONE
        /*
        Projection projection = getProjectionById(projectionId);
        if(projection == null) {
            System.out.println("Projection: " + projectionId + " not deleted");
            return;
        }
        delete(String.valueOf("projection:" + projectionId));
        System.out.println("Projection: " + projectionId + " deleted");
        */
    }
    
    // da fare
    public Projection getProjectionById(int projectionId){ 
        // VECCHIA IMPLEMENTAZIONE   
        /*
        String value = get(String.valueOf("projection:" + projectionId));
        if(value == null) return null;

        String[] field = value.split(":");
        try {
            return new Projection(projectionId, dateFormat.parse(field[1]), Integer.parseInt(field[3]));
        } catch (ParseException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    */
        return null; // mi urtava l'errore della mancanza di un ritorno
        
    }
    
    
}
