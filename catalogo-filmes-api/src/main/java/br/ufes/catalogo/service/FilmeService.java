package br.ufes.catalogo.service;

import br.ufes.catalogo.dto.FilmeDTO;
import br.ufes.catalogo.dto.PaginacaoDTO;
import br.ufes.catalogo.exception.RecursoNaoEncontradoException;
import br.ufes.catalogo.model.Filme;
import br.ufes.catalogo.model.Genero;
import br.ufes.catalogo.repository.FilmeRepository;
import br.ufes.catalogo.repository.GeneroRepository;

import java.util.List;

public class FilmeService {

    private final FilmeRepository filmeRepository = new FilmeRepository();
    private final GeneroRepository generoRepository = new GeneroRepository();

    private Genero resolverGenero(FilmeDTO dto) {
        if (dto.getGeneroId() != null) {
            return generoRepository.buscarPorId(dto.getGeneroId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Gênero não encontrado com ID: " + dto.getGeneroId()));
        }

        throw new IllegalArgumentException("Informe generoId");
    }

    public FilmeDTO criar(FilmeDTO dto) {
        Genero genero = resolverGenero(dto);

        Filme filme = new Filme();
        filme.setTitulo(dto.getTitulo());
        filme.setSinopse(dto.getSinopse());
        filme.setDataLancamento(dto.getDataLancamento());
        filme.setDuracao(dto.getDuracao());
        filme.setDiretor(dto.getDiretor());
        filme.setAvaliacao(dto.getAvaliacao());
        filme.setPosterUrl(dto.getPosterUrl());
        filme.setGenero(genero);

        Filme filmeSalvo = filmeRepository.salvar(filme);
        return new FilmeDTO(filmeSalvo);
    }

    public FilmeDTO buscarPorId(Long id) {
        return filmeRepository.buscarPorIdComAtores(id)
                .map(FilmeDTO::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Filme não encontrado com ID: " + id));
    }

    public List<FilmeDTO> listarTodos() {
        return filmeRepository.listarTodosComAtores().stream()
                .map(FilmeDTO::new)
                .toList();
    }

    public PaginacaoDTO<FilmeDTO> listarPaginado(int pagina, int tamanho) {
        List<FilmeDTO> filmes = filmeRepository.listarPaginadoComAtores(pagina, tamanho).stream()
                .map(FilmeDTO::new)
                .toList();

        long total = filmeRepository.contarTotal();

        return new PaginacaoDTO<>(filmes, pagina, tamanho, total);
    }

    public FilmeDTO atualizar(Long id, FilmeDTO dto) {
        Filme filme = filmeRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Filme não encontrado com ID: " + id));

        // Se veio gênero por id, atualiza
        boolean veioGeneroId = dto.getGeneroId() != null;
        if (veioGeneroId) {
            Genero genero = resolverGenero(dto);
            if (filme.getGenero() == null || !genero.getId().equals(filme.getGenero().getId())) {
                filme.setGenero(genero);
            }
        }

        if (dto.getTitulo() != null) filme.setTitulo(dto.getTitulo());
        if (dto.getSinopse() != null) filme.setSinopse(dto.getSinopse());
        if (dto.getDataLancamento() != null) filme.setDataLancamento(dto.getDataLancamento());
        if (dto.getDuracao() != null) filme.setDuracao(dto.getDuracao());
        if (dto.getDiretor() != null) filme.setDiretor(dto.getDiretor());
        if (dto.getAvaliacao() != null) filme.setAvaliacao(dto.getAvaliacao());
        if (dto.getPosterUrl() != null) filme.setPosterUrl(dto.getPosterUrl());

        Filme filmeAtualizado = filmeRepository.salvar(filme);
        return new FilmeDTO(filmeAtualizado);
    }

    public void deletar(Long id) {
        if (filmeRepository.buscarPorId(id).isEmpty()) {
            throw new RecursoNaoEncontradoException("Filme não encontrado com ID: " + id);
        }
        filmeRepository.deletar(id);
    }

    public List<FilmeDTO> buscarPorTitulo(String titulo) {
        return filmeRepository.buscarPorTituloComAtores(titulo).stream()
                .map(FilmeDTO::new)
                .toList();
    }

    public List<FilmeDTO> buscarPorGenero(Long generoId) {
        return filmeRepository.buscarPorGeneroComAtores(generoId).stream()
                .map(FilmeDTO::new)
                .toList();
    }

    public List<FilmeDTO> buscarPorDiretor(String diretor) {
        return filmeRepository.buscarPorDiretorComAtores(diretor).stream()
                .map(FilmeDTO::new)
                .toList();
    }
}
