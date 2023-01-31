package hu.ponte.hr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String fileName;
    private String fileType;
    private long size;

    @Lob
    private byte[] digitalSignature;

    @Lob
    private byte[] fileData;

}
