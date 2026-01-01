package br.com.locadora.repositories;

import br.com.locadora.models.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {

    // busca original
    List<Filme> findByTituloContainingIgnoreCaseOrDiretorContainingIgnoreCase(String titulo, String diretor);

    // ---  QUERY PARA O GRÁFICO 📊 ---
    // Isso cria uma lista tipo: [["Ação", 5], ["Comédia", 2]]
    @Query("SELECT f.categoria.nome, COUNT(f) FROM Filme f GROUP BY f.categoria.nome")
    List<Object[]> findFilmesPorCategoria();
}