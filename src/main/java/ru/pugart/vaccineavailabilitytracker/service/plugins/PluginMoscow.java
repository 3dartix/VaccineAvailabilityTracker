package ru.pugart.vaccineavailabilitytracker.service.plugins;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ru.pugart.vaccineavailabilitytracker.config.DriverInitializer;
import ru.pugart.vaccineavailabilitytracker.dto.Location;
import ru.pugart.vaccineavailabilitytracker.utils.LocationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.pugart.vaccineavailabilitytracker.enums.Status.*;

@Slf4j
public class PluginMoscow extends Plugin {

    public PluginMoscow() {
        pluginName = "MOSCOW";
    }

    private void timeout(WebDriver driver){
        driver.manage().timeouts().implicitlyWait(delay, TimeUnit.SECONDS);
    }

    @SneakyThrows
    @Override
    void start(){
        setState(PROCESSING);

        WebDriver driver = DriverInitializer.getWebDriver();

        driver.get("https://www.mos.ru/city/projects/covid-19/privivka/#map");
        List<Location> locations = new ArrayList<>();

        timeout(driver);
        WebElement buttonOpen = driver.findElement(By.xpath("//*[@data-hidden-rec-ids=\"273975447\"]"));
        buttonOpen.click();

        timeout(driver);
        List<WebElement> elementList = driver.findElements(By.xpath("//*[@id=\"rec273975447\"]/div/div/div"));

        for (WebElement element : elementList) {
            locations.add(new LocationBuilder()
                    .setStartDate(element.findElement(By.cssSelector("div.t521__leftcol")).getText())
                    .setAddresses(element.findElement(By.cssSelector("div.t521__rightcol")).getText().split("\n\n"))
                    .build());
        }

        timeout(driver);
        driver.findElement(By.xpath("//*[@id=\"widget-vaccination-map\"]/div/div[2]/a[2]")).click();

        timeout(driver);

        Select select = new Select(driver.findElement(By.xpath("//*[@id=\"widget-vaccination-map\"]/div/div[3]/div[2]/select")));

        for (int i = 0; i < select.getOptions().size(); i++) {
            select.selectByIndex(i);
            Thread.sleep(60);

            try {
                elementList = driver.findElements(By.xpath("//*[@id=\"widget-vaccination-map\"]/div/div[4]"));

                for (WebElement element : elementList) {
                    locations.add(new LocationBuilder()
                            .setName(element.findElement(By.cssSelector("div.widget-vaccination-map-point-name")).getText())
                            .setPhone(element.findElement(By.cssSelector("div.widget-vaccination-map-point-phone")).getText())
                            .setAddresses(element.findElement(By.cssSelector("div.widget-vaccination-map-point-address")).getText())
                            .build());
                }
            } catch (Exception ex) {
                setState(ERROR);
                log.info("Parser error: {}", ex.getLocalizedMessage());
            }
        }

        driver.close();

        locations.forEach(System.out::println);

        setState(COMPLETE);
    }

    @Override
    public void run() {
        locations = new ArrayList<>();
        try {
            start();
        } catch (Exception ex){
            log.info("run exception: {}", ex.getLocalizedMessage());
            setState(ERROR);
        }
    }
}
