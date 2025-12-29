package br.com.locadora.repositories;

import br.com.locadora.models.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
    // Podemos adicionar buscas personalizadas no futuro, ex: buscar por cliente
}