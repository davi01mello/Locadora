package br.com.locadora.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Selecione um cliente")
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull(message = "Selecione um filme")
    @ManyToOne
    @JoinColumn(name = "filme_id")
    private Filme filme;

    @NotNull(message = "A data de locação é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataLocacao;

    @NotNull(message = "Defina a data de devolução")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDevolucaoPrevista;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDevolucaoReal; // Quando o cliente realmente devolveu

    private Double valorMulta; // Valor da multa se houver atraso

    public Locacao() {
        this.dataLocacao = LocalDate.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Filme getFilme() { return filme; }
    public void setFilme(Filme filme) { this.filme = filme; }

    public LocalDate getDataLocacao() { return dataLocacao; }
    public void setDataLocacao(LocalDate dataLocacao) { this.dataLocacao = dataLocacao; }

    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }

    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }

    public Double getValorMulta() { return valorMulta; }
    public void setValorMulta(Double valorMulta) { this.valorMulta = valorMulta; }
}