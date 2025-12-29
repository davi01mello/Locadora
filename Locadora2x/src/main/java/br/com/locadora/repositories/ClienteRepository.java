package br.com.locadora.repositories;

import br.com.locadora.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Busca clientes cujo nome contenha o texto (ignorando maiúsculas/minúsculas)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}