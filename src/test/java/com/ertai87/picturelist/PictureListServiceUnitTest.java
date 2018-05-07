package com.ertai87.picturelist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class PictureListServiceUnitTest {

    @Autowired
    private PropertySourcesPlaceholderConfigurer pspc;

    @Mock
    private PictureService pictureService;

    @InjectMocks
    private PictureListService tested;

    @Value("${picturelist.length}")
    private int listLength;

    @Value("${timeout.length}")
    private int timeoutLength;

    private Random gen = new Random();

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(tested, "listLength", listLength);
        ReflectionTestUtils.setField(tested, "timeoutLength", timeoutLength);
    }

    @Test
    public void testGetPicturesWithExistantPictures(){
        when(pictureService.getPicture(anyInt())).thenReturn("http://example.com");
        int id = gen.nextInt();
        QueryResult result = tested.getPictures(id);
        verify(pictureService, times(listLength)).getPicture(anyInt());
        assertThat(result.isFull(), is(true));
        assertThat(result.getMaxId(), is(id + listLength - 1));
        assertThat(result.getUrls().size(), is(listLength));
    }

    @Test
    public void testGetPicturesWithNoPictures(){
        when(pictureService.getPicture(anyInt())).thenReturn(null);
        int id = gen.nextInt();
        QueryResult result = tested.getPictures(id);
        verify(pictureService, times(timeoutLength)).getPicture(anyInt());
        assertThat(result.isFull(), is(false));
        assertThat(result.getMaxId(), is(id + timeoutLength - 1));
        assertThat(result.getUrls().size(), is(0));
    }

    @Test
    public void testGetPicturesWithLessPictures(){
        when(pictureService.getPicture(anyInt())).thenReturn(
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                null
        );
        int id = gen.nextInt();
        QueryResult result = tested.getPictures(id);
        verify(pictureService, times(timeoutLength + 8)).getPicture(anyInt());
        assertThat(result.isFull(), is(false));
        assertThat(result.getMaxId(), is(id + 7 + timeoutLength));
        assertThat(result.getUrls().size(), is(9));
    }

    @Test
    public void testGetPicturesWithGapInPictures(){
        when(pictureService.getPicture(anyInt())).thenReturn(
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com",
                null,
                "http://example.com",
                "http://example.com",
                "http://example.com",
                "http://example.com"
        );
        int id = gen.nextInt();
        QueryResult result = tested.getPictures(id);
        verify(pictureService, times(listLength + 1)).getPicture(anyInt());
        assertThat(result.isFull(), is(true));
        assertThat(result.getMaxId(), is(id + listLength));
        assertThat(result.getUrls().size(), is(listLength));
    }

    @Test
    public void testGetPictureForced(){
        when(pictureService.getPicture(anyInt())).thenReturn(null, "http://example.com");
        int id = gen.nextInt();
        QueryResult result = tested.getPictureForced(id);
        verify(pictureService, times(2)).getPicture(anyInt());
        assertThat(result.isFull(), is(true));
        assertThat(result.getMaxId(), is(id + 1));
        assertThat(result.getUrls().size(), is(1));
    }

    @Test
    public void testGetPicturesTwice(){
        when(pictureService.getPicture(anyInt())).thenReturn("http://example.com");
        int id = gen.nextInt();
        QueryResult result = tested.getPictures(id);
        assertThat(result.getMaxId(), is(id + 9));
        result = tested.getPictures(result.getMaxId() + 1);
        assertThat(result.getMaxId(), is(id + 19));
    }
}
