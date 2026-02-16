package br.ufes.catalogo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "generos")
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do gênero é obrigatório")
    @Size(min = 2, max = 50, message = "Nome do gênero deve ter entre 2 e 50 caracteres")
    @Column(name = "nome", nullable = false, unique = true, length = 50)
    private String nome;

    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    @Column(name = "descricao", length = 500)
    private String descricao;

    @OneToMany(mappedBy = "genero", fetch = FetchType.LAZY)
    private Set<Filme> filmes = new HashSet<>();

    public Genero() {
    }

    public Genero(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
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

    public Set<Filme> getFilmes() {
        return filmes;
    }

    public void setFilmes(Set<Filme> filmes) {
        this.filmes = filmes;
    }
}
