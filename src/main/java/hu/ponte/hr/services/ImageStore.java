package hu.ponte.hr.services;

import hu.ponte.hr.entity.FileUpload;
import hu.ponte.hr.repository.FileUploadRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageStore {
    private FileUploadRepository fileUploadRepository;

    public ImageStore(FileUploadRepository fileUploadRepository) {
        this.fileUploadRepository = fileUploadRepository;
    }

    public void saveToDb (MultipartFile file) {
        FileUpload image = new FileUpload();
        try {
            image.setFileData(file.getBytes());
            image.setFileType(file.getContentType());
            image.setFileName(file.getOriginalFilename());
            image.setSize(file.getSize());
            fileUploadRepository.save(image);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public FileUpload previewFile (Integer id) {
        return fileUploadRepository.getOne(id);
    }

    public List<FileUpload> listFiles () {
        return fileUploadRepository.findAll();
    }
}
