package br.com.locadora.services;

import br.com.locadora.models.Filme;
import br.com.locadora.repositories.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmeService {

    @Autowired
    private FilmeRepository repository;

    public List<Filme> buscarTodos() {
        return repository.findAll();
    }

    // Método auxiliar para buscar por ID
    public Filme buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Filme salvar(Filme filme) {
        return repository.save(filme);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }



    public Filme atualizar(Long id, Filme filmeComNovosDados) {
        Filme filmeAntigo = repository.findById(id).orElse(null);

        if (filmeAntigo != null) {
            filmeAntigo.setTitulo(filmeComNovosDados.getTitulo());


            filmeAntigo.setDiretor(filmeComNovosDados.getDiretor());

            filmeAntigo.setAnoLancamento(filmeComNovosDados.getAnoLancamento());


            filmeAntigo.setCategoria(filmeComNovosDados.getCategoria());

            return repository.save(filmeAntigo);
        }
        return null;
    }
    // Método de Busca Inteligente
    public List<Filme> buscar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return repository.findAll(); // Traz tudo se não tiver busca
        }
        // Pesquisa tanto no título quanto no diretor
        return repository.findByTituloContainingIgnoreCaseOrDiretorContainingIgnoreCase(termo, termo);
    }
}