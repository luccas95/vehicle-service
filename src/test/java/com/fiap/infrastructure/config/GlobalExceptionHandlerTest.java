package com.fiap.infrastructure.config;

import com.fiap.infrastructure.exception.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void whenRuntimeException_thenBadRequest() throws Exception {
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Runtime error occurred"));
    }

    @Test
    void whenNotFoundException_thenNotFound() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Vehicle not found"));
    }

    @Test
    void whenGeneralException_thenInternalServerError() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("General error occurred"));
    }

    @Test
    void whenValidationException_thenBadRequest() throws Exception {
        String invalidJson = "{}"; // Campo "name" ausente

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.messages.name").value("Name is mandatory"));
    }

    // 👇 Test controller fake só para disparar as exceções no teste
    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/runtime")
        public void throwRuntime() {
            throw new RuntimeException("Runtime error occurred");
        }

        @GetMapping("/notfound")
        public void throwNotFound() {
            throw new NotFoundException("Vehicle not found");
        }

        @GetMapping("/exception")
        public void throwGeneral() throws Exception {
            throw new Exception("General error occurred");
        }

        @PostMapping("/validation")
        public void validateInput(@Valid @RequestBody DummyRequest request) {
            // Simplesmente dispara a validação
        }

        static class DummyRequest {
            @NotBlank(message = "Name is mandatory")
            public String name;
        }
    }
}
