package com.nnk.springboot;

import com.nnk.springboot.services.impl.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    private JWTService jwtService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Test
    void shouldReturnAValidateTokenWithTrue() {
        //Arrange
        String username = "testUsername";
        when(userDetails.getUsername()).thenReturn(username);
        when(authentication.getName()).thenReturn(username);

        String token = jwtService.generateToken(authentication);

        //Act
        boolean result = jwtService.validateToken(token, userDetails);

        //Assert
        assertTrue(result);
    }

}
