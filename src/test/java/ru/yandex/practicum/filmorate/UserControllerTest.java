package ru.yandex.practicum.filmorate;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends FilmorateApplicationTests {
    private final String usersUri = "/users";

    @Test
    @Description("Корректные входные данные при POST /users")
    public void postUsersShouldReturnSuccess() throws Exception {
        User user = new User(1, "dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(user));
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @Description("login с empty при  POST /users")
    public void postUsersShouldReturnBadRequestEmptyLogin() throws Exception {
        User user = new User(1, "  ", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));

        mvc.perform(MockMvcRequestBuilders.post(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("not email format при  POST /users")
    public void postUsersShouldReturnBadRequestEmailIncorrect() throws Exception {
        User user = new User(1, "login", "Nick Name", "mail.ru", LocalDate.of(1946, 8, 20));

        mvc.perform(MockMvcRequestBuilders.post(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("incorrect date при  POST /users")
    public void postUsersShouldReturnBadRequestReleaseDateIncorrect() throws Exception {
        User user = new User(1, "login", "Nick Name", "mail@mail.ru", LocalDate.of(2445, 8, 20));

        mvc.perform(MockMvcRequestBuilders.post(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("empty name при  POST /users")
    public void postUsersShouldReturnSuccessNameIsEmpty() throws Exception {
        User user = new User(1, "dolore", null, "mail@mail.ru", LocalDate.of(1946, 8, 20));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(
                new User(3, "dolore", "dolore", "mail@mail.ru", LocalDate.of(1946, 8, 20)));
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                expectedResponseBody);
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @Description("Позитивный сценарий обнолвения  PUT /users")
    public void putUsersShouldReturnSuccessInputCorrect() throws Exception {
        User userPost = new User(1, "dolore", "test", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        //Создали пользователя
        mvc.perform(MockMvcRequestBuilders.post(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userPost)))
                .andReturn();

        User userPut = new User(1, "dolore", "test", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        //Обновили пользователя
        MvcResult mvcResultPut = mvc.perform(MockMvcRequestBuilders.put(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userPut)))
                .andReturn();


        String actualResponseBody = mvcResultPut.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(userPut);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                expectedResponseBody);
        assertEquals(200, mvcResultPut.getResponse().getStatus());
    }

    @Test
    @Description("incorrect user id  PUT /users")
    public void putUsersShouldReturnNotFoundUserIdIncorrect() throws Exception {
        User userPut = new User(9999, "dolore", "test", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        MvcResult mvcResultPut =
                mvc.perform(MockMvcRequestBuilders.put(usersUri)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(userPut)))
                        .andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResultPut.getResponse().getStatus());
    }

    @Test
    @Description("Non users was creared GET /users")
    public void getUsersShouldReturnEmptyList() throws Exception {
        MvcResult mvcResultPut =
                mvc.perform(MockMvcRequestBuilders.get(usersUri)
                                .contentType("application/json")
                                .content(""))
                        .andReturn();
        assertEquals(200, mvcResultPut.getResponse().getStatus());
        assertEquals(0, mvcResultPut.getResponse().getContentLength());
    }

    @Test
    @Description("one user was created GET /users")
    public void getUsersShouldReturnSuccessOneObject() throws Exception {
        User user = new User(1, "dolore", "test", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        //Создали пользователя
        mvc.perform(MockMvcRequestBuilders.post(usersUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn();

        MvcResult mvcResultPut =
                mvc.perform(MockMvcRequestBuilders.get(usersUri)
                                .contentType("application/json")
                                .content(""))
                        .andReturn();
        assertEquals(200, mvcResultPut.getResponse().getStatus());
    }
}
