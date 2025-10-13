package ra.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.com.dto.MovieDto;
import ra.com.dto.MovieUpdateDto;
import ra.com.model.Movie;
import ra.com.service.MovieService;

import java.util.List;

@RestController
@RequestMapping
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/api/v1/manager/movies")
    public ResponseEntity<List<Movie>> getAll(){
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/api/v1/manager/movies/add")
    public ResponseEntity<?> addMovie(@ModelAttribute MovieDto movieDto) {
        Movie movie = movieService.save(movieDto);
        if(movie != null){
            return new ResponseEntity<>(movie,HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("add false" , HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/v1/manager/movies/edit/{id}")
    public ResponseEntity<?> editMovie(@PathVariable long id, @RequestBody MovieUpdateDto movieUpdateDto) {
        Movie movie = movieService.update(movieUpdateDto,id);
        if(movie != null){
            return new ResponseEntity<>(movie ,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("edit false" , HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/v1/manager/movies/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable long id) {
        movieService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
