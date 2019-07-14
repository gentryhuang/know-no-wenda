package com.know.wenda;

import com.know.wenda.base.KnownoWendaApplicationTests;
import com.know.wenda.service.IFeedService;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * FeedServiceTest
 *
 * @author hlb
 */
public class FeedServiceTest extends KnownoWendaApplicationTests {

    @Resource
    private IFeedService feedService;

    @Test
    public void findFeedCount(){
        int count = feedService.findFeedCount(Arrays.asList(18));
        Assert.assertEquals(count,5);
    }

    @Test
    public void findFeedCounts(){
        int count = feedService.findFeedCount(Arrays.asList(0));
        Assert.assertEquals(count,0);
    }

}