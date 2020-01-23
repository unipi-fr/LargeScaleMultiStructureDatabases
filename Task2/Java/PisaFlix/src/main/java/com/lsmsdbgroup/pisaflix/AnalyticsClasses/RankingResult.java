package com.lsmsdbgroup.pisaflix.AnalyticsClasses;

import java.util.Set;

public class RankingResult {
    public static final Long COMMENT_WEIGHT = 3L;
    public static final Long FAVOURITE_WEIGHT = 2L;
    public static final Long VIEW_WEIGHT = 1L;
    
    private String id;
    private String title_username;
    private Long commentCount;
    private Long favouriteCount;
    private Long viewCount;
    

    public RankingResult() {
        this.id = "";
        this.title_username = "";
        this.commentCount = 0L;
        this.favouriteCount = 0L;
        this.viewCount = 0L;
    } 
    
    public RankingResult(String id, String title_username, Long commentCount, Long favouriteCount, Long viewCount) {
        this.id = id;
        this.title_username = title_username;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.favouriteCount = favouriteCount;
    }

    public String getId() {
        return id;
    }
    
    public String getTitle_username() {
        return title_username;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public Long getFavouriteCount() {
        return favouriteCount;
    }
    
    public Long getScore(){
        return (commentCount*COMMENT_WEIGHT) + (favouriteCount*COMMENT_WEIGHT) + (viewCount*VIEW_WEIGHT);
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle_username(String title_username) {
        this.title_username = title_username;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public void setFavouriteCount(Long favouriteCount) {
        this.favouriteCount = favouriteCount;
    }    
    
    public int compareTo(RankingResult other){
        Long thisScore = this.getScore();
        Long otherScore = other.getScore();
        if(thisScore > otherScore){
            return 1;
        }
        if(thisScore == otherScore){
            return 0;
        }
        // this.getScore() < other.getScore()
        return -1;
}
    
    public boolean merge(RankingResult other){
        if(this.id == null ? other.id != null : !this.id.equals(other.id)){
            return false;
        }
        if(this.title_username == ""){
            this.title_username += other.getTitle_username();
        }
        this.commentCount += other.commentCount;
        this.favouriteCount += other.favouriteCount;
        this.viewCount += other.viewCount;
        return true;
    }
    
    public static void addOrMerge(Set<RankingResult> destination, Set<RankingResult> surce){
        for(RankingResult rs : surce){  
            addOrMerge(destination,rs);
        }
    }
    
    public static void addOrMerge(Set<RankingResult> setDestination, RankingResult itemToAdd){
        for(RankingResult rd : setDestination){
            if(rd.merge(itemToAdd)){
                return;
            }
        }
        setDestination.add(itemToAdd);     
    }
    
    @Override
    public String toString(){
       return "ID: "+this.id+" | Title/Username: "+this.title_username+" | Comment: "+this.commentCount+" | Favourite: "+this.favouriteCount+" | View: "+this.viewCount; 
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RankingResult)) {
            return false;
        }
        RankingResult other = (RankingResult) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
}
