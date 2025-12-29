package br.com.locadora;

import br.com.locadora.models.Categoria;
import br.com.locadora.models.Filme;
import br.com.locadora.repositories.CategoriaRepository;
import br.com.locadora.repositories.FilmeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class LocadoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocadoraApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(FilmeRepository filmeRepository, CategoriaRepository categoriaRepository) {
		return (args) -> {
			// 1. Criar MUITAS Categorias
			List<Categoria> categorias = Arrays.asList(
					new Categoria("Ação"),
					new Categoria("Aventura"),
					new Categoria("Comédia"),
					new Categoria("Drama"),
					new Categoria("Terror"),
					new Categoria("Ficção Científica"),
					new Categoria("Fantasia"),
					new Categoria("Romance"),
					new Categoria("Suspense"),
					new Categoria("Documentário"),
					new Categoria("Animação"),
					new Categoria("Musical"),
					new Categoria("Faroeste"),
					new Categoria("Policial")
			);

			// Salvando todas de uma vez
			categoriaRepository.saveAll(categorias);

			// Pegando algumas categorias para usar nos exemplos (pelo índice da lista)
			Categoria ficcao = categorias.get(5); // Ficção Científica
			Categoria fantasia = categorias.get(6); // Fantasia

			// 2. Criar Filmes
			Filme f1 = new Filme("Matrix", "Lana Wachowski", 1999, ficcao);
			Filme f2 = new Filme("O Senhor dos Anéis", "Peter Jackson", 2001, fantasia);
			Filme f3 = new Filme("Star Wars", "George Lucas", 1977, ficcao);

			filmeRepository.saveAll(Arrays.asList(f1, f2, f3));
		};
	}
}