package ru.netology.rest;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DebitCardOrderTest {
    private WebDriver driver;


    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup(); //авто настройка драйверов для хрома
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;


    }


    @Test
    void debitCardRegistration() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Клоков Алексей");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79033003333");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(result.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());


    }

    /// //////////Невалидные тесты на строку phone

    @Test
    void registrationNumberIncorrect() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Клоков Алексей");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89033003333");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void numberLessThan11() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Клоков Алексей");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("8903300");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test
    void emptyNumberField() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Клоков Алексей");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());

    }

    /// //////////Невалидные тесты на строку name

    @Test
    void emptyNameField() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79033003333");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void nameInEnglish() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Aleksyi Klokov");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79033003333");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void nameWithAnApostrophe() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Шарль Д’Артаньян");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79033003333");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }


    // тест без согласия обработки данных

    @Test
    void withoutConsent() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Клоков Алексей");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79033003333");
        driver.findElement(By.tagName("button")).click();
       assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());




    }
}
