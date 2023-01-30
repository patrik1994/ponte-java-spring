package hu.ponte.hr.controller;


import hu.ponte.hr.entity.FileUpload;
import hu.ponte.hr.services.ImageStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("api/images")
public class ImagesController {

    private ImageStore imageStore;

    public ImagesController(ImageStore imageStore) {
        this.imageStore = imageStore;
    }

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		List<ImageMeta> images = new ArrayList<>();
        List<FileUpload> uploadedFiles = imageStore.listFiles();

        if (uploadedFiles == null || uploadedFiles.isEmpty()) {
            log.debug("couldn't find any image in the database");
            return null;
        }

        for (FileUpload file : uploadedFiles) {
            ImageMeta image = ImageMeta.builder().id(Integer.toString(file.getId()))
                                                 .name(file.getFileName())
                                                 .mimeType(file.getFileType())
                                                 .size(file.getSize())
                                                 .digitalSign("")
                                                 .build();
            images.add(image);
        }

        return images;
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        if (!id.chars().allMatch(Character::isDigit)) {
           log.error("error while previewing image, invalid id: " + id);
            response.setStatus(500);
            return;
        }
        FileUpload file = imageStore.previewFile(Integer.valueOf(id));
        if (file == null) {
            log.error("file not found with id: " + id);
            response.setStatus(500);
            return;
        }
        response.setContentType(file.getFileType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + file.getFileName());

        OutputStream out = response.getOutputStream();
        byte[] data = file.getFileData();
        for (int i = 0; i < data.length; i++) {
            out.write(data);
        }
        out.close();
	}

}
