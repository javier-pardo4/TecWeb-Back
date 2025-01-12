package edu.uclm.esi.fakeaccountsbe.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;



@TestInstance(Lifecycle.PER_CLASS)

@TestMethodOrder(OrderAnnotation.class)
public class TestDirectorioUCLMTest {
	OrderAnnotation x;
	
	private WebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor jsExecutor;
	private WebDriverWait wait;

	
  @BeforeAll
  public void setUp() {
		System.setProperty("webdriver.chrome.driver", 
				"C:/Users/carlo/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");	 
			
			ChromeOptions options = new ChromeOptions();
			options.setBinary("C:/Users/carlo/Downloads/chrome-win64/chrome-win64/chrome.exe"); 
			options.addArguments("--remote-allow-origins=*");

			driver = new ChromeDriver(options);
			this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));

			jsExecutor = (JavascriptExecutor) driver;
			vars = new HashMap<String, Object>();

  }
  
	@AfterAll
	public void tearDown() {
		driver.quit();
	}

	@Test @Order(1)
	public void testRegistroCorrecto() {
		driver.get("https://alarcosj.esi.uclm.es/examplesfortesting");
		this.pausa(1000);
		
		driver.manage().window().setSize(new Dimension(1241, 992));
		
		WebElement we = driver.findElement(By.xpath("/html/body/div/div/a[1]"));
		we.click();
		
		we = driver.findElement(By.xpath("/html/body/app-root/div/header/nav/ul/li[2]/a"));
		we.click();
		
		WebElement cajaNombre = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/div/main/app-register/form/div[1]/input")));
		
		
		WebElement cajaEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/div/main/app-register/form/div[2]/input")));
		WebElement cajaPwd1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/div/main/app-register/form/div[3]/input")));
		WebElement cajaPwd2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/div/main/app-register/form/div[4]/input")));
		WebElement boton = driver.findElement(By.tagName("button"));
		
		cajaNombre.sendKeys("Manuel");
		cajaEmail.sendKeys("manuel@manuel.com");
		cajaPwd1.sendKeys("Manuel1234");
		cajaPwd2.sendKeys("Manuel1234");
		boton.click();
		
		WebElement etiqueta = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/h2"));
		assertEquals("Login", etiqueta.getText());
	}
	
	@Test @Order(2)
	public void testLoginCorrecto() {
		WebElement cajaNombre = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/form/div[1]/input"));
		WebElement cajaPwd = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/form/div[2]/input"));
		WebElement boton = driver.findElement(By.tagName("button"));
		
		cajaNombre.sendKeys("manuel@manuel.com");
		cajaPwd.sendKeys("Manuel1234");
		boton.click();
		
		String currentUrl = driver.getCurrentUrl();
		assertEquals("https://alarcosj.esi.uclm.es/examplesfortesting/angular/celebration", currentUrl);
		
	}

	
	private void pausa(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
