package com.example.login.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Si no hay un usuario en la sesion, manda al login en vez de dejar
 * entrar directo al dashboard u otras paginas escribiendo la URL.
 */
@Component
public class SesionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean logueado = session != null && session.getAttribute("usuario") != null;
        if (!logueado) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
