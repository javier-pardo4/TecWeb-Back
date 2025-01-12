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
import org.openqa.selenium.Point;

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

public class TestingTecWeb {

	OrderAnnotation x;

	private WebDriver driverPepe;
	private WebDriver driverAna;
	private Map<String, Object> vars;
	JavascriptExecutor jsExecutor;
	private WebDriverWait wait;

    // Variables para los correos
    private final String EMAIL_PEPE = "biblio91@gmail.com";
    private final String EMAIL_ANA = "anatecwebB11@gmail.com";
	
	@BeforeAll
	public void setUp() {
		System.setProperty("webdriver.chrome.driver",
				"/home/javiipardo/Documentos/UCLM/4ºCurso/1º Cuatri/TecSerWeb/ListaCompra/webdriver/testingTECWEB/chromedriver-linux64/chromedriver");

		ChromeOptions options = new ChromeOptions();
		options.setBinary("/usr/bin/google-chrome");

		options.addArguments("--remote-allow-origins=*");

		driverPepe = new ChromeDriver(options);
		driverAna = new ChromeDriver(options);
		this.wait = new WebDriverWait(driverPepe, Duration.ofSeconds(3));
		this.wait = new WebDriverWait(driverAna, Duration.ofSeconds(8));
		jsExecutor = (JavascriptExecutor) driverPepe;
		jsExecutor = (JavascriptExecutor) driverAna;
		
		vars = new HashMap<String, Object>();

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int halfWidth = screenWidth / 2;

		driverPepe.manage().window().setSize(new Dimension(halfWidth, 900));
		driverPepe.manage().window().setPosition(new Point(0, -250));
		driverAna.manage().window().setSize(new Dimension(halfWidth, 900));
		driverAna.manage().window().setPosition(new Point(halfWidth, 0));

	}

	@AfterEach
	public void tearDown() {
		driverPepe.quit();
		driverAna.quit();
	}

	@Test
	public void registroConfirmacionInicio() {
		
		
		driverPepe.get("https://localhost:4200/");
		driverPepe.findElement(By.xpath("/html/body/div/div[2]/button[3]")).click();
		driverPepe.findElement(By.xpath("/html/body/div/div[3]/p[2]/a")).click();
		driverPepe.findElement(By.tagName("button")).click();
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/p[1]/span")).click();
		driverPepe.findElement(By.name("email")).click();
		driverPepe.findElement(By.name("email")).sendKeys(EMAIL_PEPE); //Cambiar Correo
		driverPepe.findElement(By.name("password")).click();
		driverPepe.findElement(By.name("password")).sendKeys("Pepe1234");
		driverPepe.findElement(By.name("confirmPassword")).click();
		driverPepe.findElement(By.name("confirmPassword")).sendKeys("Pepe1234");
		driverPepe.findElement(By.cssSelector(".btn-register")).click();
		this.pausa(15000);
		// Realizar scroll hacia abajo
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driverPepe;
		jsExecutor.executeScript("window.scrollBy(0, 20);"); 
		this.pausa(5000);
		driverPepe.findElement(By.className("success-message")).click();
		this.pausa(5000);
        Set<String> handles = driverPepe.getWindowHandles();
        ArrayList<String> tabs = new ArrayList<>(handles);
        driverPepe.switchTo().window(tabs.get(1));
        driverPepe.findElement(By.xpath("/html/body/app-root/div/app-confirm-account/div[2]/div/button")).click();
        this.pausa(3000);
        driverPepe.close();
        driverPepe.switchTo().window(tabs.get(0));
		this.pausa(3000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[1]/input")).click();
		this.pausa(3000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[1]/input")).sendKeys(EMAIL_PEPE);
		this.pausa(3000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[2]/input")).click();
		this.pausa(3000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[2]/input")).sendKeys("Pepe1234");
		this.pausa(3000);
		driverPepe.findElement(By.cssSelector(".btn")).click();
		this.pausa(3000);
		
		// SE REGISTRA ANA
		driverAna.get("https://localhost:4200/");
		driverAna.findElement(By.xpath("/html/body/div/div[2]/button[3]")).click();
		driverAna.findElement(By.xpath("/html/body/div/div[3]/p[2]/a")).click();
		driverAna.findElement(By.tagName("button")).click();
		driverAna.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/p[1]/span")).click();
		driverAna.findElement(By.name("email")).click();
		this.pausa(3000);
		driverAna.findElement(By.name("email")).sendKeys(EMAIL_ANA);
		this.pausa(3000);
		driverAna.findElement(By.name("password")).click();
		driverAna.findElement(By.name("password")).sendKeys("Ana12345");
		this.pausa(3000);
		driverAna.findElement(By.name("confirmPassword")).click();
		driverAna.findElement(By.name("confirmPassword")).sendKeys("Ana12345");
		this.pausa(3000);
		driverAna.findElement(By.cssSelector(".btn-register")).click();
		this.pausa(5000);
		JavascriptExecutor jsExecutor3 = (JavascriptExecutor) driverAna;
		jsExecutor3.executeScript("window.scrollBy(0, 20);");
		this.pausa(5000);
		driverAna.findElement(By.xpath("/html/body/app-root/div/app-register1/div[2]/div/div/a")).click();
		this.pausa(5000);
        Set<String> handles2 = driverAna.getWindowHandles();
        ArrayList<String> tabs2 = new ArrayList<>(handles2);
        driverAna.switchTo().window(tabs2.get(1));
        driverAna.findElement(By.xpath("/html/body/app-root/div/app-confirm-account/div[2]/div/button")).click();
        this.pausa(3000);
        driverAna.close();
        driverAna.switchTo().window(tabs2.get(0));
		this.pausa(5000);
		
		//GESTOR LISTA PEPE
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-gestor-listas/div[2]/section[2]/div/input")).click();
		this.pausa(3000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-gestor-listas/div[2]/section[2]/div/input"))
				.sendKeys("Cumpleaños");
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-gestor-listas/div[2]/section[2]/div/button")).click();
		this.pausa(5000);
		// Realizar scroll hacia abajo
		JavascriptExecutor jsExecutor1 = (JavascriptExecutor) driverPepe;
		jsExecutor1.executeScript("window.scrollBy(0, 5000);"); 
		this.pausa(5000);
		driverPepe.findElement(By.cssSelector(".btn-action:nth-child(1)")).click();
		this.pausa(5000);
//		{
//			WebElement element = driverPepe.findElement(By.cssSelector(".btn-action:nth-child(1)"));
//			Actions builder = new Actions(driverPepe);
//			builder.moveToElement(element).perform();
//		}
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[1]/input")).click();
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[1]/input"))
				.sendKeys("Latas de Cerveza");
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[2]/input")).click();
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[2]/input"))
				.sendKeys("30");
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[4]/button")).click();
		this.pausa(5000);
		jsExecutor1.executeScript("window.scrollBy(0, 1000);"); 
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[1]/input")).click();
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[1]/input"))
				.sendKeys("Tarta");
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[2]/input")).click();
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[2]/input"))
				.sendKeys("1");
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[4]/button")).click();
		this.pausa(5000);
		jsExecutor1.executeScript("window.scrollBy(0, 1000);"); 
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[1]/input")).click();
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[1]/input"))
				.sendKeys("Bolsas de Patatas Fritas");
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[2]/input")).click();
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[2]/input"))
				.sendKeys("2");
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[5]/form/div[4]/button")).click();
		jsExecutor1.executeScript("window.scrollBy(0, -1000);"); 
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[3]/button[2]")).click();
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[6]/div/div/div[2]/form/div/input")).click();
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[6]/div/div/div[2]/form/div/input")).sendKeys(EMAIL_ANA);
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[6]/div/div/div[2]/form/button")).click();
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[6]/div/div/div[2]/form/div[2]/a")).click();
		this.pausa(5000);
        Set<String> handles10 = driverPepe.getWindowHandles();
        ArrayList<String> tabs10 = new ArrayList<>(handles10);
		this.pausa(5000);
		driverPepe.switchTo().window(tabs10.get(1));
		this.pausa(5000);
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-invitaciones/div[3]/button")).click();
		this.pausa(5000);
		driverPepe.close();
		driverPepe.switchTo().window(tabs10.get(0));
		this.pausa(5000);
		
		driverPepe.findElement(By.xpath("/html/body/app-root/div/app-detalle-lista/div[6]/div/div/div[1]/button")).click();
		this.pausa(2000);
		
		
		driverAna.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[1]/input")).click();
		this.pausa(2000);
		driverAna.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[1]/input")).sendKeys(EMAIL_ANA);
		this.pausa(2000);
		driverAna.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[2]/input")).click();
		this.pausa(2000);
		driverAna.findElement(By.xpath("/html/body/app-root/div/app-login1/div[2]/div/form/div[2]/input")).sendKeys("Ana12345");
		this.pausa(2000);
		driverAna.findElement(By.cssSelector(".btn")).click();
		this.pausa(5000);
		jsExecutor3.executeScript("window.scrollBy(0, 500);");
		this.pausa(5000);
		driverAna.findElement(By.cssSelector(".btn-action:nth-child(1)")).click();
//		{
//			WebElement element = driverAna.findElement(By.cssSelector(".btn-action:nth-child(1)"));
//			Actions builder = new Actions(driverAna);
//			builder.moveToElement(element).perform();
//		}
		this.pausa(5000);
		driverAna.findElement(By.xpath("//tr[td[contains(text(), 'Tarta')]]//button[contains(@class, 'btn-success')]")).click();
		this.pausa(40000);
		

	}


	private void pausa(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
