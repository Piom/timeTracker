package ru.piom.time;

import com.codeborne.selenide.AuthenticationType;
import com.codeborne.selenide.Condition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.addListener;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;

public abstract class FillTimeTracker {
    private static String username = System.getProperty("time.username", "xxx");
    private static String password = System.getProperty("time.password", "xxx");

    @BeforeClass
    public static void openInbox() {
        timeout = 10000;
        baseUrl = "https://tt.muranosoft.com";
        startMaximized = false;
        browser = "chrome";
        browserPosition = "890x10";
        browserSize = "780x950";
        addListener(new Highlighter());

        open("/TimeEntry.aspx", AuthenticationType.BASIC, username, password);
        waitUntilPagesIsLoaded();
    }

    protected static void waitUntilPagesIsLoaded() {
        $(By.id("ctl00__content_ddlProjects")).waitUntil(Condition.visible, 10000);
    }

    @AfterClass
    public static void logout() {
        closeWebDriver();
    }

}
