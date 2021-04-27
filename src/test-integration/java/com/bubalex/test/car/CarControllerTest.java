package com.bubalex.test.car;

import com.bubalex.test.AbstractBaseIT;
import com.bubalex.test.web.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.bubalex.test.TestData.*;
import static com.bubalex.test.web.model.ApiModelBody.SEDAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CarControllerTest extends AbstractBaseIT {

    public static final String CAR_ID = "ca03a5f6-043c-4aa2-8519-d2cb8438fcaa";
    public static final String CAR_TO_DELETE_ID = "1ed43f75-a4d3-4dd7-ad5a-c4a0869e16c2";
    public static final String NON_EXISTING_CAR_ID = "e66c4362-1111-1111-1111-d0a4ac39ecde";

    @Test
    void createCar() throws Exception {
        var carRequest = getMockedCar();
        var mvcResult = mockMvc.perform(post("/cars")
                .content(objectMapper.writeValueAsString(carRequest))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        var responseBody = mvcResult.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseBody, ApiModelCar.class);
        assertEquals(carRequest.getMake(), response.getMake());
        assertEquals(carRequest.getModel(), response.getModel());
        assertEquals(carRequest.getCapacity(), response.getCapacity());
        assertEquals(carRequest.getVolume(), response.getVolume());
        assertEquals(carRequest.getBody(), response.getBody());
        assertEquals(carRequest.getTransmission(), response.getTransmission());
        assertEquals(carRequest.getDrive(), response.getDrive());
        assertEquals(carRequest.getIssueMonth(), response.getIssueMonth());
        assertEquals(carRequest.getIssueYear(), response.getIssueYear());
    }

    @Test
    void createCarWithNullRequest() throws Exception {
        mockMvc.perform(post("/cars")
                .content(objectMapper.writeValueAsString(null))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        assertTrue(true);
    }

    @Test
    void getCar() throws Exception {
        UUID uuid = UUID.fromString(CAR_ID);
        MvcResult mvcResult = mockMvc.perform(get("/cars/{carId}", uuid)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseBody, ApiModelCar.class);
        assertEquals(uuid, response.getId());
    }

    @Test
    void getCarWithWrongUUID() throws Exception {
        UUID uuid = UUID.fromString(NON_EXISTING_CAR_ID);
        mockMvc.perform(get("/cars/{carId}", uuid)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        assertTrue(true);
    }

    @Test
    void getCars() throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(PAGE_NUMBER, "1")
                .param(PAGE_SIZE, "50")
                .param(SORT, "+model")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(6, response.getItems().size());
    }

    @Test
    void getCarsWithAscOrderByModel() throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(SORT, "+model")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(6, response.getItems().size());
        assertEquals("model_01", response.getItems().get(0).getModel());
    }

    @Test
    void getCarsWithDescOrderByModel() throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(SORT, "-model")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(6, response.getItems().size());
        assertEquals("model_06", response.getItems().get(0).getModel());
    }

    @ParameterizedTest
    @CsvSource({
            "1, +model, model_01, model_03",
            "2, +model, model_04, model_06",
            "1, -model, model_06, model_04",
            "2, -model, model_03, model_01",})
    void getCarsWithDescOrderByModelOnSpecificPage(int pageNumber, String sortColumn, String resultFirstModel, String resultSecondModel) throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
                .param(PAGE_SIZE, "3")
                .param(SORT, sortColumn)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(3, response.getItems().size());
        assertEquals(resultFirstModel, response.getItems().get(0).getModel());
        assertEquals(resultSecondModel, response.getItems().get(2).getModel());
    }

    @ParameterizedTest
    @CsvSource({
            "+issueYear, +model, model_02",
            "-issueYear, -model, model_01",
            "+issueYear, -model, model_02",
            "-issueYear, +model, model_01",})
    void getCarsWithAscOrderByTwoColumns(String firstSortColumn, String secondSortColumn, String resultFirstModel) throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(PAGE_NUMBER, "1")
                .param(PAGE_SIZE, "50")
                .param(SORT, firstSortColumn, secondSortColumn)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(6, response.getItems().size());
        assertEquals(resultFirstModel, response.getItems().get(0).getModel());
    }

    @Test
    void getCarsPageOne() throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(PAGE_NUMBER, "1")
                .param(PAGE_SIZE, "4")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(4, response.getItems().size());
    }

    @Test
    void getCarsPageTwo() throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(PAGE_NUMBER, "2")
                .param(PAGE_SIZE, "4")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(2, response.getItems().size());
    }

    @ParameterizedTest
    @CsvSource({
            "models, model_01, 1",
            "transmissions, AUTO, 3",
            "drives, FULL, 2",
            "bodies, SEDAN, 5",})
    void getCarsByFilter(String paramName, String paramValue, int recordsCount) throws Exception {
        var mvcResult = mockMvc.perform(get("/cars")
                .param(paramName, paramValue)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCars.class);
        assertEquals(recordsCount, response.getItems().size());
    }

    @Test
    void updateCar() throws Exception {
        ApiModelCarRequest editedCar = getEditedCar();

        UUID uuid = UUID.fromString(CAR_ID);
        var mvcResult = mockMvc.perform(put("/cars/{id}", uuid)
                .content(objectMapper.writeValueAsString(editedCar))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = mvcResult.getResponse();
        var response = objectMapper.readValue(responseBody.getContentAsString(), ApiModelCar.class);
        assertEquals(editedCar.getMake(), response.getMake());
        assertEquals(editedCar.getModel(), response.getModel());
        assertEquals(editedCar.getCapacity(), response.getCapacity());
        assertEquals(editedCar.getVolume(), response.getVolume());
        assertEquals(editedCar.getBody(), response.getBody());
        assertEquals(editedCar.getTransmission(), response.getTransmission());
        assertEquals(editedCar.getDrive(), response.getDrive());
        assertEquals(editedCar.getIssueMonth(), response.getIssueMonth());
        assertEquals(editedCar.getIssueYear(), response.getIssueYear());
    }

    @Test
    void deleteCar() throws Exception {
        var carId = UUID.fromString(CAR_TO_DELETE_ID);
        mockMvc.perform(delete("/cars/{id}", carId)

                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cars/{id}", carId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        assertTrue(true);
    }

    @Test
    void deleteCarNonExisting() throws Exception {
        var carId = UUID.fromString(NON_EXISTING_CAR_ID);
        mockMvc.perform(delete("/cars/{id}", carId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        assertTrue(true);
    }

    @Test
    void deleteCarWithNullCarId() throws Exception {
        UUID carId = null;
        mockMvc.perform(delete("/cars/{id}", carId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
        assertTrue(true);
    }

    private ApiModelCarRequest getMockedCar() {
        return new ApiModelCarRequest()
                .model("mocked_model")
                .make("mocked_make")
                .body(SEDAN)
                .capacity(5)
                .color("blue")
                .volume(5)
                .transmission(ApiModelTransmission.AUTO)
                .issueMonth(1)
                .turbocharger(true)
                .issueYear(1990)
                .drive(ApiModelDrive.FULL);
    }

    private ApiModelCarRequest getEditedCar() {
        ApiModelCarRequest persistedCar = getPersistedCar();
        persistedCar.setCapacity(450);
        persistedCar.setColor("yellow");
        persistedCar.setVolume(600);

        return persistedCar;
    }

    private ApiModelCarRequest getPersistedCar() {
        return new ApiModelCarRequest()
                .make("make_01")
                .model("model_01")
                .body(SEDAN)
                .capacity(200)
                .color("green")
                .volume(100)
                .transmission(ApiModelTransmission.AUTO)
                .issueMonth(2)
                .turbocharger(true)
                .issueYear(2009)
                .drive(ApiModelDrive.FRONT);
    }

}
