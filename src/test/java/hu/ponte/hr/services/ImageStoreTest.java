package hu.ponte.hr.services;

import hu.ponte.hr.entity.FileUpload;
import hu.ponte.hr.repository.FileUploadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageStoreTest {

    ImageStore imageStore;

    @Mock
    FileUploadRepository fileUploadRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        imageStore = new ImageStore(fileUploadRepository);
    }

    @Test
    public void previewFile() {
        final String fileName = "test.jpg";
        final String fileType = "image/jpeg";
        final int fileSize = 987;
        final int fileId = 1;

        FileUpload file = new FileUpload();
        file.setId(fileId);
        file.setFileName(fileName);
        file.setSize(fileSize);
        file.setFileType(fileType);

        when(imageStore.previewFile(anyInt())).thenReturn(file);

        FileUpload testFile = imageStore.previewFile(fileId);

        assertEquals(testFile.getId(), fileId);
        assertEquals(testFile.getFileName(), fileName);
        assertEquals(testFile.getSize(), fileSize);
        assertEquals(testFile.getFileType(), fileType);
        verify(fileUploadRepository, times(1)).getOne(anyInt());
    }

    @Test
    public void listFiles() {
        FileUpload file = new FileUpload();
        List<FileUpload> fileList = new ArrayList<>();
        fileList.add(file);

        when(imageStore.listFiles()).thenReturn(fileList);

        List<FileUpload> files = imageStore.listFiles();

        assertEquals(files.size(), 1);
        verify(fileUploadRepository, times(1)).findAll();
    }
}