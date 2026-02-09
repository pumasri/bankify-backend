package seniorproject.bankifycore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final long perMinuteLimit;
    private final String prefix; // "partner" or "atm"

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            String actorId = String.valueOf(auth.getPrincipal());
            String key = "rl:" + prefix + ":" + actorId;

            if (!rateLimitService.allow(key, perMinuteLimit)) {
                response.setStatus(429);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getWriter(), Map.of(
                        "error", "RATE_LIMITED",
                        "message", "Too many requests"
                ));
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
