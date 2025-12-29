package br.com.locadora.repositories;

import br.com.locadora.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository; // <--- Importante!
import org.springframework.stereotype.Repository;

@Repository

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}