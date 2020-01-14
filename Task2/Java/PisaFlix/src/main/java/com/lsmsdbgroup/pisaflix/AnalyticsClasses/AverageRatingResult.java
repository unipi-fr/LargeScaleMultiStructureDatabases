package com.lsmsdbgroup.pisaflix.AnalyticsClasses;

public class AverageRatingResult {
    private String tag;
    private Double averageRating;
    private Long count;

    public AverageRatingResult(String tag, Double averageRating, Long count) {
        this.tag = tag;
        this.averageRating = averageRating;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    } 
}
