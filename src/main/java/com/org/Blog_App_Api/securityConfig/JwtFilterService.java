package com.org.Blog_App_Api.securityConfig;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Blog_App_Api.Util.GenericResponseHandler;
import com.org.Blog_App_Api.service.impl.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterService extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetlImpl detlImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			String header = request.getHeader("Authorization");

			String user = null;
			String token = null;
			if (header != null && header.startsWith("Bearer ")) {
				token = header.substring(7);
				user = jwtService.extractUserName(token);
			}

			if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails loadUserByUsername = detlImpl.loadUserByUsername(user);

				boolean validateToken = jwtService.authenticate(token, loadUserByUsername);

				if (validateToken) {

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							loadUserByUsername, null, loadUserByUsername.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		} catch (Exception e) {
			jwtTokenResponse(response, e);
			return;
		}

		filterChain.doFilter(request, response);

	}

	private void jwtTokenResponse(HttpServletResponse response, Exception e)
			throws JsonProcessingException, IOException {
		response.setContentType("json/text");
		response.setStatus(401);
		Object errResponse = GenericResponseHandler.builder().message(e.getMessage())
				.status(HttpStatus.UNAUTHORIZED.value()).statusCode(HttpStatus.UNAUTHORIZED).build().onlyMessage()
				.getBody();
		response.getWriter().write(new ObjectMapper().writeValueAsString(errResponse));

	}

}
