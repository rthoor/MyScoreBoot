package ru.itis.myscore.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.itis.myscore.dto.PlayerDto;
import ru.itis.myscore.security.details.AccountDetailsService;
import ru.itis.myscore.service.PlayerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PlayerController.class)
@DisplayName("PlayerControllerTest is working when ...")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PlayerControllerTest {
    private static final PlayerDto PLAYER_1 = PlayerDto.builder()
            .id(1L).firstName("Артур").lastName("Бадамшин").build();

    private static final PlayerDto PLAYER_2 = PlayerDto.builder()
            .id(2L).firstName("Marco").lastName("Reus").build();

    private static final PlayerDto PLAYER_3 = PlayerDto.builder()
            .id(3L).firstName("Cris").lastName("Penaldo").build();

    private static final List<PlayerDto> PLAYERS_LIST = Arrays.asList(PLAYER_1, PLAYER_2, PLAYER_3);

    private static final PlayerDto NEW_PLAYER = PlayerDto.builder()
            .firstName("Leo").lastName("Pepsi").countryId(7L)
            .teamId(10L).position("Forward").number(30).build();

    private static final PlayerDto CREATED_PLAYER = PlayerDto.builder()
            .id(777L).firstName("Leo").lastName("Pepsi")
            .countryId(7L).teamId(10L).position("Forward").number(30).build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private AccountDetailsService accountDetailsService;

    private final String TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJqd3RDcmVhdGVkRGF0ZSI6IjIwMjItMDYtMDYgMDA6NDA6MDEuMDczIiwic3RhdGUiOiJDT05GSVJNRUQiLCJlbWFpbCI6InJ0aG9vckBtYWlsLnJ1In0.NOzs0NsRmrbw6iJL8OyqS4oLIKwM5g_noIKDt7Z8Txs";

    @BeforeEach
    void setUp() throws Exception {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1987-06-24");
            NEW_PLAYER.setDateOfBirth(date);
            CREATED_PLAYER.setDateOfBirth(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        when(playerService.getPlayer(1L)).thenReturn(PLAYER_1);
        when(playerService.getPlayer(2L)).thenReturn(PLAYER_2);
        when(playerService.addPlayer(NEW_PLAYER)).thenReturn(CREATED_PLAYER);
    }

    @Test
    public void return_403_without_token() throws Exception {
        mockMvc.perform(get("/players/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void return_player_with_id_1() throws Exception {
        mockMvc.perform(get("/players/1")
                        .header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("firstName", is("Артур")))
                .andExpect(jsonPath("lastName", is("Бадамшин")));
    }

    @Test
    public void return_player_with_id_2() throws Exception {
        mockMvc.perform(get("/players/2")
                        .header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(2)))
                .andExpect(jsonPath("firstName", is("Marco")))
                .andExpect(jsonPath("lastName", is("Reus")));
    }

    @Test
    public void return_all_players() throws Exception {
        mockMvc.perform(get("/authors")
                        .header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("players", hasSize(3)))
                .andExpect(jsonPath("players[0].id", is(1)))
                .andExpect(jsonPath("players[0].firstName", is("Артур")))
                .andExpect(jsonPath("players[0].lastName", is("Бадамшин")))
                .andExpect(jsonPath("players[1].id", is(2)))
                .andExpect(jsonPath("players[1].firstName", is("Marco")))
                .andExpect(jsonPath("players[1].lastName", is("Reus")))
                .andExpect(jsonPath("players[2].id", is(3)))
                .andExpect(jsonPath("players[2].firstName", is("Cris")))
                .andExpect(jsonPath("players[2].lastName", is("Penaldo")));;
    }

    @Test
    public void add_player_with_valid_names() throws Exception {
        mockMvc.perform(post("/players")
                        .header("Authorization", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"countryId\": 7,\n" +
                                "  \"dateOfBirth\": \"1987-06-24\",\n" +
                                "  \"firstName\": \"Leo\",\n" +
                                "  \"lastName\": \"Pepsi\",\n" +
                                "  \"number\": 30,\n" +
                                "  \"position\": \"Forward\",\n" +
                                "  \"teamId\": 10\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(777)))
                .andExpect(jsonPath("firstName", is("Leo")))
                .andExpect(jsonPath("lastName", is("Pepsi")))
                .andDo(print());
    }

}