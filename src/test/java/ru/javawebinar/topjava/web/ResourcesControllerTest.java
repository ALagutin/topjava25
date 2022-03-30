package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourcesControllerTest extends AbstractControllerTest{

    @Test
    void getStyleCss() throws Exception {
        perform(get("/resources/css/style.css"))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith("text/css;charset=UTF-8"));
                .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("text/css;charset=UTF-8")));
    }
}
