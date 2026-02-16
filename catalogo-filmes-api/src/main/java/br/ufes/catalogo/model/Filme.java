package br.ufes.catalogo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "filmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "Sinopse é obrigatória")
    @Size(max = 2000, message = "Sinopse não pode exceder 2000 caracteres")
    @Column(name = "sinopse", nullable = false, length = 2000)
    private String sinopse;

    @NotNull(message = "Data de lançamento é obrigatória")
    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @NotNull(message = "Duração é obrigatória")
    @Min(value = 1, message = "Duração deve ser maior que 0")
    @Column(name = "duracao", nullable = false)
    private Integer duracao;

    @NotBlank(message = "Diretor é obrigatório")
    @Size(max = 150, message = "Nome do diretor não pode exceder 150 caracteres")
    @Column(name = "diretor", nullable = false, length = 150)
    private String diretor;

    @DecimalMin(value = "0.0", message = "Avaliação não pode ser negativa")
    @DecimalMax(value = "10.0", message = "Avaliação não pode exceder 10.0")
    @Column(name = "avaliacao")
    private Double avaliacao;

    @Size(max = 500, message = "URL do poster não pode exceder 500 caracteres")
    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genero_id", nullable = false)
    @NotNull(message = "Gênero é obrigatório")
    private Genero genero;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "filme_ator",
        joinColumns = @JoinColumn(name = "filme_id"),
        inverseJoinColumns = @JoinColumn(name = "ator_id")
    )
    private Set<Ator> atores = new HashSet<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDate criadoEm;

    @Column(name = "atualizado_em")
    private LocalDate atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDate.now();
        atualizadoEm = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDate.now();
    }

    public Filme() {
    }

    public Filme(String titulo, String sinopse, LocalDate dataLancamento, Integer duracao, String diretor, Genero genero) {
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.dataLancamento = dataLancamento;
        this.duracao = duracao;
        this.diretor = diretor;
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Set<Ator> getAtores() {
        return atores;
    }

    public void setAtores(Set<Ator> atores) {
        this.atores = atores;
    }

    public void addAtor(Ator ator) {
        this.atores.add(ator);
        ator.getFilmes().add(this);
    }

    public void removeAtor(Ator ator) {
        this.atores.remove(ator);
        ator.getFilmes().remove(this);
    }

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public LocalDate getAtualizadoEm() {
        return atualizadoEm;
    }
}
