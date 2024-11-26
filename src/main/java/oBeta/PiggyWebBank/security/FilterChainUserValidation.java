//package oBeta.PiggyWebBank.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletRequestWrapper;
//import jakarta.servlet.http.HttpServletResponse;
//import oBeta.PiggyWebBank.payloads.UserDTO;
//import oBeta.PiggyWebBank.payloads.login.LoginDTO;
//import org.apache.coyote.BadRequestException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//
//
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class FilterChainUserValidation extends OncePerRequestFilter {
//
//    private final ObjectMapper objectMapper;
//
//    public FilterChainUserValidation() {
//        this.objectMapper = new ObjectMapper();
//        this.objectMapper.registerModule(new JavaTimeModule());
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // Leggi il corpo della richiesta manualmente
//        StringBuilder sb = new StringBuilder();
//        String line;
//        try (BufferedReader reader = request.getReader()) {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//        }
//        String requestBody = sb.toString();
//
//        // Controllo personalizzato sul corpo della richiesta
//        try {
//            LoginDTO userDTO = objectMapper.readValue(requestBody, LoginDTO.class);
//            System.out.println("Request JSON Body: " + requestBody);
//            System.out.println(userDTO);
//
//            // Logica di validazione (esempio)
//
//
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("Errore nel processamento del payload.");
//            return;
//        }
//
//        // Wrappa la richiesta originale con il corpo letto
//        HttpServletRequest wrappedRequest = new CustomHttpServletRequest(request, requestBody);
//
//        // Procedi con la catena di filtri
//        filterChain.doFilter(wrappedRequest, response);
//    }
//
//    // Classe personalizzata per wrappare la richiesta con il corpo letto
//    private static class CustomHttpServletRequest extends HttpServletRequestWrapper {
//
//        private final String body;
//
//        public CustomHttpServletRequest(HttpServletRequest request, String body) {
//            super(request);
//            this.body = body;
//        }
//
//        @Override
//        public BufferedReader getReader() throws IOException {
//            return new BufferedReader(
//                    new InputStreamReader(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)))
//            );
//        }
//    }
//
//}
