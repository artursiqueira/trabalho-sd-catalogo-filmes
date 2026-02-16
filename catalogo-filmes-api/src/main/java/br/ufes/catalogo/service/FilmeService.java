package br.ufes.catalogo.service;

import br.ufes.catalogo.dto.FilmeDTO;
import br.ufes.catalogo.dto.PaginacaoDTO;
import br.ufes.catalogo.exception.DadosInvalidosException;
import br.ufes.catalogo.exception.RecursoNaoEncontradoException;
import br.ufes.catalogo.model.Filme;
import br.ufes.catalogo.model.Genero;
import br.ufes.catalogo.repository.FilmeRepository;
import br.ufes.catalogo.repository.GeneroRepository;

import java.time.LocalDate;
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

        throw new DadosInvalidosException("Informe o ID do gênero do filme");
    }

    public FilmeDTO criar(FilmeDTO dto) {
        validarFilmeDTO(dto);
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
        if (id == null || id <= 0) {
            throw new DadosInvalidosException("ID do filme inválido");
        }
        
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
        if (pagina < 0) {
            throw new DadosInvalidosException("Número da página não pode ser negativo");
        }
        
        if (tamanho <= 0 || tamanho > 100) {
            throw new DadosInvalidosException("Tamanho da página deve ser entre 1 e 100");
        }
        
        List<FilmeDTO> filmes = filmeRepository.listarPaginadoComAtores(pagina, tamanho).stream()
                .map(FilmeDTO::new)
                .toList();

        long total = filmeRepository.contarTotal();

        return new PaginacaoDTO<>(filmes, pagina, tamanho, total);
    }

    public FilmeDTO atualizar(Long id, FilmeDTO dto) {
        if (id == null || id <= 0) {
            throw new DadosInvalidosException("ID do filme inválido");
        }
        
        if (dto == null) {
            throw new DadosInvalidosException("Dados do filme não podem ser nulos");
        }
        
        Filme filme = filmeRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Filme não encontrado com ID: " + id));

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
        if (id == null || id <= 0) {
            throw new DadosInvalidosException("ID do filme inválido");
        }
        
        if (filmeRepository.buscarPorId(id).isEmpty()) {
            throw new RecursoNaoEncontradoException("Filme não encontrado com ID: " + id);
        }
        filmeRepository.deletar(id);
    }

    public List<FilmeDTO> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new DadosInvalidosException("Título para busca não pode ser vazio");
        }
        
        return filmeRepository.buscarPorTituloComAtores(titulo).stream()
                .map(FilmeDTO::new)
                .toList();
    }

    public List<FilmeDTO> buscarPorGenero(Long generoId) {
        if (generoId == null || generoId <= 0) {
            throw new DadosInvalidosException("ID do gênero inválido");
        }
        
        return filmeRepository.buscarPorGeneroComAtores(generoId).stream()
                .map(FilmeDTO::new)
                .toList();
    }

    public List<FilmeDTO> buscarPorDiretor(String diretor) {
        if (diretor == null || diretor.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome do diretor para busca não pode ser vazio");
        }
        
        return filmeRepository.buscarPorDiretorComAtores(diretor).stream()
                .map(FilmeDTO::new)
                .toList();
    }

    private void validarFilmeDTO(FilmeDTO dto) {
        if (dto == null) {
            throw new DadosInvalidosException("Dados do filme não podem ser nulos");
        }
        
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new DadosInvalidosException("Título do filme é obrigatório");
        }
        
        if (dto.getTitulo().length() > 200) {
            throw new DadosInvalidosException("Título do filme não pode exceder 200 caracteres");
        }
        
        if (dto.getSinopse() == null || dto.getSinopse().trim().isEmpty()) {
            throw new DadosInvalidosException("Sinopse do filme é obrigatória");
        }
        
        if (dto.getSinopse().length() > 2000) {
            throw new DadosInvalidosException("Sinopse do filme não pode exceder 2000 caracteres");
        }
        
        if (dto.getDataLancamento() == null) {
            throw new DadosInvalidosException("Data de lançamento é obrigatória");
        }
        
        if (dto.getDataLancamento().isAfter(LocalDate.now().plusYears(5))) {
            throw new DadosInvalidosException("Data de lançamento não pode ser maior que 5 anos no futuro");
        }
        
        if (dto.getDuracao() == null || dto.getDuracao() <= 0) {
            throw new DadosInvalidosException("Duração do filme deve ser maior que 0");
        }
        
        if (dto.getDuracao() > 600) {
            throw new DadosInvalidosException("Duração do filme não pode exceder 600 minutos (10 horas)");
        }
        
        if (dto.getDiretor() == null || dto.getDiretor().trim().isEmpty()) {
            throw new DadosInvalidosException("Nome do diretor é obrigatório");
        }
        
        if (dto.getDiretor().length() > 150) {
            throw new DadosInvalidosException("Nome do diretor não pode exceder 150 caracteres");
        }
        
        if (dto.getAvaliacao() != null && (dto.getAvaliacao() < 0.0 || dto.getAvaliacao() > 10.0)) {
            throw new DadosInvalidosException("Avaliação deve estar entre 0.0 e 10.0");
        }
        
        if (dto.getPosterUrl() != null && dto.getPosterUrl().length() > 500) {
            throw new DadosInvalidosException("URL do poster não pode exceder 500 caracteres");
        }
    }
}
