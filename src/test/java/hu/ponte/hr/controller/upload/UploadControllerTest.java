package hu.ponte.hr.controller.upload;

import hu.ponte.hr.exceptions.InvalidFileException;
import hu.ponte.hr.services.ImageStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



import static junit.framework.TestCase.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class UploadControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wContext;

    @Mock
    ImageStore imageStore;

    @Mock
    UploadController uploadController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();

        uploadController = new UploadController(imageStore);
    }

    @Test
    void handleFormUpload() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","fileName",
                "img/jpeg", "test data".getBytes());

        doNothing().when(imageStore).saveToDb(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/file/post")
                        .file(mockMultipartFile))
                .andExpect(status().is(200));
    }

    @Test
    void handleFormUploadSizeLimit() throws Exception{
        byte[] bytes = new byte[1024 * 1024 * 2];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","fileName",
                "img/jpeg", bytes);

        doNothing().when(imageStore).saveToDb(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/file/post")
                        .file(mockMultipartFile))
                .andExpect(status().is(200));
    }

    @Test
    void handleFormUploadGreaterFile() {
        byte[] bytes = new byte[1024 * 1024 * 2 + 1];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","fileName",
                "img/jpeg", bytes);

        try {
            uploadController.handleFormUpload(mockMultipartFile);
            fail("File uploading didn't throw invalid file exception for a file greater than 2 MB.");
        } catch (InvalidFileException ex) {}
    }
}