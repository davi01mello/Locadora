package br.com.locadora.repositories;

import br.com.locadora.models.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {

    // "Encontre por Título contendo (X) OU Diretor contendo (X), ignorando maiúsculas/minúsculas"
    List<Filme> findByTituloContainingIgnoreCaseOrDiretorContainingIgnoreCase(String titulo, String diretor);
}