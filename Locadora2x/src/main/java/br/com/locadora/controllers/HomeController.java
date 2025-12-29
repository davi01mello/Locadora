package br.com.locadora.controllers;

import br.com.locadora.services.ClienteService;
import br.com.locadora.services.FilmeService;
import br.com.locadora.services.LocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private LocacaoService locacaoService;

    @GetMapping("/")
    public String home(Model model) {
        // Envia os totais para os Cards do Dashboard


        model.addAttribute("totalFilmes", filmeService.buscarTodos().size());
        model.addAttribute("totalClientes", clienteService.buscarTodos().size());
        model.addAttribute("totalLocacoes", locacaoService.buscarTodos().size());

        return "home";
    }
}