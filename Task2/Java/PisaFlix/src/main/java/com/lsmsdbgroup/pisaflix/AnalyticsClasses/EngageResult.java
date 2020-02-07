package com.lsmsdbgroup.pisaflix.AnalyticsClasses;

public class EngageResult {
    public static final Long COMMENT_WEIGHT = 3L;
    public static final Long FAVOURITE_WEIGHT = 2L;
    public static final Long VIEW_WEIGHT = 1L;
    
    private Integer year;
    private Long viewCount;
    private Long favouriteCount;
    private Long commentCount;
    
    public EngageResult(int year, Long view, Long favourite, Long comment){
        this.year = year;
        viewCount = view;
        favouriteCount = favourite;
        commentCount = comment;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Long favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public Long getEngage(){
        return (commentCount*COMMENT_WEIGHT) + (favouriteCount*COMMENT_WEIGHT) + (viewCount*VIEW_WEIGHT);
    }
}
