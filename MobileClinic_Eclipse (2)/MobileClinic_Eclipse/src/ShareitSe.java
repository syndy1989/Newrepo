import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ShareitSe {
	static AndroidDriver<WebElement> driver;
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "4d00704e4e375185");
		capabilities.setCapability("platformVersion", "6.0");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("BROWSER_NAME", "Android");
		//capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("appPackage", "com.lenovo.anyshare.gps");
		capabilities.setCapability("appActivity", "com.lenovo.anyshare.activity.MainActivity");

		driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
		Thread.sleep(10000);
		System.out.println("Launch App");
		driver.findElement(By.id("com.lenovo.anyshare.gps:id/au")).click();

		WebDriverWait wait = new WebDriverWait(driver,20);
		      WebElement aboutMe;
		      aboutMe= wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.lenovo.anyshare.gps:id/js"))); 


		driver.findElement(By.id("com.lenovo.anyshare.gps:id/js")).click();

		WebDriverWait wait2 = new WebDriverWait(driver,20);
		      WebElement aboutMe2;
		      aboutMe2= wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.lenovo.anyshare.gps:id/sk"))); 

		driver.findElement(By.name("Files")).click();


		WebDriverWait wait3 = new WebDriverWait(driver,20);
		      WebElement aboutMe3;
		      aboutMe= wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.lenovo.anyshare.gps:id/sk"))); 


		driver.findElement(By.name("Videos")).click();

		WebDriverWait wait4 = new WebDriverWait(driver,20);
		      WebElement aboutMe4;
		      aboutMe4= wait4.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.lenovo.anyshare.gps:id/sk"))); 



	}

}
