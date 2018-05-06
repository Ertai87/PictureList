package com.ertai87.picturelist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class PictureListService {

    @Autowired
    private PictureService pictureService;

    @Value("${picturelist.length}")
    private int listLength;

    @Value("${timeout.length}")
    private int timeoutLength;

    public QueryResult getPictures(int startId){
        List<String> urls = new LinkedList<>();
        int lastFound = startId - 1;
        for (int i = startId; urls.size() < listLength && i < lastFound + timeoutLength; i++){
            String url = pictureService.getPicture(i);
            if (url != null){
                urls.add(url);
                lastFound = i;
            }
        }
        if (urls.size() == listLength) {
            return new QueryResult(true, lastFound, urls);
        }else{
            return new QueryResult(false, lastFound + timeoutLength - 1, urls);
        }
    }

    public QueryResult getPictureForced(int id){
        String url = null;
        while (url == null){
            url = pictureService.getPicture(id);
            id++;
        }
        return new QueryResult(true, id, Arrays.asList(new String[]{url}));
    }
}
