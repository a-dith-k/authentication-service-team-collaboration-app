package site.adithk.authenticationservice.configurations.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import site.adithk.authenticationservice.services.jwt.JwtService;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;




    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        log.info(authHeader);
        if(authHeader==null)
            log.info("Authorization Header is Null");
        String token=null;
        String username=null;


            if(authHeader!=null&&authHeader.startsWith("Bearer ")){
                token=authHeader.substring(7);

                try{
                    username=jwtService.extractUsername(token);

                }catch (IllegalArgumentException e) {
                    log.info("Illegal Argument while fetching the username from token");
                    log.error("IllegalArgumentException{}",IllegalArgumentException.class);
                } catch (ExpiredJwtException e) {
                    log.info("Given jwt token is expired ");
                    log.error("ExpiredJwtException{}",ExpiredJwtException.class);
                } catch (MalformedJwtException e) {
                    log.info("Token modified | Invalid Token");
                    log.error("MalformedJwtException{}",MalformedJwtException.class);
                } catch (Exception e) {
                    log.error("Exception{}",e.getMessage());

                }



    }

            if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=userDetailsService.loadUserByUsername(username);

                if(jwtService.validateToken(token,userDetails)){
                    log.info("Token status : Token Verified");

                    UsernamePasswordAuthenticationToken authToken
                            =new UsernamePasswordAuthenticationToken(
                                    userDetails.getUsername(),
                            null,
                                    userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }

            filterChain.doFilter(request,response);

    }

}
