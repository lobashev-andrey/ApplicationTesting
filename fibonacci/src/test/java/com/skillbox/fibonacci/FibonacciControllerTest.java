package com.skillbox.fibonacci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FibonacciControllerTest extends PostgresTestContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("when find by index > 0 then status ok and return proper value")
    public void whenFindByIndexGraterThan_0_thenReturnProperValue() throws Exception {

        String actual = mockMvc.perform(get("/fibonacci/10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = "{\"index\":10,\"value\":55}";
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("when find by index < 1 then status 400 and message")
    public void whenFindByIndexLessThan_1_thenStatus_400() throws Exception {

        var response = mockMvc.perform(get("/fibonacci/-8"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        String actual = response.getContentAsString();
        String expected = "Index should be greater or equal to 1";

        assertEquals(expected, actual);
    }
}