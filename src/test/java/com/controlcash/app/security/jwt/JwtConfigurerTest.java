package com.controlcash.app.security.jwt;

import jakarta.servlet.Filter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class JwtConfigurerTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private HttpSecurity httpSecurity;

    @InjectMocks
    private JwtConfigurer jwtConfigurer;

    @Test
    void testConfigure_ShouldAddFilterToHttp() {
        jwtConfigurer.configure(httpSecurity);

        Mockito.verify(httpSecurity, Mockito.only()).addFilterBefore(Mockito.any(Filter.class), Mockito.any());
    }
}
