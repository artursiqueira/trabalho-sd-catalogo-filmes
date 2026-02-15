package br.ufes.catalogo.dto;

import br.ufes.catalogo.model.Genero;

/**
 * DTO enxuto para evitar LazyInitializationException ao serializar a entidade Genero.
 * Não expõe a coleção "filmes".
 */
public class GeneroDTO {
    private Long id;
    private String nome;
    private String descricao;

    public GeneroDTO() {
    }

    public GeneroDTO(Genero genero) {
        if (genero == null) {
            return;
        }
        this.id = genero.getId();
        this.nome = genero.getNome();
        this.descricao = genero.getDescricao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
