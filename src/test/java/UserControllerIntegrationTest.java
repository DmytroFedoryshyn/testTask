import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import test.Application;
import test.dto.DateRangeDto;
import test.dto.UserRequestDto;
import test.dto.UserResponseDto;
import test.repository.UserRepository;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository repository;

    @AfterEach
    public void clearRepository() {
        repository.deleteAll();
    }


    @Test
    public void testRegisterUserValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getSampleUserJSON()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void testRegisterUserInvalid() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setBirthDate(LocalDate.now().plusDays(1));
        String jsonDto = objectMapper.writeValueAsString(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDto))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists());
    }


    @Test
    public void testPartialUpdateUser() throws Exception {
        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getSampleUserJSON()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String createdUserJson = createResult.getResponse().getContentAsString();
        UserResponseDto createdUser = objectMapper.readValue(createdUserJson, UserResponseDto.class);

        UserRequestDto updateUserDto = new UserRequestDto();
        updateUserDto.setBirthDate(LocalDate.of(2002, 10, 10));
        String updateUserJson = objectMapper.writeValueAsString(updateUserDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/partialUpdate/" + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("2002-10-10"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getSampleUserJSON()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String createdUserJson = createResult.getResponse().getContentAsString();
        UserResponseDto createdUser = objectMapper.readValue(createdUserJson, UserResponseDto.class);

        UserRequestDto updateUserDto = new UserRequestDto();
        updateUserDto.setFirstName("Jane");
        updateUserDto.setLastName("Doe");
        updateUserDto.setEmail("email@example.com");
        updateUserDto.setBirthDate(LocalDate.now().minusYears(20));
        String updateUserJson = objectMapper.writeValueAsString(updateUserDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update/" + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jane"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getSampleUserJSON()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String createdUserJson = createResult.getResponse().getContentAsString();
        UserResponseDto createdUser = objectMapper.readValue(createdUserJson, UserResponseDto.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/" + createdUser.getId()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    public void testSearchByBirthDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getSampleUserJSON()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        LocalDate fromDate = LocalDate.now().minusYears(30);
        LocalDate toDate = LocalDate.now().minusYears(20);

        DateRangeDto dateRangeDto = new DateRangeDto();
        dateRangeDto.setFrom(fromDate);
        dateRangeDto.setTo(toDate);
        String searchDtoJson = objectMapper.writeValueAsString(dateRangeDto);

        MvcResult searchResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/searchByBirthDate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchDtoJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String searchResultJson = searchResult.getResponse().getContentAsString();
        List<UserResponseDto> searchResults = objectMapper.readValue(searchResultJson, new TypeReference<>() {
        });

        for (UserResponseDto user : searchResults) {
            LocalDate userBirthDate = user.getBirthDate();
            assertTrue(userBirthDate.isAfter(fromDate) || userBirthDate.isEqual(fromDate));
            assertTrue(userBirthDate.isBefore(toDate) || userBirthDate.isEqual(toDate));
        }
    }

    public String getSampleUserJSON() throws JsonProcessingException {
        UserRequestDto dto = new UserRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("email@example.com");
        dto.setBirthDate(LocalDate.now().minusYears(20));

        return objectMapper.writeValueAsString(dto);
    }
}