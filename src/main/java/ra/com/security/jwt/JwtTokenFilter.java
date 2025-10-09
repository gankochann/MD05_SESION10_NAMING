package ra.com.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ra.com.security.principal.MyUserDetailService;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected MyUserDetailService myUserDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if(token != null && jwtService.validateToken(token)){
                //giai ma lay username tu object
                String username = jwtService.getSujectRromToken(token);

                //tim da userDetail
                UserDetails userDetails = myUserDetailService.loadUserByUsername(username);

                //xac thuc
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());

                //luu vao securityContextHolder

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){}

        filterChain.doFilter(request,response);
    }

    public String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
