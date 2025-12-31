package br.com.locadora.controllers;

import br.com.locadora.dtos.FilmeDto;
import br.com.locadora.models.Filme;
import br.com.locadora.repositories.CategoriaRepository;
import br.com.locadora.services.FilmeService;
import br.com.locadora.services.OmdbService; // <--- 1. NOVO IMPORT
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
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    // --- 2. INJEÇÃO DO SERVIÇO DE BUSCA (IA) ---
    @Autowired
    private OmdbService omdbService;
    // -------------------------------------------

    // LISTAR FILMES
    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        if (busca != null && !busca.isEmpty()) {
            model.addAttribute("filmes", service.buscarTodos());
            model.addAttribute("busca", busca);
        } else {
            model.addAttribute("filmes", service.buscarTodos());
            model.addAttribute("busca", "");
        }
        return "filmes";
    }

    // ABRIR FORMULÁRIO
    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("filmeDto", new FilmeDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "formulario";
    }

    // SALVAR FILME
    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute("filmeDto") FilmeDto filmeDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "formulario";
        }

        Filme filme = modelMapper.map(filmeDto, Filme.class);

        if (filmeDto.getId() != null) {
            filme.setId(filmeDto.getId());
        }

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
        FilmeDto filmeDto = modelMapper.map(filme, FilmeDto.class);

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

    // Esse endpoint recebe o título, busca na OMDb e devolve os dados (JSON) para o Javascript
    @GetMapping("/buscar-omdb")
    @ResponseBody
    public FilmeDto buscarDadosOmdb(@RequestParam String titulo) {
        return omdbService.buscarFilmePeloTitulo(titulo);
    }
    // -----------------------------
}