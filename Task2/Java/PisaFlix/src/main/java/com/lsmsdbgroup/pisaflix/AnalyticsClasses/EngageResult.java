package com.lsmsdbgroup.pisaflix.AnalyticsClasses;

public class EngageResult {
    private String idFilm;
    private Long viewCount;
    private Long favouriteCount;
    private Long commentCount;
    
    public EngageResult(String idFilm, Long view, Long favourite, Long comment){
        this.idFilm = idFilm;
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
}
