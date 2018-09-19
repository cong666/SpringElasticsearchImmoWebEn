package com.chen.immo.test;

import com.chen.app.start.SpringElasticsearchImmoWebApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by: ccong
 * Date: 18/8/25 下午5:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringElasticsearchImmoWebApplication.class)
//@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")//use the yml config file for test
public class ApplicationTest {

}
