package com.revature.get_user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.get_user.models.User;
import com.revature.get_user.stubs.TestLogger;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GetUserHandlerTest {

    static TestLogger testLogger;
    static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    GetUserHandler sut;
    Context mockContext;
    UserRepository mockUserRepository;

    @BeforeAll
    public static void beforeAllTests() {
        testLogger = new TestLogger();
    }

    @AfterAll
    public static void afterAllTests() {
        testLogger.close();
    }

    @BeforeEach
    public void beforeEachTest() {
        mockUserRepository = mock(UserRepository.class);
        sut = new GetUserHandler(mockUserRepository);
        mockContext = mock(Context.class);

        when(mockContext.getLogger()).thenReturn(testLogger);
    }

    @AfterEach
    public void afterEachTest() {
        sut = null;
        mockContext = null;
        mockUserRepository = null;
    }

    @Test
    public void given_validId_handlerReturnsValidUser() {
        User expectedUser = User.builder()
                .id("valid")
                .username("valid")
                .favorite_sets(new ArrayList<>())
                .created_sets(new ArrayList<>())
                .profile_picture("valid")
                .points(0)
                .wins(0)
                .losses(0)
                .registration_date("valid")
                .gameRecord(new ArrayList<>())
                .build();

        APIGatewayProxyRequestEvent mockRequestEvent = new APIGatewayProxyRequestEvent();
        mockRequestEvent.withPath("/users");
        mockRequestEvent.withHttpMethod("GET");
        mockRequestEvent.withHeaders(null);
        mockRequestEvent.withBody(null);
        mockRequestEvent.withQueryStringParameters(Collections.singletonMap("id", "valid"));

        when(mockUserRepository.findUserById(anyString())).thenReturn(expectedUser);

        APIGatewayProxyResponseEvent expectedResponse = new APIGatewayProxyResponseEvent();
        expectedResponse.setBody(mapper.toJson(expectedUser));

        APIGatewayProxyResponseEvent actualResponse = sut.handleRequest(mockRequestEvent, mockContext);

        verify(mockUserRepository, times(1)).findUserById(anyString());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void given_invalidId_handlerReturnsBadRequest() {
        APIGatewayProxyRequestEvent mockRequestEvent = new APIGatewayProxyRequestEvent();
        mockRequestEvent.withPath("/users");
        mockRequestEvent.withHttpMethod("GET");
        mockRequestEvent.withHeaders(null);
        mockRequestEvent.withBody(null);
        mockRequestEvent.withQueryStringParameters(Collections.singletonMap("id", "invalid"));

        when(mockUserRepository.findUserById(anyString())).thenReturn(null);

        APIGatewayProxyResponseEvent expectedResponse = new APIGatewayProxyResponseEvent();
        expectedResponse.setStatusCode(HttpStatusCode.BAD_REQUEST);

        APIGatewayProxyResponseEvent actualResponse = sut.handleRequest(mockRequestEvent, mockContext);

        verify(mockUserRepository, times(1)).findUserById(anyString());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void given_noQueryParams_handlerReturnsBadRequest() {
        APIGatewayProxyRequestEvent mockRequestEvent = new APIGatewayProxyRequestEvent();
        mockRequestEvent.withPath("/users");
        mockRequestEvent.withHttpMethod("GET");
        mockRequestEvent.withHeaders(null);
        mockRequestEvent.withBody(null);
        mockRequestEvent.withQueryStringParameters(null);

        APIGatewayProxyResponseEvent expectedResponse = new APIGatewayProxyResponseEvent();
        expectedResponse.setStatusCode(HttpStatusCode.BAD_REQUEST);

        APIGatewayProxyResponseEvent actualResponse = sut.handleRequest(mockRequestEvent, mockContext);

        verify(mockUserRepository, times(0)).findUserById(anyString());
        assertEquals(expectedResponse, actualResponse);
    }
}