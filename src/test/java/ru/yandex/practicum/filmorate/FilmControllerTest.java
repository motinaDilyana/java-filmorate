package ru.yandex.practicum.filmorate;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerTest extends FilmorateApplicationTests {
    private final String filmsUri = "/films";

    @Test
    @Description("Корректные входные данные при POST /films")
    public void postFilmsShouldReturnSuccess() throws Exception {
        Film film = new Film(3, "dolore", "Nick Name", LocalDate.of(1946, 8, 20), 100);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(film));
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @Description("name empty при  POST /films")
    public void postFilmsShouldReturnBadRequestEmptyName() throws Exception {
        Film film = new Film(1, "", "Nick Name", LocalDate.of(1946, 8, 20), 100);

        mvc.perform(MockMvcRequestBuilders.post(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("description too long при  POST /films")
    public void postFilmsShouldReturnBadRequestDescriptionLong() throws Exception {
        Film film = new Film(1, "test", "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.", LocalDate.of(1946, 8, 20), 100);

        mvc.perform(MockMvcRequestBuilders.post(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("incorrect release date при  POST /films")
    public void postFilmsShouldReturnBadRequestReleaseDateIncorrect() throws Exception {
        Film film = new Film(1, "test", "Nick Name", LocalDate.of(1846, 8, 20), 100);

        mvc.perform(MockMvcRequestBuilders.post(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("duration incorrect  POST /films")
    public void postFilmsShouldReturnBadRequestDurationIncorrect() throws Exception {
        Film film = new Film(1, "test", "Nick Name", LocalDate.of(1946, 8, 20), -100);
        mvc.perform(MockMvcRequestBuilders.post(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("Позитивный сценарий обнолвения  PUT /films")
    public void putFilmsShouldReturnSuccessInputCorrect() throws Exception {
        Film filmPost = new Film(1, "test", "Nick Name", LocalDate.of(1946, 8, 20), 100);
        //Создали пользователя
        mvc.perform(MockMvcRequestBuilders.post(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmPost)))
                .andReturn();

        Film filmPut = new Film(1, "test222", "Nick Name", LocalDate.of(1946, 8, 20), 100);
        //Обновили пользователя
        MvcResult mvcResultPut = mvc.perform(MockMvcRequestBuilders.put(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmPut)))
                .andReturn();


        String actualResponseBody = mvcResultPut.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(filmPut);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                expectedResponseBody);
        assertEquals(200, mvcResultPut.getResponse().getStatus());
    }

    @Test
    @Description("incorrect film id  PUT /films")
    public void putFilmsShouldReturnNotFoundUserIdIncorrect() throws Exception {
        Film filmPost = new Film(99999, "test", "Nick Name", LocalDate.of(1946, 8, 20), 100);
        MvcResult mvcResultPut =
                mvc.perform(MockMvcRequestBuilders.put(filmsUri)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(filmPost)))
                        .andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResultPut.getResponse().getStatus());
    }

    @Test
    @Description("Non films was creared GET /films")
    public void getFilmsShouldReturnEmptyList() throws Exception {
        MvcResult mvcResultPut =
                mvc.perform(MockMvcRequestBuilders.get(filmsUri)
                                .contentType("application/json")
                                .content(""))
                        .andReturn();
        assertEquals(200, mvcResultPut.getResponse().getStatus());
        assertEquals(0, mvcResultPut.getResponse().getContentLength());
    }

    @Test
    @Description("one film was created GET /films")
    public void getFilmsShouldReturnSuccessOneObject() throws Exception {
        Film film = new Film(1, "test", "Nick Name", LocalDate.of(1946, 8, 20), 100);
        //Создали фильм
        mvc.perform(MockMvcRequestBuilders.post(filmsUri)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andReturn();

        MvcResult mvcResultPut =
                mvc.perform(MockMvcRequestBuilders.get(filmsUri)
                                .contentType("application/json")
                                .content(""))
                        .andReturn();
        assertEquals(200, mvcResultPut.getResponse().getStatus());
    }

}