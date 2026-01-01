package br.com.locadora.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode ficar em branco")
    private String titulo;

    @NotBlank(message = "O diretor é obrigatório")
    private String diretor;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1895, message = "O ano deve ser maior que 1895")
    private Integer anoLancamento;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @NotNull(message = "Você deve selecionar uma categoria")
    private Categoria categoria;

    // --- CAMPOS IA ---
    @Column(length = 4000)
    private String sinopse;
    private String imagemUrl;
    private String classificacao;

    // --- NOVO CAMPO: ESTOQUE 📦 ---
    @NotNull(message = "Informe a quantidade em estoque")
    @Min(value = 0, message = "O estoque não pode ser negativo")
    private Integer estoque;
    // ------------------------------

    public Filme() {}

    public Filme(String titulo, String diretor, Integer anoLancamento, Categoria categoria, Integer estoque) {
        this.titulo = titulo;
        this.diretor = diretor;
        this.anoLancamento = anoLancamento;
        this.categoria = categoria;
        this.estoque = estoque;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDiretor() { return diretor; }
    public void setDiretor(String diretor) { this.diretor = diretor; }
    public Integer getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(Integer anoLancamento) { this.anoLancamento = anoLancamento; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    // Getter e Setter do Estoque
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
}