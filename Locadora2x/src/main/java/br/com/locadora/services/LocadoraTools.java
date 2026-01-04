package br.com.locadora.services;

import br.com.locadora.models.Filme;
import br.com.locadora.repositories.ClienteRepository;
import br.com.locadora.repositories.FilmeRepository;
import br.com.locadora.repositories.LocacaoRepository;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LocadoraTools {

    @Autowired private FilmeRepository filmeRepository;
    @Autowired private LocacaoRepository locacaoRepository;
    @Autowired private ClienteRepository clienteRepository; // Adicionado

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

    // --- NOVA FERRAMENTA DE INSIGHTS 🧠 ---
    @Tool("Analisa todos os dados do Dashboard (Categorias, Clientes, Pendências) para gerar insights estratégicos de negócio.")
    public String analisarDashboardCompleto() {
        // 1. Dados Gerais
        long totalFilmes = filmeRepository.count();
        long totalClientes = clienteRepository.count();
        long locacoesAbertas = locacaoRepository.countByDataDevolucaoRealIsNull();
        long locacoesFechadas = locacaoRepository.countByDataDevolucaoRealIsNotNull();

        // 2. Categorias (Para ver o que o público gosta)
        List<Object[]> dadosCategoria = filmeRepository.findFilmesPorCategoria();
        String resumoCategorias = dadosCategoria.stream()
                .map(obj -> obj[0] + ": " + obj[1] + " filmes")
                .collect(Collectors.joining(", "));

        // 3. Top Clientes (Para identificar VIPs)
        List<Object[]> topClientes = locacaoRepository.findLocacoesPorCliente();
        String resumoClientes = topClientes.stream().limit(5)
                .map(obj -> obj[0] + " (" + obj[1] + ")")
                .collect(Collectors.joining(", "));

        // Monta o relatório para o Gemini ler
        return String.format("""
                === RELATÓRIO DE DADOS DA LOCADORA ===
                Totais: %d Filmes, %d Clientes Cadastrados.
                Status Locações: %d Em Aberto (Pendentes), %d Devolvidas (Finalizadas).
                Distribuição do Acervo por Categoria: [%s].
                Top 5 Clientes Ativos: [%s].
                """,
                totalFilmes, totalClientes, locacoesAbertas, locacoesFechadas, resumoCategorias, resumoClientes);
    }
    // ... outros métodos ...

    @Tool("Busca detalhes de uma locação específica pelo ID para gerar cobranças ou tirar dúvidas.")
    public String buscarDetalhesLocacao(Long idLocacao) {
        var locacaoOpt = locacaoRepository.findById(idLocacao);

        if (locacaoOpt.isEmpty()) return "Locação não encontrada com o ID " + idLocacao;

        var locacao = locacaoOpt.get();

        // Calcula atraso em tempo real (pois se não devolveu, não tem dataDevolucaoReal)
        long diasAtraso = 0;
        if (locacao.getDataDevolucaoReal() == null && java.time.LocalDate.now().isAfter(locacao.getDataDevolucaoPrevista())) {
            diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(locacao.getDataDevolucaoPrevista(), java.time.LocalDate.now());
        }

        double multaEstimada = diasAtraso * 5.0; // R$ 5,00 por dia
        String status = (diasAtraso > 0) ? "EM ATRASO (" + diasAtraso + " dias)" : "EM DIA";

        return String.format("""
                DADOS DA LOCAÇÃO #%d:
                Cliente: %s (Telefone: %s)
                Filme: %s
                Data Prevista: %s
                Status Atual: %s
                Multa Acumulada: R$ %.2f
                """,
                locacao.getId(),
                locacao.getCliente().getNome(),
                locacao.getCliente().getTelefone(),
                locacao.getFilme().getTitulo(),
                locacao.getDataDevolucaoPrevista(),
                status,
                multaEstimada);
    }
}