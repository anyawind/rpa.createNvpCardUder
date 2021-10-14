package ru.p03.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class WebDriverSettings {
    public static WebDriver driver;

    public static void initIEDriver() {
        File file = new File(AppEnv.getContext().getIEDriver());
        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
        DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
        caps.setCapability("ignoreProtectedModeSettings", true);
        caps.setCapability("ignoreZoomSetting", true);
        driver = new InternetExplorerDriver(caps);
    }
}
