package br.ufes.catalogo.service;

import br.ufes.catalogo.dto.GeneroDTO;
import br.ufes.catalogo.exception.DadosInvalidosException;
import br.ufes.catalogo.exception.RecursoDuplicadoException;
import br.ufes.catalogo.exception.RecursoNaoEncontradoException;
import br.ufes.catalogo.model.Genero;
import br.ufes.catalogo.repository.GeneroRepository;

import java.util.List;

public class GeneroService {

    private final GeneroRepository generoRepository = new GeneroRepository();

    public GeneroDTO criar(Genero genero) {
        validarGenero(genero);
        
        List<Genero> generosExistentes = generoRepository.listarTodos();
        boolean nomeJaExiste = generosExistentes.stream()
                .anyMatch(g -> g.getNome().equalsIgnoreCase(genero.getNome()));
        
        if (nomeJaExiste) {
            throw new RecursoDuplicadoException("Já existe um gênero com o nome: " + genero.getNome());
        }
        
        Genero generoCriado = generoRepository.salvar(genero);
        return new GeneroDTO(generoCriado);
    }

    public GeneroDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new DadosInvalidosException("ID do gênero inválido");
        }
        
        return generoRepository.buscarPorId(id)
                .map(GeneroDTO::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gênero não encontrado com ID: " + id));
    }

    public List<GeneroDTO> listarTodos() {
        return generoRepository.listarTodos().stream()
                .map(GeneroDTO::new)
                .toList();
    }

    public GeneroDTO atualizar(Long id, Genero genero) {
        if (id == null || id <= 0) {
            throw new DadosInvalidosException("ID do gênero inválido");
        }
        
        validarGenero(genero);
        
        Genero generoExistente = generoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gênero não encontrado com ID: " + id));
        
        List<Genero> generosComMesmoNome = generoRepository.listarTodos();
        boolean nomeJaExiste = generosComMesmoNome.stream()
                .anyMatch(g -> !g.getId().equals(id) && g.getNome().equalsIgnoreCase(genero.getNome()));
        
        if (nomeJaExiste) {
            throw new RecursoDuplicadoException("Já existe outro gênero com o nome: " + genero.getNome());
        }
        
        genero.setId(id);
        Genero generoAtualizado = generoRepository.salvar(genero);
        return new GeneroDTO(generoAtualizado);
    }

    public void deletar(Long id) {
        if (id == null || id <= 0) {
            throw new DadosInvalidosException("ID do gênero inválido");
        }
        
        if (generoRepository.buscarPorId(id).isEmpty()) {
            throw new RecursoNaoEncontradoException("Gênero não encontrado com ID: " + id);
        }
        
        generoRepository.deletar(id);
    }

    private void validarGenero(Genero genero) {
        if (genero == null) {
            throw new DadosInvalidosException("Dados do gênero não podem ser nulos");
        }
        
        if (genero.getNome() == null || genero.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("Nome do gênero é obrigatório");
        }
        
        if (genero.getNome().trim().length() < 2) {
            throw new DadosInvalidosException("Nome do gênero deve ter pelo menos 2 caracteres");
        }
        
        if (genero.getNome().length() > 50) {
            throw new DadosInvalidosException("Nome do gênero não pode exceder 50 caracteres");
        }
        
        if (genero.getDescricao() != null && genero.getDescricao().length() > 500) {
            throw new DadosInvalidosException("Descrição do gênero não pode exceder 500 caracteres");
        }
    }
}
