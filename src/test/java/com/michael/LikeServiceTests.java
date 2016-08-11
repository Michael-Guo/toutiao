package com.michael;

import com.michael.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by GWC on 2016/7/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {

    @Autowired
    LikeService likeService;

    //测试用例所在
    @Test
    public void testLike() {
        likeService.like(123, 1, 1);
        Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test
    public void testDislike() {
        likeService.disLike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    //异常测试，正常情况下应该抛出异常
    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        throw new IllegalArgumentException("Exception Test");
    }

    //在测试用例之前初始化数据
    @Before
    public void setUp() {
        System.out.println("setUp");
    }

    //在测试用例之后清理数据
    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    //在所有测试用例之前运行，只运行一次
    @BeforeClass
    public static void beforeClass() {
        System.out.println("before");
    }

    //在所有测试用例之后运行，只运行一次
    @AfterClass
    public static void afterClass() {
        System.out.println("after");
    }
}
