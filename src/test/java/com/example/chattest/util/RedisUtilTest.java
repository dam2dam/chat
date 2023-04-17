package com.example.chattest.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.chattest.ChatTestApplication;

@SpringBootTest
@ContextConfiguration(classes = ChatTestApplication.class)
class RedisUtilTest {

	@Autowired
	RedisUtil redisUtil;

	@Test
	void data() {

		System.out.println("set test");
		redisUtil.setData("hi", "hello");

		System.out.println("get test");
		Object hi = redisUtil.getData("hi");
		System.out.println("hi = " + hi);
		assertEquals(hi, "hello");

		System.out.println("del test");
		redisUtil.deleteData("hi");
		redisUtil.getData("hi");

	}

}
