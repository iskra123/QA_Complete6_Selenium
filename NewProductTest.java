package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class NewProductTest {

	WebDriver driver;

	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		options.addArguments("--start-maximized", "--disable-extensions");
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://shop.pragmatic.bg/admin/");
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void addNewProduct() {
		WebElement usernameField = driver.findElement(By.name("username"));
		WebElement passwordField = driver.findElement(By.name("password"));
		usernameField.sendKeys("admin");
		passwordField.sendKeys("parola");

		WebElement loginButton = driver.findElement(By.linkText("Login"));
		loginButton.click();
		WebElement logoutButton = driver.findElement(By.linkText("Logout"));
		Assert.assertEquals("Logout", logoutButton.getText());

		WebElement catalogMenu = driver.findElement(By.id("catalog"));
		catalogMenu.click();
		WebElement products = driver.findElement(By.linkText("Products"));
		products.click();
		WebElement productLable = driver.findElement(By.xpath("//div[@class='heading']/h1"));
		Assert.assertEquals("Products", productLable.getText());

		// Check if the wished product is not created

		WebElement productNames = driver.findElement(By.name("filter_name"));
		productNames.sendKeys("Iskra Antonova");
		WebElement filterName = driver.findElement(By.linkText("Filter"));
		filterName.click();

		if (driver.findElements(By.xpath("//form[id='form']//tr[2]//td[3]")).size() != 0) {
			WebElement checkbox = driver.findElement(By.xpath("//form[id='form']//input[@type='checkbox']"));
			if (!checkbox.isSelected())
				checkbox.click();
			driver.findElement(By.linkText("Delete")).click();
			try {
				Alert alert = driver.switchTo().alert();
				alert.accept();
				WebElement message = driver.findElement(By.className("success"));
				assertEquals("You have modified products!", message.getText());
			} catch (NoAlertPresentException e) {
				e.printStackTrace();
			}
			
		} else {
			driver.findElement(By.linkText("Insert")).click();
			WebElement addProduct = driver.findElement(By.xpath("//form[@id='form']//input[@name='product_description[1][name]']"));
			addProduct.sendKeys("Iskra Antonova");
			WebElement data = driver.findElement(By.linkText("Data"));
			data.click();
			WebElement model = driver.findElement(By.xpath("//form[@id='form']//input[@name='model']"));
			model.sendKeys("new");
			WebElement saveButton = driver.findElement(By.linkText("Save"));
			saveButton.click();
		
			Assert.assertEquals(productNames.getText(),"Iskra Antonova" );
		}
	}
}
