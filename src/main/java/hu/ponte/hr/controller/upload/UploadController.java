package hu.ponte.hr.controller.upload;

import hu.ponte.hr.exceptions.InvalidFileException;
import hu.ponte.hr.services.ImageStore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Component
@Slf4j
@RequestMapping("api/file")
public class UploadController
{
    private final ImageStore imageStore;

    public UploadController(ImageStore imageStore) {
        this.imageStore = imageStore;
    }

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) throws InvalidFileException {
        if (file == null) {
            log.error("error while uploading file, the file is empty");
            throw (new InvalidFileException("file is empty"));
        } else {
            imageStore.saveToDb(file);
            return "ok";
        }
    }
}
