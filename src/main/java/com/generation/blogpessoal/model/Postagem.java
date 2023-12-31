/*Model: essa camada é reponsável pela abstração dos objetos que estão registrados nas tabelas, que seram gerada no banco de dados 
 * as classes criada nessa camada representam os objetos que estão no banco de dados
 */
package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //criando uma tabela
@Table(name = "tb_postagens") //está nomenado a tabela 
public class Postagem {
	
	@Id //primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Auto incremento 
	private Long id;
		
	@NotBlank(message = "O atributo título é Obrigatório!") // tradução literal não pode haver espaços vazios  // Not null = para números / Not Blank = para texto 
	@Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 05 e no máximo 100 caracteres") // Size define o tamanho mínimo e o tamanho máximo de caracteres
	@Column(length = 100) //sobrescreve a quantidade padrão de caracteres máximo 
	private String titulo;
		
	@NotBlank(message = "O atributo texto é Obrigatório!") //não permite que o atributo seja nulo ou que tenha espaços em branco. Já o notnull permitiria os espaços em branco 
	@Column(length = 1000)
	@Size(min = 10, max = 1000, message = "O atributo texto deve conter no mínimo 10 e no máximo 1000 caracteres")//para definir o min e max de caracteres que um usuario pode escrever no atributo
	private String texto;
		
	@UpdateTimestamp //a data será gerada pelo banco de dados 
	private LocalDateTime data;
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Tema tema;
	
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
	
	
	
	//	@autor é uma anotação do Javadoc, o javadoc ajuda a fazer a documentação de uma api através dos comentários.
	//JPA => Todos os métodos para manipular o BD 
	
}
