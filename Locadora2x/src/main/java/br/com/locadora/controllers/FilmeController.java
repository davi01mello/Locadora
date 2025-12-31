package br.com.locadora.controllers;

import br.com.locadora.dtos.FilmeDto;
import br.com.locadora.models.Filme;
import br.com.locadora.repositories.CategoriaRepository;
import br.com.locadora.repositories.FilmeRepository;
import br.com.locadora.services.FilmeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Importante: @Controller
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Define que este arquivo controla as telas HTML
@RequestMapping("/filmes") // Define a rota base como /filmes
public class FilmeController {

    @Autowired
    private FilmeRepository filmeRepository; // Usando repository direto para facilitar a busca

    @Autowired
    private FilmeService service; // Usando service para salvar/deletar (regras de negocio)

    @Autowired
    private CategoriaRepository categoriaRepository; // Para o filtro

    // 1. TELA PRINCIPAL (CATÁLOGO + FILTRO) 🎬
    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        // Lógica de busca por texto
        List<Filme> filmes;
        if (busca != null && !busca.isEmpty()) {
            filmes = filmeRepository.findByTituloContainingIgnoreCaseOrDiretorContainingIgnoreCase(busca, busca);
        } else {
            filmes = filmeRepository.findAll();
        }

        // Envia os filmes para a tela
        model.addAttribute("filmes", filmes);

        // Envia as categorias para o Dropdown de Filtro
        model.addAttribute("categorias", categoriaRepository.findAll());

        return "filmes"; // Abre o filmes.html
    }

    // 2. TELA DE NOVO FILME 📝
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("filmeDto", new FilmeDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "formulario"; // Abre o formulario.html
    }

    // 3. SALVAR FILME (POST) 💾
    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute FilmeDto filmeDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "formulario";
        }

        // Converte DTO para Entidade
        Filme filme = new Filme();
        if(filmeDto.getId() != null) filme.setId(filmeDto.getId());

        filme.setTitulo(filmeDto.getTitulo());
        filme.setDiretor(filmeDto.getDiretor());
        filme.setAnoLancamento(filmeDto.getAnoLancamento());
        filme.setSinopse(filmeDto.getSinopse());
        filme.setImagemUrl(filmeDto.getImagemUrl());
        filme.setClassificacao(filmeDto.getClassificacao());
        filme.setEstoque(filmeDto.getEstoque());

        // Pega a categoria pelo ID
        filme.setCategoria(categoriaRepository.findById(filmeDto.getCategoriaId()).orElse(null));

        service.salvar(filme);
        return "redirect:/filmes";
    }

    // 4. TELA DE EDITAR ✏️
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Filme filme = service.buscarPorId(id);

        FilmeDto dto = new FilmeDto();
        dto.setId(filme.getId());
        dto.setTitulo(filme.getTitulo());
        dto.setDiretor(filme.getDiretor());
        dto.setAnoLancamento(filme.getAnoLancamento());
        dto.setEstoque(filme.getEstoque());
        dto.setCategoriaId(filme.getCategoria().getId());
        dto.setSinopse(filme.getSinopse());
        dto.setImagemUrl(filme.getImagemUrl());
        dto.setClassificacao(filme.getClassificacao());

        model.addAttribute("filmeDto", dto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "formulario";
    }

    // 5. DELETAR 🗑️
    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        service.deletar(id);
        return "redirect:/filmes";
    }
}