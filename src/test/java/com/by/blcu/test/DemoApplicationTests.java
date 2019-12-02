package com.by.blcu.test;

import com.by.blcu.core.utils.ApplicationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
		String waitDecode = "orderDate=20140319152513&orderNo=1403190004&amount=10.00&xmpch=036- 2014010005&return_url=http://www.test.com/returnPage.htm&notify_url=http://www.test.com/ notifyPage.htm";
		                   //orderDate=20140319152513&orderNo=1403190004&amount=10.00&xmpch=036- 2014010005&return_url=http://www.test.com/returnPage.htm&notify_url=http://www.test.com/ notifyPage.htmumz4aea6g97skeect0jtxigvjkrimd0o
		waitDecode = waitDecode + "umz4aea6g97skeect0jtxigvjkrimd0o";
								//umz4aea6g97skeect0jtxigvjkrimd0o

//		waitDecode = "orderDate=20140319152513&orderNo=1403190004&amount=10.00&xmpch=036- 2014010005&return_url=http://www.test.com/returnPage.htm&notify_url=http://www.test.com/ notifyPage.htmumz4aea6g97skeect0jtxigvjkrimd0o";

		System.out.println("MD5签名："+ApplicationUtils.md5Hex(waitDecode));


	}


}
