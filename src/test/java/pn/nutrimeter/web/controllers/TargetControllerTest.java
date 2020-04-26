package pn.nutrimeter.web.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.validation.BindingResult;
import pn.nutrimeter.data.models.enums.Sex;
import pn.nutrimeter.service.services.api.LifeStageGroupService;
import pn.nutrimeter.service.services.api.MacroTargetService;
import pn.nutrimeter.service.services.api.MicroTargetService;
import pn.nutrimeter.web.base.ViewTestBase;
import pn.nutrimeter.web.models.binding.LifeStageGroupBindingModel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TargetControllerTest extends ViewTestBase {

    @MockBean
    LifeStageGroupService lifeStageGroupService;

    @MockBean
    MacroTargetService macroTargetService;

    @MockBean
    MicroTargetService microTargetService;

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAdd_shouldReturnCorrect() throws Exception {
        this.mockMvc.perform(get("/target/group/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("target/life-stage-group-add"));
    }

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAddPost_whenBindingResultHasNoErrors_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(
                        post("/target/group/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("sex", String.valueOf(Sex.FEMALE))
                                .param("lowerAgeBound", "10")
                                .param("upperAgeBound", "20"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithMockUser("test")
    public void lifeStageGroupAddPost_whenBindingResultHasErrors_shouldReturnCorrect() throws Exception {
        this.mockMvc
                .perform(
                        post("/target/group/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("yo", "yo"))
                .andExpect(status().isOk())
                .andExpect(view().name("target/life-stage-group-add"));
    }
}