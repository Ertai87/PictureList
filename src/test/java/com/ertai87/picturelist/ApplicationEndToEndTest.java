package com.ertai87.picturelist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ApplicationEndToEndTest {

    @Autowired
    private PropertySourcesPlaceholderConfigurer pspc;

    @Autowired
    private PictureListController app;

    @Value("${picturelist.length}")
    private int listLength;

    @Test
    public void endToEndTest(){
        QueryResult result = app.getPicturesFromStartId(1);
        assert(result.isFull());
        assert(result.getMaxId() >= listLength);
        assert(result.getUrls().size() <= result.getMaxId());
        QueryResult result2 = app.getPicturesFromStartId(result.getMaxId() + 1);
        assert(result2.isFull());
        assert(result2.getMaxId() >= listLength * 2);
        result.getUrls().forEach(u -> {
            assert(!result2.getUrls().contains(u));
        });
    }
}
