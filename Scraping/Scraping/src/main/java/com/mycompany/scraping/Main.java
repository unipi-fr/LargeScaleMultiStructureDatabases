
package com.mycompany.scraping;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;


public class Main {

    
    public static void main(String[] args) {
        
        HttpResponse response = Unirest.post(API_URL)
        .header("X-RapidAPI-Key", API_KEY)
        .queryString("parameter", "value")
        .field("parameter", "value")
        .asJson();
        
    }
    
}
