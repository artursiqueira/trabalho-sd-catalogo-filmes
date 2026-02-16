package br.ufes.catalogo.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import br.ufes.catalogo.model.Ator;
import br.ufes.catalogo.model.Filme;

public class FilmeDTO {
    private Long id;
    private String titulo;
    private String sinopse;
    private LocalDate dataLancamento;
    private Integer duracao;
    private String diretor;
    private Double avaliacao;
    private String posterUrl;
    private Long generoId;
    private Set<Long> atoresIds;

    public FilmeDTO() {
    }

    public FilmeDTO(Filme filme) {
        this.id = filme.getId();
        this.titulo = filme.getTitulo();
        this.sinopse = filme.getSinopse();
        this.dataLancamento = filme.getDataLancamento();
        this.duracao = filme.getDuracao();
        this.diretor = filme.getDiretor();
        this.avaliacao = filme.getAvaliacao();
        this.posterUrl = filme.getPosterUrl();
        if (filme.getGenero() != null) {
            this.generoId = filme.getGenero().getId();
        }
        if (filme.getAtores() != null) {
            this.atoresIds = filme.getAtores().stream()
                    .map(Ator::getId)
                    .collect(Collectors.toSet());
        }
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

    public Long getGeneroId() {
        return generoId;
    }

    public void setGeneroId(Long generoId) {
        this.generoId = generoId;
    }

    public Set<Long> getAtoresIds() {
        return atoresIds;
    }

    public void setAtoresIds(Set<Long> atoresIds) {
        this.atoresIds = atoresIds;
    }
}
