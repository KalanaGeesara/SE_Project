//package com.example;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.test.context.junit4.SpringRunner;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.*;
//@RunWith(SpringRunner.class)
//public class RandomPortWebTestClientExampleTests {
//
//    private WebDriver browser;
//
//    @Before
//    public void setup() {
////        browser = new FirefoxDriver();
//        browser = new ChromeDriver();
//    }
//
//    @Test
//    public void startTest() {
//        browser.get("http://localhost:8080/login/");
//
//        browser.findElement(By.id("login")).click();
//
//// Will throw exception if elements not found
//        browser.findElement(By.id("email")).sendKeys("jd");
//        browser.findElement(By.id("password")).sendKeys("secret");
//
//        browser.findElement(By.id("submit")).click();
////        browser.findElement(By.id("account")).click();
//
////        assertEquals("John", browser.findElement(By.id("firstName")).getAttribute("value"));
//    }
//
//    @After
//    public void tearDown() {
//        browser.close();
//    }
//}
