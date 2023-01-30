package hu.ponte.hr;

import hu.ponte.hr.repository.FileUploadRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeniorTestApplicationTests
{
	@Autowired
	private FileUploadRepository fileUploadRepository;

	@Test
	public void contextLoads() {
		assertNotNull(fileUploadRepository);
	}

}

