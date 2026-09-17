import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CheckPoint 5 - Login")
public class Login {

    private static final String BASE_URL = "https://www.saucedemo.com/";

    private static final String USUARIO_VALIDO = "standard_user";
    private static final String SENHA_VALIDA = "secret_sauce";
    private static final String USUARIO_BLOQUEADO = "locked_out_user";
    private static final String USUARIO_INVALIDO = "usuario_invalido";
    private static final String SENHA_INVALIDA = "senha_invalida";
    private static final String CAMPO_VAZIO = "";

    private static final By CAMPO_USUARIO = By.id("user-name");
    private static final By CAMPO_SENHA = By.id("password");
    private static final By BOTAO_LOGIN = By.id("login-button");
    private static final By ICONE_CARRINHO = By.className("shopping_cart_link");
    private static final By MENSAGEM_ERRO = By.cssSelector("[data-test='error']");

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void abrirNavegador() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("CT1 - Login com sucesso (200/302)")
    void deveLogarComCredenciaisValidas() {
        // Dado: que esteja na página saucedemo.com
        driver.get(BASE_URL);
        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals("Swag Labs", driver.getTitle());

        // Quando: inserir usuário e senha válidos
        driver.findElement(CAMPO_USUARIO).sendKeys(USUARIO_VALIDO);
        driver.findElement(CAMPO_SENHA).sendKeys(SENHA_VALIDA);

        // E: clicar no botão "Login"
        driver.findElement(BOTAO_LOGIN).click();

        // Então: o sistema autentica e redireciona para inventory.html
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        assertEquals(BASE_URL + "inventory.html", driver.getCurrentUrl());
        assertTrue(driver.findElement(ICONE_CARRINHO).isDisplayed());
    }

    @Test
    @DisplayName("CT2 - Login com credenciais inválidas (401)")
    void naoDeveLogarComCredenciaisInvalidas() {
        // Dado: que esteja na página saucedemo.com
        driver.get(BASE_URL);

        // Quando: inserir usuário e senha inválidos
        driver.findElement(CAMPO_USUARIO).sendKeys(USUARIO_INVALIDO);
        driver.findElement(CAMPO_SENHA).sendKeys(SENHA_INVALIDA);

        // E: clicar no botão "Login"
        driver.findElement(BOTAO_LOGIN).click();

        // Então: o sistema recusa a autenticação e exibe mensagem de erro
        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(MENSAGEM_ERRO));
        assertTrue(erro.getText().contains("do not match"));
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("CT3 - Login com usuário sem permissão / bloqueado (403)")
    void naoDeveLogarComUsuarioBloqueado() {
        // Dado: que esteja na página saucedemo.com
        driver.get(BASE_URL);

        // Quando: inserir credenciais válidas de um usuário bloqueado
        driver.findElement(CAMPO_USUARIO).sendKeys(USUARIO_BLOQUEADO);
        driver.findElement(CAMPO_SENHA).sendKeys(SENHA_VALIDA);

        // E: clicar no botão "Login"
        driver.findElement(BOTAO_LOGIN).click();

        // Então: o sistema nega o acesso e informa que o usuário está bloqueado
        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(MENSAGEM_ERRO));
        assertTrue(erro.getText().contains("locked out"));
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("CT4 - Login com campos obrigatórios vazios (400)")
    void naoDeveLogarComCamposVazios() {
        // Dado: que esteja na página saucedemo.com
        driver.get(BASE_URL);

        // Quando/E: clicar em "Login" sem informar usuário e senha
        driver.findElement(BOTAO_LOGIN).click();

        // Então: o sistema rejeita a requisição e informa que os dados são obrigatórios
        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(MENSAGEM_ERRO));
        assertTrue(erro.getText().contains("Username is required"));
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("CT5 - Login com usuário correto e senha incorreta (401)")
    void naoDeveLogarComSenhaIncorreta() {
        // Dado: que esteja na página saucedemo.com
        driver.get(BASE_URL);

        // Quando: inserir um usuário válido e uma senha incorreta
        driver.findElement(CAMPO_USUARIO).sendKeys(USUARIO_VALIDO);
        driver.findElement(CAMPO_SENHA).sendKeys(SENHA_INVALIDA);

        // E: clicar no botão "Login"
        driver.findElement(BOTAO_LOGIN).click();

        // Então: o sistema recusa a autenticação, mesmo com usuário correto
        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(MENSAGEM_ERRO));
        assertTrue(erro.getText().contains("do not match"));
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("CT6 - Login com campo de usuário vazio (400)")
    void naoDeveLogarComUsuarioVazio() {
        // Dado: que esteja na página saucedemo.com
        driver.get(BASE_URL);

        // Quando: deixar o usuário vazio e preencher apenas a senha
        driver.findElement(CAMPO_USUARIO).sendKeys(CAMPO_VAZIO);
        driver.findElement(CAMPO_SENHA).sendKeys(SENHA_VALIDA);

        // E: clicar no botão "Login"
        driver.findElement(BOTAO_LOGIN).click();

        // Então: o sistema rejeita a requisição por falta do campo obrigatório
        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(MENSAGEM_ERRO));
        assertTrue(erro.getText().contains("Username is required"));
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }
}
