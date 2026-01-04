package br.com.locadora.controllers;

import br.com.locadora.repositories.ClienteRepository;
import br.com.locadora.repositories.FilmeRepository;
import br.com.locadora.repositories.LocacaoRepository;
import br.com.locadora.services.AnaliseService; // Importa nossa nova classe
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired private FilmeRepository filmeRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private LocacaoRepository locacaoRepository;

    @Autowired private AnaliseService analiseService; // Injeção continua igual

    @GetMapping("/")
    public String home(Model model) {
        // ... (MANTENHA O CÓDIGO DOS GRÁFICOS QUE JÁ ESTAVA AQUI) ...
        // Vou resumir para caber na resposta, mas NÃO delete o que já funcionava!

        model.addAttribute("totalFilmes", filmeRepository.count());
        model.addAttribute("totalClientes", clienteRepository.count());
        model.addAttribute("totalLocacoes", locacaoRepository.count());

        // ... (Código dos gráficos de categoria e clientes) ...
        List<Object[]> dadosCategoria = filmeRepository.findFilmesPorCategoria();
        List<String> nomesCategorias = new ArrayList<>();
        List<Long> qtdFilmes = new ArrayList<>();
        for (Object[] obj : dadosCategoria) {
            nomesCategorias.add((String) obj[0]);
            qtdFilmes.add((Long) obj[1]);
        }
        model.addAttribute("catLabels", nomesCategorias);
        model.addAttribute("catData", qtdFilmes);

        List<Object[]> todosClientes = locacaoRepository.findLocacoesPorCliente();
        List<Object[]> top5Clientes = todosClientes.stream().limit(5).collect(Collectors.toList());
        List<String> nomesClientes = new ArrayList<>();
        List<Long> qtdLocacoes = new ArrayList<>();
        for (Object[] obj : top5Clientes) {
            nomesClientes.add((String) obj[0]);
            qtdLocacoes.add((Long) obj[1]);
        }
        model.addAttribute("cliLabels", nomesClientes);
        model.addAttribute("cliData", qtdLocacoes);

        long abertas = locacaoRepository.countByDataDevolucaoRealIsNull();
        long fechadas = locacaoRepository.countByDataDevolucaoRealIsNotNull();
        model.addAttribute("statusData", List.of(abertas, fechadas));

        return "home";
    }

    // --- ROTA DA IA (AJUSTADA) ---
    @GetMapping("/api/analise-ia")
    @ResponseBody
    public String gerarAnalise() {
        // 1. Coleta os dados
        long totalFilmes = filmeRepository.count();
        long locacoesAbertas = locacaoRepository.countByDataDevolucaoRealIsNull();
        List<Object[]> topCategorias = filmeRepository.findFilmesPorCategoria();

        // 2. Monta texto
        StringBuilder resumo = new StringBuilder();
        resumo.append("Total Filmes: ").append(totalFilmes).append(". ");
        resumo.append("Locações Pendentes: ").append(locacoesAbertas).append(". ");
        resumo.append("Categorias: ");
        for (Object[] cat : topCategorias) {
            resumo.append(cat[0]).append(" (").append(cat[1]).append("), ");
        }

        // 3. Chama o serviço manual
        return analiseService.analisarDados(resumo.toString());
    }
}