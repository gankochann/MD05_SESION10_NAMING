package ra.com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovieDto {
    @NotBlank
    private String title;
    @NotNull
    private int duration;
    @NotBlank
    private String genre;
    @NotNull
    @Pattern(regexp = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NotNull
    private MultipartFile image;
}
