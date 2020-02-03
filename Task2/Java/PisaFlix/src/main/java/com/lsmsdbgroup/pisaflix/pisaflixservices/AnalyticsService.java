package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.*;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.AnalyticsServiceInterface;
import java.util.Date;
import java.util.Set;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;

public class AnalyticsService implements AnalyticsServiceInterface {
    private AnalyticsManagerDatabaseInterface analyticsManager;

    public AnalyticsService(AnalyticsManagerDatabaseInterface analyticsManager) {
        this.analyticsManager = analyticsManager;
    }
    

    @Override
    public Set<EngageResult> engagementAnalytics(Date startdate, Date endDate, Entity entity) {
        return this.analyticsManager.engagementAnalytics(startdate, endDate, entity);
    }

    @Override
    public Set<AverageRatingResult> ratingAnalytics(Date startdate, Date endDate, RatingType typeOfRating) {
        return this.analyticsManager.ratingAnalytics(startdate, endDate, typeOfRating);
    }

    @Override
    public Set<RankingResult> rankingAnalytics(Date startDate, Date endDate, RankingType typeOfRanking) {
        return analyticsManager.rankingAnalytics(startDate, endDate, typeOfRanking);
    }
    
}
