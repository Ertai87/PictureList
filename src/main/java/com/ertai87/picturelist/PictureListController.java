package com.ertai87.picturelist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
public class PictureListController {

    @Autowired
    PictureListService pictureListService;

    @RequestMapping("/getImages/{startid}/")
    public @ResponseBody QueryResult getPicturesFromStartId(@PathVariable("startid") int startId){
        return pictureListService.getPictures(startId);
    }

    @RequestMapping("/getImagesForce/{id}/")
    public @ResponseBody QueryResult getPictureFromIdForced(@PathVariable("id") int id){
        return pictureListService.getPictureForced(id);
    }
}
