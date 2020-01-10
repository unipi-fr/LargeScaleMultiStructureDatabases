/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.AnalyticsClasses;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author FraRonk
 */
public class ResultDetailed extends BaseResult {
    
    public ResultDetailed(String tag, Double value) {
        super(tag, value);
    }
    
    private Set<BaseResult> resultDetail = new LinkedHashSet<>();

    public Set<BaseResult> getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(Set<BaseResult> resultDetail) {
        this.resultDetail = resultDetail;
    }
    
    public void addBaseResultToDetail(BaseResult br){
        this.resultDetail.add(br);
    }
    
}
