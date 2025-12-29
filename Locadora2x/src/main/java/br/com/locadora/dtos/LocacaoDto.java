package br.com.locadora.dtos;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class LocacaoDto {

    private Long id;
    private Long clienteId;
    private Long filmeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDevolucaoPrevista;

    // Construtor vazio
    public LocacaoDto() {
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getFilmeId() {
        return filmeId;
    }

    public void setFilmeId(Long filmeId) {
        this.filmeId = filmeId;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }
}