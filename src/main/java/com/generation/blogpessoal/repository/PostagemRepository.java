/*essa camada é responsável por implementar as interfaces que contem dados já pré-implementados para a manipulação
 * dos dados
 */
package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagem;


//JPA = Interface que contém os métodos de manipulação da BD
//PostagemRepository = interface que herda os métodos da JPA 
public interface PostagemRepository extends JpaRepository<Postagem, Long> { //extends = foi adicionado a herança //LONG = representa nossa chave primaria que é o atributo ID 

	
	public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo")String titulo);
}
