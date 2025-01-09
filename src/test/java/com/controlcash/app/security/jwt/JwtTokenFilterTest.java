package com.controlcash.app.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class JwtTokenFilterTest {

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletResponse servletResponse;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testDoFilter_ShouldSetAuthentication_WhenTokenIsValid() throws IOException, ServletException {
        String token = "validToken";
        Mockito.when(jwtTokenProvider.resolveToken(httpServletRequest)).thenReturn(token);
        Mockito.when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        Mockito.when(jwtTokenProvider.getAuthentication(token)).thenReturn(authentication);

        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        Mockito.verify(securityContext).setAuthentication(authentication);
        Mockito.verify(filterChain).doFilter(httpServletRequest, servletResponse);
    }

    @Test
    void testDoFilter_ShouldNotSetAuthentication_WhenAuthenticationIsNull() throws IOException, ServletException {
        String token = "validToken";
        Mockito.when(jwtTokenProvider.resolveToken(httpServletRequest)).thenReturn(token);
        Mockito.when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        Mockito.when(jwtTokenProvider.getAuthentication(token)).thenReturn(null);

        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        Mockito.verify(securityContext, Mockito.never()).setAuthentication(authentication);
        Mockito.verify(filterChain).doFilter(httpServletRequest, servletResponse);
    }

    @Test
    void testDoFilter_ShouldNotSetAuthentication_WhenTokenIsInvalid() throws IOException, ServletException {
        String token = "invalidToken";
        Mockito.when(jwtTokenProvider.resolveToken(httpServletRequest)).thenReturn(token);
        Mockito.when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        Mockito.verify(securityContext, Mockito.never()).setAuthentication(Mockito.any());
        Mockito.verify(filterChain).doFilter(httpServletRequest, servletResponse);
    }

    @Test
    void testDoFilter_ShouldNotSetAuthentication_WhenTokenIsNull() throws IOException, ServletException {
        Mockito.when(jwtTokenProvider.resolveToken(httpServletRequest)).thenReturn(null);

        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        Mockito.verify(securityContext, Mockito.never()).setAuthentication(Mockito.any());
        Mockito.verify(filterChain).doFilter(httpServletRequest, servletResponse);
    }
}
