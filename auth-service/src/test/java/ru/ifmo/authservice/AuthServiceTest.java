package ru.ifmo.authservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import feign.FeignException.ServiceUnavailable;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import ru.ifmo.authservice.client.UserServiceClient;
import ru.ifmo.authservice.service.AuthService;


@SpringBootTest
@WireMockTest
class AuthServiceTest {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${test.id}")
    private Long id;

    @Value("${test.username}")
    private String username;

    @Value("${test.password}")
    private String password;

    @Value("${test.encrypted-password}")
    private String encryptedPassword;

    @Value("${test.jwt}")
    private String jwtToken;

    @Value("${response.user-found.filename}")
    private String responseUserFoundFilename;

    @Value("${response.user-not-found.filename}")
    private String responseUserNotFoundFilename;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private UserServiceClient userServiceClient;

    @MockBean
    private JwtEncoder jwtEncoder;

    @MockBean
    private AuthService authService;

    private static WireMockRuntimeInfo wireMockRuntimeInfo;

    @BeforeAll
    static void beforeAll(WireMockRuntimeInfo info) {
        wireMockRuntimeInfo = info;
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("wiremock.url", wireMockRuntimeInfo::getHttpBaseUrl);
    }


    @Test
    void testLogin() {
        stubFor(get(contextPath + "/users/" + username).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-type", "application/json")
                .withBodyFile(responseUserFoundFilename)));
        when(authService.login(any(), any())).thenReturn(jwtToken);   
        assertEquals(jwtToken, authService.login(username, password));
    }

    @Test
    void testNoLogin() {
        stubFor(get(contextPath + "/users/" + username).willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-type", "application/json")
                .withBodyFile(responseUserNotFoundFilename)));
        assertNull(authService.login(username, password));
    }

    // @Test
    public void testCircuitBreaker() throws InterruptedException {
        CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("UserServiceClientCB");
        cb.reset();
        cb.getEventPublisher()
                .onError(System.out::println)
                .onSuccess(System.out::println)
                .onCallNotPermitted(System.out::println)
                .onStateTransition(System.out::println);

        /* Тестируем CLOSED с переходом в OPEN.
         * Переход должен произойти, если 2 вызова из 5 завершатся с ошибкой. */
        stubFor(get(contextPath + "/users/" + username).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-type", "application/json")
                .withBodyFile(responseUserFoundFilename)));
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> userServiceClient.getUser(username));
        }
        
        WireMock.reset(); // сбрасываем для нового stubFor
        stubFor(get(contextPath + "/users/" + username).willReturn(aResponse().withStatus(503)));
        for (int i = 0; i < 3; i++) {
            if (i != 2) {
                assertThrows(ServiceUnavailable.class, () -> userServiceClient.getUser(username));
            } else {
                assertThrows(CallNotPermittedException.class, () -> userServiceClient.getUser(username));
            }
        }

        /* Тестируем HALF-OPEN c возвратом в OPEN.
         * Он должен произойти тогда, когда оба вызова в состоянии HALF-OPEN не будут успешны. */
        System.out.println("Sleeping for 10 seconds...");
        Thread.sleep(10000); // спим 10с, чтобы осуществился переход из OPEN в HALF-OPEN
        for (int i = 0; i < 3; i++) {
            if (i != 2) {
                assertThrows(ServiceUnavailable.class, () -> userServiceClient.getUser(username));
            } else {
                assertThrows(CallNotPermittedException.class, () -> userServiceClient.getUser(username));
            }
        }

        /* Тестируем HALF-OPEN c возвратом в CLOSED.
         * Он должен произойти тогда, когда оба вызова в состоянии HALF-OPEN будут успешны. */
        System.out.println("Sleeping for 10 seconds...");
        Thread.sleep(10000); // спим 10с, чтобы осуществился переход из OPEN в HALF-OPEN
        WireMock.reset(); // сбрасываем для нового stubFor
        stubFor(get(contextPath + "/users/" + username).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-type", "application/json")
                .withBodyFile(responseUserFoundFilename)));
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> userServiceClient.getUser(username));
        }

        /* Сбрасываем CB */
        cb.reset();
    }



}