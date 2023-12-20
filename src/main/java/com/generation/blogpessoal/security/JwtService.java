package com.generation.blogpessoal.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component //INDICA QUE ESSA CLASE ESTÁ SENDO GERENCIADA PELO SPRING E QUE PERMITE INJETAR E INSTACIAR QUALQUER DEPENDENCIA ESPECIFICADA NA IMPLEMENTAÇÃO
public class JwtService {
	
	public static final String SECRET = "acbdc17052cc4386bba2dc0e2c5d729f537d0251ba8186ece83ca896bbe31604";

	private Key getSignKey() { //O MÉTODO KEY... É RESPONSÁVEL POR CODIFICAR A SECRET EM BASE 64 E GERAR A ASSINATURA DO TOKEN, CODIFICADA PELA SENHA QUE VOCÊ CRIOU 
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private Claims extractAllClaims(String token) { //RETORNA AS CLAIMS INSERIDAS MO PAYLOAD = CLAIMS SÃO INFORMAÇÕES DECLARADAS SOBRE O ASSUNTO  
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims:: getSubject);
	}
	
	public Date extractExpirarion(String token) {
		return extractClaim(token, Claims:: getExpiration);
	}
	

	private Boolean isTokenExpired(String token) {
		return extractExpirarion(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
					.setClaims(claims)
					.setSubject(userName)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

}
