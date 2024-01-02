package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //Indica que é uma classe teste e o environment indica que se caso a porta 8080 estja ocupada, o spring irá atribuir a outra porta automaticamente 
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //indica que o ciclo da vida da classe teste será por classe
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTempalte; //enviar as requisições para a aplicação
	
	@Autowired
	private UsuarioService usuarioService; //para persistir os objetos no Banco de dados de testes com a senha criptografada.
	
	@Autowired
	private UsuarioRepository usuarioRepository; //para limpar o banco de dados de testes
	
	@BeforeAll //apaga todos os dados da tabela e cria ouro usuario para testar os métodos protwgidos por autenticação
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(
				0L, "Root", "root@root.com", "rootroot", "12345678910" ));
	}
	
	@Test
	@DisplayName("Cadastrar um usuário") //configurando ammensagem que será exibida
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario( //Essa parte do código equivale ao post do insomnia
				0L, "Vitória França", "vitoria.sousa@gmail.com", "12345678910", "-"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTempalte.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Não deve permitir a duplicação de um usuário")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Marida de Jesus", "maria.jesus@gmail.com.br", "69874512310", "-"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario( //Essa parte do código equivale ao post do insomnia
				0L, "Maria de Jesus", "maria.jesus@gmail.com.br", "69874512310", "-"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTempalte.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "-"));

		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123" , "-");
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
			.withBasicAuth("vitoria.sousa@gmail.com", "12345678910")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		
	}

	@Test
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {

		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "-"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "-"));

		ResponseEntity<String> resposta = testRestTemplate
		.withBasicAuth("root@root.com", "rootroot")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}

}
