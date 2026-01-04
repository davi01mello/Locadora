package br.com.locadora.services;

import br.com.locadora.models.Filme;
import br.com.locadora.repositories.FilmeRepository;
import br.com.locadora.repositories.LocacaoRepository;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocadoraTools {

    @Autowired private FilmeRepository filmeRepository;
    @Autowired private LocacaoRepository locacaoRepository;

    @Tool("Retorna a quantidade total de filmes no acervo e quantos estão alugados.")
    public String contarFilmesELocacoes() {
        long totalFilmes = filmeRepository.count();
        long locacoesAbertas = locacaoRepository.countByDataDevolucaoRealIsNull();
        return String.format("Temos %d filmes no total e %d estão alugados no momento.", totalFilmes, locacoesAbertas);
    }

    @Tool("Verifica se um filme específico tem estoque disponível pelo nome (ou parte do nome).")
    public String verificarEstoqueFilme(String nomeFilme) {
        List<Filme> filmes = filmeRepository.findByTituloContainingIgnoreCaseOrDiretorContainingIgnoreCase(nomeFilme, nomeFilme);

        if (filmes.isEmpty()) return "Não encontrei nenhum filme com esse nome.";

        return filmes.stream()
                .map(f -> String.format("- %s (Estoque: %d)", f.getTitulo(), f.getEstoque()))
                .collect(Collectors.joining("\n"));
    }

    @Tool("Lista os top 3 clientes que mais alugaram filmes na história da locadora.")
    public String listarMelhoresClientes() {
        List<Object[]> topClientes = locacaoRepository.findLocacoesPorCliente();

        if (topClientes.isEmpty()) return "Ainda não temos locações registradas.";

        StringBuilder sb = new StringBuilder("Top Clientes:\n");
        topClientes.stream().limit(3).forEach(obj -> {
            String nome = (String) obj[0];
            Long qtd = (Long) obj[1];
            sb.append(String.format("- %s com %d locações\n", nome, qtd));
        });
        return sb.toString();
    }

    @Tool("Consulta as regras de negócio sobre multas e prazos de devolução.")
    public String consultarPolitica() {
        return """
               POLÍTICA DA LOCADORA:
               1. O prazo de devolução é definido no momento da locação.
               2. A multa por atraso é fixa: R$ 5,00 por dia corrido.
               3. Para alugar, o cliente precisa estar cadastrado com CPF e Endereço.
               """;
    }
}