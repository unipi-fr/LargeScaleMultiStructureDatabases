/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.*;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface.*;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author FraRonk
 */
public interface AnalyticsServiceInterface {
    //tyopeOfRating perchè nelle specifiche abbiamo scritto che si può calcolare il rating medio raggruppando per [Genere - Regista - Attore]
    Set<AverageRatingResult> ratingAnalytics(Date startdate, Date endDate, RatingType typeOfRating);
    
    //tyopeOfRanking perchè nelle specifiche abbiamo scritto che si può calcolare il rating medio raggruppando per [Film - utenti]
    Set<RankingResult> rankingAnalytics(Date startDate, Date endDate, RankingType typeOfRanking);
    
    //Entity può essere un film o un utente
    Set<EngageResult> engagementAnalytics(Date startdate, Date endDate, Entity entity);
}
