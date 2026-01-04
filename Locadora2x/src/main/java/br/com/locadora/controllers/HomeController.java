package br.com.locadora.controllers;

import br.com.locadora.repositories.ClienteRepository;
import br.com.locadora.repositories.FilmeRepository;
import br.com.locadora.repositories.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired private FilmeRepository filmeRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private LocacaoRepository locacaoRepository;

    @GetMapping("/")
    public String home(Model model) {
        // --- DADOS DO DASHBOARD (Mantidos intactos) ---

        // 1. Cards do Topo
        model.addAttribute("totalFilmes", filmeRepository.count());
        model.addAttribute("totalClientes", clienteRepository.count());
        model.addAttribute("totalLocacoes", locacaoRepository.count());

        // 2. Gráfico de Categorias
        List<Object[]> dadosCategoria = filmeRepository.findFilmesPorCategoria();
        List<String> nomesCategorias = new ArrayList<>();
        List<Long> qtdFilmes = new ArrayList<>();
        for (Object[] obj : dadosCategoria) {
            nomesCategorias.add((String) obj[0]);
            qtdFilmes.add((Long) obj[1]);
        }
        model.addAttribute("catLabels", nomesCategorias);
        model.addAttribute("catData", qtdFilmes);

        // 3. Gráfico de Top 5 Clientes
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

        // 4. Gráfico de Status (Abertas vs Fechadas)
        long abertas = locacaoRepository.countByDataDevolucaoRealIsNull();
        long fechadas = locacaoRepository.countByDataDevolucaoRealIsNotNull();
        model.addAttribute("statusData", List.of(abertas, fechadas));

        return "home";
    }
}