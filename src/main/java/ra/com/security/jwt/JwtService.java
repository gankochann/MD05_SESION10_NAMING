package ra.com.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JwtService {
    private long jwtExpiration = 900000L;
    private String jwtScret = "ganko";

    //tao jwt
    public String generateJwtToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, jwtScret)
                .compact();
    }

    //giai ma jwt

    public String getSujectRromToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtScret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //validation jwt
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtScret).parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            log.error(e.getMessage());
            return false;
        }
    }
}
