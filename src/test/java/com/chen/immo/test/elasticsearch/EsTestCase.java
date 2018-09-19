package com.chen.immo.test.elasticsearch;

import com.chen.app.search.SearchService;
import com.chen.immo.test.ApplicationTest;
import com.chen.web.form.RentFilter;
import com.chen.web.util.ServiceMultipleResult;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by: ccong
 * Date: 18/9/7 下午1:54
 */
public class EsTestCase extends ApplicationTest {

    private static Logger logger = LoggerFactory.getLogger(EsTestCase.class);
    @Autowired
    private SearchService searchService;

    @Test
    @Transactional
    public void testIndex() {
        boolean result = searchService.index(15L);
        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void testQuery() {
        RentFilter rentFilter = new RentFilter();
        rentFilter.setCityEnName("paris");
        rentFilter.setStart(0);
        rentFilter.setSize(3);
        ServiceMultipleResult<Long> result = searchService.query(rentFilter);
        logger.info("Result:"+result.getTotal());
        for(Long id : result.getResult()) {
            logger.info("---------id : "+id);
        }
    }
}
