package ra.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.com.model.Movie;

public interface IMovie extends JpaRepository<Movie, Long> {
}
