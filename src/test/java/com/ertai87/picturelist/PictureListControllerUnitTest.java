package com.ertai87.picturelist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class PictureListControllerUnitTest {
    @Mock
    private PictureListService pictureListService;

    @InjectMocks
    private PictureListController tested = new PictureListController();

    private Random gen = new Random();

    @Test
    public void testGetPicturesFromStartId(){
        int val = gen.nextInt();
        tested.getPicturesFromStartId(val);
        verify(pictureListService).getPictures(val);
    }

    @Test
    public void testGetPicturesFromIdForced(){
        int val = gen.nextInt();
        tested.getPictureFromIdForced(val);
        verify(pictureListService).getPictureForced(val);
    }
}
