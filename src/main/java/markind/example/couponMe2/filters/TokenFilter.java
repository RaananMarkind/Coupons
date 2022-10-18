package markind.example.couponMe2.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
            String token = request.getHeader("authorization").replace("Bearer ", "");
            String email = JWT.decode(token).getClaim("email").asString();
            String password = JWT.decode(token).getClaim("password").asString();
            String typeUser = JWT.decode(token).getClaim("typeUser").asString();
            filterChain.doFilter(request, response); // מעביר לקונטרולר
        }catch (Exception e){
            response.setStatus(401);
            response.getWriter().println("Invalid credentials!");
        }
	}
	
	@Override
	protected boolean shouldNotFilter (HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/home") || path.startsWith("/auth");
	}
}
