package com.bluebirdme.mes.core.junit;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml", "classpath:spring-mvc.xml"})
public abstract class BaseTest {
    public BaseTest() {
    }
}
