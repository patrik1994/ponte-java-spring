package hu.ponte.hr.controller;

import hu.ponte.hr.entity.FileUpload;
import hu.ponte.hr.services.ImageStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImagesControllerTest {

    @Mock
    ImageStore imageStore;

    ImagesController imagesController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        imagesController = new ImagesController(imageStore);
    }

    @Test
    void listImages() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(imagesController).build();

        mockMvc.perform(get("/api/images/meta"))
                .andExpect(status().isOk());
    }

    @Test
    void getImage() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(imagesController).build();

        FileUpload mockedFile = new FileUpload();
        mockedFile.setId(987);
        mockedFile.setFileName("test.jpg");
        mockedFile.setFileType("image/jpeg");
        mockedFile.setSize(1234);
        mockedFile.setFileData(new byte[] {1,2,3,4});

        when(imageStore.previewFile(987)).thenReturn(mockedFile);

        mockMvc.perform(get("/api/images/preview/987"))
                .andExpect(status().isOk());
    }

    @Test
    void getImageNotExist1() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(imagesController).build();

        mockMvc.perform(get("/api/images/preview/123"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getImageNotExist2() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(imagesController).build();

        mockMvc.perform(get("/api/images/preview/test"))
                .andExpect(status().is5xxServerError());
    }
}