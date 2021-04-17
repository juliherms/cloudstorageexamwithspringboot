package com.github.juliherms.cloudstorage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This class responsible to set initial setup to test case
 * 
 * @author jlv
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudstorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	/**
	 * Setup selenium with chrome driver
	 */
	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	/**
	 * Initialize chrome driver
	 */
	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@Test
	void contextLoads() {
	}

}
