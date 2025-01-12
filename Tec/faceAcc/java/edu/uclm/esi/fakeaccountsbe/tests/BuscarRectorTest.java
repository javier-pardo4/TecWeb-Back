package edu.uclm.esi.fakeaccountsbe.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

public class BuscarRectorTest {
	private WebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor jsExecutor;

  
  @BeforeEach
  public void setUp() {
		System.setProperty("webdriver.chrome.driver", 
				"C:/Users/felip/Desktop/testingTECWEB/chromedriver-win64/chromedriver.exe");	 
			
			ChromeOptions options = new ChromeOptions();
			options.setBinary("C:/Users/felip/Desktop/testingTECWEB/chrome-win64/chrome.exe"); 
			options.addArguments("--remote-allow-origins=*");

			driver = new ChromeDriver(options);
			new WebDriverWait(driver, Duration.ofSeconds(3));

			jsExecutor = (JavascriptExecutor) driver;
			vars = new HashMap<String, Object>();

  }
  
	@AfterEach
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testBuscarAlRector() {
		driver.get("https://directorio.uclm.es/");
		driver.manage().window().setSize(new Dimension(1241, 992));
		driver.findElement(By.id("CPH_CajaCentro_tb_busqueda")).click();
		driver.findElement(By.id("CPH_CajaCentro_tb_busqueda")).sendKeys("Julián Garde");
		driver.findElement(By.cssSelector(".hidden-xs")).click();
		driver.findElement(By.id("CPH_CajaCentro_gv_personas_lkbtn_descripcionPersona_0")).click();
		assertEquals(driver.findElement(By.id("CPH_CajaCentro_rpt_cargos_lb_cargo_0")).getText(), 
			"RECTOR/A");
	}
}

