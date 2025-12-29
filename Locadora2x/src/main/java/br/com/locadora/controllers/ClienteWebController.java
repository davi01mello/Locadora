package br.com.locadora.controllers;

import br.com.locadora.models.Cliente;
import br.com.locadora.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes") // Prefixo para todas as URLs de cliente
public class ClienteWebController {

    @Autowired
    private ClienteService service;

    // Listar
    @GetMapping
    public String listarClientes(@RequestParam(name = "busca", required = false) String busca, Model model) {
        model.addAttribute("clientes", service.buscarPorNome(busca));
        return "clientes"; // Nome do arquivo HTML
    }

    // Formulário Novo
    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "formulario-cliente"; // Nome do arquivo HTML
    }

    // Salvar
    @PostMapping("/novo")
    public String salvarCliente(@Valid Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return "formulario-cliente";
        }
        service.salvar(cliente);
        return "redirect:/clientes";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", service.buscarPorId(id));
        return "formulario-cliente";
    }

    // Deletar
    @GetMapping("/deletar/{id}")
    public String deletarCliente(@PathVariable Long id) {
        service.deletar(id);
        return "redirect:/clientes";
    }
}