package br.com.locadora.controllers;

import br.com.locadora.dtos.FilmeDto;
import br.com.locadora.models.Filme;
import br.com.locadora.repositories.CategoriaRepository;
import br.com.locadora.repositories.FilmeRepository;
import br.com.locadora.services.FilmeService;
import br.com.locadora.services.OmdbService; // <--- O IMPORT TEM QUE ESTAR AQUI
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/filmes")
public class FilmeController {

    @Autowired private FilmeRepository filmeRepository;
    @Autowired private FilmeService service;
    @Autowired private CategoriaRepository categoriaRepository;

    @Autowired private OmdbService omdbService; // <--- INJEÇÃO DO SERVIÇO

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        List<Filme> filmes = (busca != null && !busca.isEmpty()) ?
                filmeRepository.findByTituloContainingIgnoreCaseOrDiretorContainingIgnoreCase(busca, busca) :
                filmeRepository.findAll();
        model.addAttribute("filmes", filmes);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "filmes";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("filmeDto", new FilmeDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "formulario";
    }

    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute FilmeDto filmeDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "formulario";
        }
        Filme filme = new Filme();
        if(filmeDto.getId() != null) filme.setId(filmeDto.getId());
        filme.setTitulo(filmeDto.getTitulo());
        filme.setDiretor(filmeDto.getDiretor());
        filme.setAnoLancamento(filmeDto.getAnoLancamento());
        filme.setSinopse(filmeDto.getSinopse());
        filme.setImagemUrl(filmeDto.getImagemUrl());
        filme.setClassificacao(filmeDto.getClassificacao());
        filme.setEstoque(filmeDto.getEstoque());
        filme.setCategoria(categoriaRepository.findById(filmeDto.getCategoriaId()).orElse(null));
        service.salvar(filme);
        return "redirect:/filmes";
    }

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

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        service.deletar(id);
        return "redirect:/filmes";
    }

    // --- ROTA DA IA (COM LOG DE ERRO) ---
    @GetMapping("/buscar-omdb")
    @ResponseBody
    public FilmeDto buscarOmdb(@RequestParam String titulo) {
        System.out.println("Recebendo pedido de busca para: " + titulo);
        try {
            FilmeDto resultado = omdbService.buscarFilme(titulo);
            if (resultado == null) {
                System.out.println("Filme não encontrado na API.");
            } else {
                System.out.println("Filme encontrado: " + resultado.getTitulo());
            }
            return resultado;
        } catch (Exception e) {
            System.out.println("ERRO GRAVE NO CONTROLLER AO BUSCAR FILME:");
            e.printStackTrace();
            return null;
        }
    }
}