package br.com.locadora.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FilmeDto {
    private Long id;
    private String titulo;
    private String diretor;
    private Integer anoLancamento;
    private Long categoriaId;
    private String sinopse;
    private String imagemUrl;
    private String classificacao;


    @NotNull(message = "Informe a quantidade")
    @Min(value = 1, message = "O estoque deve ser pelo menos 1")
    private Integer estoque;
    // ------------------

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDiretor() { return diretor; }
    public void setDiretor(String diretor) { this.diretor = diretor; }
    public Integer getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(Integer anoLancamento) { this.anoLancamento = anoLancamento; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
}