package hu.ponte.hr.services;

import hu.ponte.hr.entity.FileUpload;
import hu.ponte.hr.repository.FileUploadRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

@Service
public class ImageStore {
    private final FileUploadRepository fileUploadRepository;
    private final SignService signService;

    public ImageStore(FileUploadRepository fileUploadRepository, SignService signService) {
        this.fileUploadRepository = fileUploadRepository;
        this.signService = signService;
    }

    /**
     * Maps the MultiPartFile input to FileUpload instance, then creates the digital signature
     * and saves the FileUpload instance to database.
     * @param file The input file parameter that will be converted and saved.
     */
    public void saveToDb (MultipartFile file) {
        FileUpload image = new FileUpload();
        try {
            byte[] signed = signService.createDigitalSignature(file.getBytes());

            image.setFileData(file.getBytes());
            image.setFileType(file.getContentType());
            image.setFileName(file.getOriginalFilename());
            image.setSize(file.getSize());
            image.setDigitalSignature(signService.encodeBase64(signed));
            fileUploadRepository.save(image);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Finds the file with the given input id in the database and return it.
     * @param id The id of the file to search for
     * @return FileUpload
     */
    public FileUpload previewFile (Integer id) {
        return fileUploadRepository.getOne(id);
    }

    /**
     * Gets all the files from the database.
     * @return List<FileUpload>
     */
    public List<FileUpload> listFiles () {
        return fileUploadRepository.findAll();
    }
}
