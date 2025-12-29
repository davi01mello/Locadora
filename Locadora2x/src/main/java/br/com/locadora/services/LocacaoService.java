package br.com.locadora.services;

import br.com.locadora.dtos.LocacaoDto;
import br.com.locadora.models.Cliente;
import br.com.locadora.models.Filme;
import br.com.locadora.models.Locacao;
import br.com.locadora.repositories.ClienteRepository;
import br.com.locadora.repositories.FilmeRepository;
import br.com.locadora.repositories.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit; // Importante para calcular dias
import java.util.List;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FilmeRepository filmeRepository;

    public List<Locacao> buscarTodos() {
        return repository.findAll();
    }

    public Locacao cadastrar(LocacaoDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Filme filme = filmeRepository.findById(dto.getFilmeId())
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));

        Locacao locacao = new Locacao();
        locacao.setCliente(cliente);
        locacao.setFilme(filme);
        locacao.setDataLocacao(java.time.LocalDate.now());

        // Garante que a data prevista seja salva
        locacao.setDataDevolucaoPrevista(dto.getDataDevolucaoPrevista());

        return repository.save(locacao);
    }

    //  DEVOLVER E CALCULAR MULTA ---
    public void devolver(Long id) {
        Locacao locacao = repository.findById(id).orElseThrow();

        // 1. Marca que entregou HOJE
        locacao.setDataDevolucaoReal(LocalDate.now());

        // 2. Verifica se houve atraso
        if (locacao.getDataDevolucaoReal().isAfter(locacao.getDataDevolucaoPrevista())) {
            // Calcula dias de diferença
            long diasAtraso = ChronoUnit.DAYS.between(locacao.getDataDevolucaoPrevista(), locacao.getDataDevolucaoReal());

            // Aplica R$ 5,00 por dia de atraso
            locacao.setValorMulta(diasAtraso * 5.0);
        } else {
            // Se entregou no prazo ou antes, multa é zero
            locacao.setValorMulta(0.0);
        }

        repository.save(locacao);
    }
    public Locacao buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Locação não encontrada"));
    }
}