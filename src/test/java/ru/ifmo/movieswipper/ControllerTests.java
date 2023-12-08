package ru.ifmo.movieswipper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ifmo.movieswipper.dto.request.ChangeRoleRequest;
import ru.ifmo.movieswipper.dto.request.LoginRequest;
import ru.ifmo.movieswipper.dto.request.RegisterRequest;
import ru.ifmo.movieswipper.dto.request.SessionAddMovieRequest;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.service.AuthService;
import ru.ifmo.movieswipper.service.UserService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestPropertySource(
        properties = {
                "spring.jpa.hibernate.ddl-auto=create",
                "spring.jpa.generate-ddl=true",
                "spring.jpa.show-sql=true",
                "spring.liquibase.enabled=false",
                "tmdb.api_key=42c5bcb5ea8914d3dbc96eb9a623712a",
                "spring.profiles.active=dev"
        }
)

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-dev.properties")
public class ControllerTests {
    @LocalServerPort
    private Integer port;

    @Value("${root.username}")
    private String rootUsername;
    @Value("${root.password}")
    private String rootPassword;
    private final String testUsername = "test1";
    private final String testPassword = "test1";

    @Autowired
    public AuthService authService;

    @Autowired
    public UserService userService;

    private static User actualUser = null;
    private static User testUser = null;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String testSessionCode = "";

    private static Long tmdbTestMovie = 555L;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1";
        RestAssured.defaultParser = Parser.JSON;
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private RequestSpecification whenAuth(String login, String password) throws JsonProcessingException {
        return
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .auth()
                        .preemptive()
                        .oauth2(
                                given()
                                        .contentType(ContentType.JSON)
                                        .when()
                                        .body(objectMapper.writeValueAsString(new LoginRequest(login, password)))
                                        .post("/auth/login")
                                        .then()
                                        .statusCode(200)
                                        .extract()
                                        .response()
                                        .getBody()
                                        .jsonPath().getString("token"));
    }

    private ValidatableResponse basicGet(String url, String login, String password) throws JsonProcessingException {
        return whenAuth(login, password)
                .get(url)
                .then()
                .statusCode(200)
                .time(lessThan(1500L));
    }

    private ValidatableResponse basicPost(String url, String login, String password, Object obj) throws JsonProcessingException {
        return whenAuth(login, password)
                .body(obj)
                .post(url)
                .then()
                .statusCode(200)
                .time(lessThan(1500L));
    }

    private User getUser() {
        if (actualUser == null)
            actualUser = userService.findByUsername(rootUsername).get();
        return actualUser;
    }

    private User getTestUser() {
        if (testUser == null)
            testUser = userService.findByUsername(testUsername).get();
        return testUser;
    }

    @Test
    @Order(1)
    public void getAuth() throws JsonProcessingException {
        whenAuth(rootUsername, rootPassword);
    }

    @Test
    @Order(2)
    void getCurrentUser() throws JsonProcessingException {
        basicGet("/user/me", rootUsername, rootPassword)
                .body("id", equalTo(getUser().getId().intValue()));
    }

    @Test
    @Order(3)
    void checkPrivilege() throws JsonProcessingException {
        basicGet("/admin/getUsersList", rootUsername, rootPassword);
    }

    @Test
    @Order(4)
    void createUser() throws JsonProcessingException {
        basicPost("/admin/register", rootUsername, rootPassword,
                objectMapper.writeValueAsString(new RegisterRequest(testUsername, testPassword)));
    }

    @Test
    @Order(5)
    void checkNewUser() throws JsonProcessingException {
        basicGet("/user/me", testUsername, testPassword)
                .body("id", equalTo(getTestUser().getId().intValue()));
    }

    @Test
    @Order(6)
    void createSession() throws JsonProcessingException {
        testSessionCode = basicPost("/session/create", rootUsername, rootPassword, "")
                .body("code", matchesPattern("^[a-zA-Z0-9]{8}$"))
                .extract()
                .response()
                .getBody()
                .jsonPath()
                .getString("code");
    }

    @Test
    @Order(7)
    void joinTestUser() throws JsonProcessingException {
        basicGet("/session/join/" + testSessionCode, testUsername, testPassword);
    }

    @Test
    @Order(8)
    void checkCurrentTestUserSession() throws JsonProcessingException {
        basicGet("/session/current", testUsername, testPassword)
                .body("code", equalTo(testSessionCode));
    }

    @Test
    @Order(9)
    void changeRoleTestUser() throws JsonProcessingException {
        basicPost("/admin/changeRole", rootUsername, rootPassword,
                objectMapper.writeValueAsString(new ChangeRoleRequest(testUsername, "ROLE_VIP")));
    }

    @Test
    @Order(10)
    void checkTestUserRole() throws JsonProcessingException {
        basicGet("/user/me", testUsername, testPassword)
                .body("roles[0].name", equalTo("ROLE_VIP"));
    }

    @Test
    @Order(11)
    void addRootMovieLike() throws JsonProcessingException {
        basicPost("/session/addMovie", rootUsername, rootPassword,
                objectMapper.writeValueAsString(new SessionAddMovieRequest(tmdbTestMovie, true)));
        basicPost("/session/addMovie", rootUsername, rootPassword,
                objectMapper.writeValueAsString(new SessionAddMovieRequest(100L, true)));
    }

    @Test
    @Order(12)
    void addTestMovieLike() throws JsonProcessingException {
        basicPost("/session/addMovie", testUsername, testPassword,
                objectMapper.writeValueAsString(new SessionAddMovieRequest(tmdbTestMovie, true)));

        basicPost("/session/addMovie", testUsername, testPassword,
                objectMapper.writeValueAsString(new SessionAddMovieRequest(200L, true)));
    }

    @Test
    @Order(13)
    void checkMatchMovies() throws JsonProcessingException {
        basicGet("/session/getMatchedMovies", rootUsername, rootPassword)
                .body("[0].tmdbMovieId", equalTo(tmdbTestMovie.intValue()))
                .body("[0].users[0].username", equalTo(getTestUser().getUsername()));
    }
}
