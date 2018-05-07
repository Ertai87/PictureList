package com.ertai87.picturelist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class PictureServiceUnitTest {
    @Autowired
    private PropertySourcesPlaceholderConfigurer pspc;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PictureService tested;

    @Value("${remote.endpoint}")
    private String endpoint;

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(tested, "endpoint", endpoint);
    }

    private Random gen = new Random();

    private final String URL = "http://example.com";

    @Test
    public void testUrlWhichExists(){
        when(restTemplate.getForObject(anyString(), eq(RemoteQueryResponse.class))).thenReturn(new RemoteQueryResponse("1", 1, "MOCK", URL));
        String url = tested.getPicture(gen.nextInt());
        assertThat(url, is(URL));
    }

    @Test
    public void testUrlWhichDoesntExist(){
        when(restTemplate.getForObject(anyString(), eq(RemoteQueryResponse.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        String url = tested.getPicture(gen.nextInt());
        assertNull(url);
    }

    @Test
    public void testGarbageUrl(){
        when(restTemplate.getForObject(anyString(), eq(RemoteQueryResponse.class))).thenReturn(new RemoteQueryResponse("1", 1, "MOCK", "MOCK"));
        String url = tested.getPicture(gen.nextInt());
        assertNull(url);
    }
}
