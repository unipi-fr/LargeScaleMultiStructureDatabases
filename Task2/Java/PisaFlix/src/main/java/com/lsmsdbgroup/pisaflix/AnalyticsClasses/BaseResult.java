package com.lsmsdbgroup.pisaflix.AnalyticsClasses;

public class BaseResult {
    private String tag;
    private Double value;

    public BaseResult(String tag, Double value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public Double getValue() {
        return value;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    
    
}
