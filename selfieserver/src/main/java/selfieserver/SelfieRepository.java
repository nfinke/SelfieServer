package selfieserver;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SelfieRepository extends CrudRepository<Selfie, Long> {

	List<Selfie> findByFilename(@Param("filename") String filename);
	
}
