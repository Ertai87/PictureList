package com.ertai87.picturelist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class PictureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureService.class);

    @Value("${remote.endpoint}")
    private String endpoint;

    public String getPicture(int id){
        LOGGER.info("Querying ID: " + id);

        RestTemplate restTemplate = new RestTemplate();
        try{
            RemoteQueryResponse response = restTemplate.getForObject(endpoint + id, RemoteQueryResponse.class);
            LOGGER.info(response.getImageUrl());
            return response.getImageUrl();
        }catch(HttpClientErrorException e){
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                LOGGER.info("No picture found for ID " + id);
                return null;
            }else throw e;
        }
    }
}
