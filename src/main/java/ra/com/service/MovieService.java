package ra.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ra.com.dto.MovieDto;
import ra.com.dto.MovieUpdateDto;
import ra.com.model.Movie;
import ra.com.repository.IMovie;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private IMovie iMovie;
    @Autowired
    private CloudinaryService cloudinaryService;

    //get

    public List<Movie> findAll(){
        return iMovie.findAll();
    }

    // tim id
    public Movie findById(long id){
        return iMovie.findById(id).orElse(null);
    }

    //post
    public Movie save(MovieDto movieDto) {
        if(movieDto != null || movieDto.getImage().isEmpty()){
            String image = cloudinaryService.uplpad(movieDto.getImage());

            Movie movie= Movie.builder()
                    .title(movieDto.getTitle())
                    .duration(movieDto.getDuration())
                    .genre(movieDto.getGenre())
                    .releaseDate(movieDto.getReleaseDate())
                    .image(image)
                    .build();
            return iMovie.save(movie);
        }else {
            return null;
        }
    }

    //put

    public Movie update(MovieUpdateDto movieDto , long id){
        String updateImage;
        if(movieDto.getImage() != null && !movieDto.getImage().getOriginalFilename().isEmpty()){
            updateImage = cloudinaryService.uplpad(movieDto.getImage());
        }else {
            updateImage = movieDto.getOldImg();
        }

        Movie movie = Movie.builder()
                .title(movieDto.getTitle())
                .duration(movieDto.getDuration())
                .genre(movieDto.getGenre())
                .releaseDate(movieDto.getReleaseDate())
                .image(updateImage)
                .build();

        return iMovie.save(movie);
    }

    //delete
    public String delete(long id){
        Movie movie = findById(id);
        if(movie != null){
            iMovie.delete(movie);
            return "success";
        }else {
            return "error";
        }
    }
}
