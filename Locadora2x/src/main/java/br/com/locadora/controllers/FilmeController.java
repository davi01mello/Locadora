package br.com.locadora.controllers;

import br.com.locadora.dtos.FilmeDto;
import br.com.locadora.models.Filme;
import br.com.locadora.services.FilmeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
// MUDANÇA AQUI: Adicionei "/api" antes
@RequestMapping("/api/filmes")
public class FilmeController {

    @Autowired
    private FilmeService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<FilmeDto> listarTodos() {
        List<Filme> filmes = service.buscarTodos();
        return filmes.stream()
                .map(filme -> modelMapper.map(filme, FilmeDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public FilmeDto criar(@RequestBody FilmeDto filmeDto) {
        Filme filme = modelMapper.map(filmeDto, Filme.class);
        Filme salvo = service.salvar(filme);
        return modelMapper.map(salvo, FilmeDto.class);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @PutMapping("/{id}")
    public Filme atualizar(@PathVariable Long id, @RequestBody Filme filme) {
        return service.atualizar(id, filme);
    }
}