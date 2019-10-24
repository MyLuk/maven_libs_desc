package ru.factorts.page;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import utils.ConfigProperties;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;


public class Base {
    static final String alternativeBaseUrl = System.getProperty("alternativeBaseUrl");
    static public String BUrl;
    String BaseUrl = BUrl;

    /**
     * Create and run driver and login
     */

    @BeforeClass
    public static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
        Configuration.driverManagerEnabled = true;
        Configuration.reopenBrowserOnFail = true;
        Configuration.timeout = 20000;
        baseUrlForSelenide();
        BUrl = baseUrl();
        Configuration.remote = ConfigProperties.getTestProperty("endpoint");
//        Configuration.browserCapabilities.setBrowserName("Chrome");
        Configuration.browser = "chrome";
        Configuration.browserCapabilities.setVersion("77.0");
//        Configuration.browser="firefox";
//        Configuration.browserCapabilities.setVersion("69.0");
        Configuration.startMaximized = true;
        Configuration.browserCapabilities.setCapability("enableVNC", true);
        Configuration.browserCapabilities.setCapability("enableVideo", true);
    }

    @After
    public void tearDownClass() throws IOException {
        screen();
        close();
    }

    public File screen() throws IOException {
        return Screenshots.takeScreenShotAsFile();
    }

    @Attachment(value = "Page screenForAllure", type = "image/png")
    public byte[] screenForAllure() throws IOException {
        File file = screen();
        return Files.toByteArray(file);
    }

    public static void baseUrlForSelenide() {
        if (alternativeBaseUrl != null) {
            Configuration.baseUrl = alternativeBaseUrl;
        } else {
            Configuration.baseUrl = ConfigProperties.getTestProperty("BaseUrl");
        }
    }

    public static String baseUrl() {
        if (alternativeBaseUrl != null) {
            BUrl = alternativeBaseUrl;
        } else {
            BUrl = ConfigProperties.getTestProperty("BaseUrl");
        }
        return BUrl;
    }





    public ArrayList<String> getControlSumAllFileInDirectory(File folder, ArrayList<String> arrayList) {
        String controlSum = null;
        File[] folderEntries = folder.listFiles();
        assert folderEntries != null;
        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
//                getControlSumFile(entry);
                continue;
            }
            System.out.println(entry);
            byte[] b = new byte[0];
            try {
                b = java.nio.file.Files.readAllBytes(Paths.get(String.valueOf(entry)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] hash = new byte[0];
            try {
                hash = MessageDigest.getInstance("MD5").digest(b);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            controlSum = DatatypeConverter.printHexBinary(hash);
            System.out.println(controlSum);
            arrayList.add(controlSum);
        }
        return arrayList;

    }
}

