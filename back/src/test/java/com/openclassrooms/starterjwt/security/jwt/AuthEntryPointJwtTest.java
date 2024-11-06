package com.openclassrooms.starterjwt.security.jwt;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;

class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private ServletOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        // Initialize les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCommence() throws IOException, ServletException {
        // Arrange: Préparer les objets nécessaires pour le test
        when(authException.getMessage()).thenReturn("Unauthorized");
        when(request.getServletPath()).thenReturn("/test-path");
        when(response.getOutputStream()).thenReturn(outputStream);

        // Crée un mock pour capturer les données JSON écrites dans la réponse
        ArgumentCaptor<byte[]> responseCaptor = ArgumentCaptor.forClass(byte[].class);

        // Act: Appeler la méthode commence() pour simuler le traitement de l'exception
        authEntryPointJwt.commence(request, response, authException);

        // Assert: Vérifier les interactions sur la réponse
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Capturer le corps de la réponse et vérifier son contenu
        verify(outputStream).write(responseCaptor.capture(), eq(0), anyInt());

        // Convertir les données capturées en une Map pour vérifier son contenu
        byte[] responseBytes = responseCaptor.getValue();
        String responseBody = new String(responseBytes);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> actualResponseBody = objectMapper.readValue(responseBody, Map.class);

        // Créer la réponse attendue
        Map<String, Object> expectedResponseBody = Map.of(
                "status", HttpServletResponse.SC_UNAUTHORIZED,
                "error", "Unauthorized",
                "message", "Unauthorized",
                "path", "/test-path");

        // Comparer les deux réponses
        assertEquals(expectedResponseBody, actualResponseBody,
                "Le corps de la réponse ne correspond pas à la réponse attendue");
    }
}
