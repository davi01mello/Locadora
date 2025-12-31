package br.com.locadora.repositories;

import br.com.locadora.models.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    // 1. Busca para o Gráfico de Top Clientes 🏆
    // Agrupa por cliente, conta e ordena do maior para o menor
    @Query("SELECT l.cliente.nome, COUNT(l) FROM Locacao l GROUP BY l.cliente.nome ORDER BY COUNT(l) DESC")
    List<Object[]> findLocacoesPorCliente();

    // 2. Buscas para o Gráfico de Status 🚦
    long countByDataDevolucaoRealIsNull();      // Em aberto
    long countByDataDevolucaoRealIsNotNull();   // Devolvidos
}