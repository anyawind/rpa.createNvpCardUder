package ru.p03.selenium;

import org.openqa.selenium.support.PageFactory;
import org.slf4j.LoggerFactory;

public class CreateNvpCardUde extends WebDriverSettings{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CreateNvpCardUde.class);

    public static void main(String[] args) {
        try {
            logger.info("===НАЧАЛО===");
            System.setProperty("logback.configurationFile", "./logback.xml");
             //запускается робот
             initIEDriver();
             ProcessingRobot processingRobot = PageFactory.initElements(driver, ProcessingRobot.class);
            processingRobot.run();
             processingRobot.close();
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }
        logger.info("===КОНЕЦ===");
    }
}
