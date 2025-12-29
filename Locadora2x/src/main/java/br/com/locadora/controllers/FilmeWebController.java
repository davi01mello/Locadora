package br.com.locadora.controllers;

import br.com.locadora.dtos.FilmeDto;
import br.com.locadora.models.Filme;
import br.com.locadora.repositories.CategoriaRepository; // Importante para o dropdown
import br.com.locadora.services.FilmeService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/filmes")
public class FilmeWebController {

    @Autowired
    private FilmeService service;

    @Autowired
    private CategoriaRepository categoriaRepository; // Precisamos disso para listar as categorias

    @Autowired
    private ModelMapper modelMapper;

    // LISTAR FILMES (Tela do Catálogo)
    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        if (busca != null && !busca.isEmpty()) {
            // Se tiver busca, filtra (precisaria implementar busca no service, por enquanto lista todos)
            model.addAttribute("filmes", service.buscarTodos());
            model.addAttribute("busca", busca);
        } else {
            model.addAttribute("filmes", service.buscarTodos());
            model.addAttribute("busca", "");
        }
        return "filmes"; // Chama o arquivo filmes.html
    }

    // ABRIR FORMULÁRIO DE NOVO FILME
    @GetMapping("/novo")
    public String formularioNovo(Model model) {

        model.addAttribute("filmeDto", new FilmeDto());


        model.addAttribute("categorias", categoriaRepository.findAll());

        return "formulario"; // Chama o arquivo formulario.html
    }

    // SALVAR FILME (Recebe do Formulário)
    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute("filmeDto") FilmeDto filmeDto, BindingResult result, Model model) {
        // Se houver erro de validação (ex: ano menor que 1895)
        if (result.hasErrors()) {
            // Recarrega as categorias para o dropdown não sumir
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "formulario";
        }

        // Converte DTO -> Entidade Filme
        Filme filme = modelMapper.map(filmeDto, Filme.class);

        // Se for edição (tem ID), garante que o ID seja mantido
        if (filmeDto.getId() != null) {
            filme.setId(filmeDto.getId());
        }

        // Se a categoria veio apenas com ID no DTO, o ModelMapper tenta resolver.
        // Mas se der erro, garantimos buscando no banco:
        if (filmeDto.getCategoriaId() != null) {
            filme.setCategoria(categoriaRepository.findById(filmeDto.getCategoriaId()).orElse(null));
        }

        service.salvar(filme);
        return "redirect:/filmes";
    }

    // EDITAR FILME
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Filme filme = service.buscarPorId(id);

        // Converte o Filme do banco para DTO para o formulário entender
        FilmeDto filmeDto = modelMapper.map(filme, FilmeDto.class);

        // Garante que o ID da categoria vá para o DTO
        if (filme.getCategoria() != null) {
            filmeDto.setCategoriaId(filme.getCategoria().getId());
        }

        model.addAttribute("filmeDto", filmeDto);
        model.addAttribute("categorias", categoriaRepository.findAll());

        return "formulario";
    }

    // DELETAR FILME
    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        service.deletar(id);
        return "redirect:/filmes";
    }
}