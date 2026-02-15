package br.ufes.catalogo.service;

import br.ufes.catalogo.dto.FilmeDTO;
import br.ufes.catalogo.exception.RecursoNaoEncontradoException;
import br.ufes.catalogo.model.Filme;
import br.ufes.catalogo.model.Genero;
import br.ufes.catalogo.repository.FilmeRepository;
import br.ufes.catalogo.repository.GeneroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FilmeServiceTest {

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private GeneroRepository generoRepository;

    private FilmeService filmeService;

    private Genero genero;
    private Filme filme;
    private FilmeDTO filmeDTO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        filmeService = new FilmeService();
        injetarDependencia(filmeService, "filmeRepository", filmeRepository);
        injetarDependencia(filmeService, "generoRepository", generoRepository);

        genero = new Genero("Ação", "Filmes de ação");
        genero.setId(1L);

        filme = new Filme();
        filme.setId(1L);
        filme.setTitulo("Filme Teste");
        filme.setSinopse("Sinopse do filme teste");
        filme.setDataLancamento(LocalDate.of(2024, 1, 1));
        filme.setDuracao(120);
        filme.setDiretor("Diretor Teste");
        filme.setAvaliacao(8.5);
        filme.setGenero(genero);

        filmeDTO = new FilmeDTO();
        filmeDTO.setTitulo("Filme Teste");
        filmeDTO.setSinopse("Sinopse do filme teste");
        filmeDTO.setDataLancamento(LocalDate.of(2024, 1, 1));
        filmeDTO.setDuracao(120);
        filmeDTO.setDiretor("Diretor Teste");
        filmeDTO.setAvaliacao(8.5);
        filmeDTO.setGeneroId(1L);
    }

    private static void injetarDependencia(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void deveCriarFilmeComSucesso() {
        when(generoRepository.buscarPorId(1L)).thenReturn(Optional.of(genero));
        when(filmeRepository.salvar(any(Filme.class))).thenReturn(filme);

        FilmeDTO resultado = filmeService.criar(filmeDTO);

        assertNotNull(resultado);
        assertEquals("Filme Teste", resultado.getTitulo());
        assertEquals(1L, resultado.getGeneroId());
        verify(filmeRepository, times(1)).salvar(any(Filme.class));
    }

    @Test
    void deveLancarExcecaoQuandoGeneroNaoExiste() {
        when(generoRepository.buscarPorId(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> filmeService.criar(filmeDTO));
    }

    @Test
    void deveBuscarFilmePorIdComSucesso() {
        when(filmeRepository.buscarPorIdComAtores(1L)).thenReturn(Optional.of(filme));

        FilmeDTO resultado = filmeService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Filme Teste", resultado.getTitulo());
    }

    @Test
    void deveLancarExcecaoQuandoFilmeNaoEncontrado() {
        when(filmeRepository.buscarPorIdComAtores(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> filmeService.buscarPorId(999L));
    }

    @Test
    void deveDeletarFilmeComSucesso() {
        when(filmeRepository.buscarPorId(1L)).thenReturn(Optional.of(filme));
        doNothing().when(filmeRepository).deletar(1L);

        assertDoesNotThrow(() -> filmeService.deletar(1L));
        verify(filmeRepository, times(1)).deletar(1L);
    }

    @Test
    void deveAtualizarFilmeComSucesso() {
        Long idExistente = 1L;

        when(filmeRepository.buscarPorId(idExistente)).thenReturn(Optional.of(filme));
        when(generoRepository.buscarPorId(1L)).thenReturn(Optional.of(genero));
        when(filmeRepository.salvar(any(Filme.class))).thenReturn(filme);

        filmeDTO.setTitulo("Título Atualizado");
        FilmeDTO resultado = filmeService.atualizar(idExistente, filmeDTO);

        assertNotNull(resultado);
        verify(filmeRepository).buscarPorId(idExistente);
        verify(filmeRepository).salvar(any(Filme.class));
    }

    @Test
    void atualizarSemGeneroIdNaoDeveConsultarGeneroRepository() {
        Long idExistente = 1L;
        when(filmeRepository.buscarPorId(idExistente)).thenReturn(Optional.of(filme));
        when(filmeRepository.salvar(any(Filme.class))).thenReturn(filme);

        FilmeDTO patch = new FilmeDTO();
        patch.setTitulo("Novo");

        FilmeDTO resultado = filmeService.atualizar(idExistente, patch);
        assertNotNull(resultado);

        verify(generoRepository, never()).buscarPorId(anyLong());
        verify(filmeRepository).salvar(any(Filme.class));
    }

    @Test
    void deletarFilmeInexistenteDeveLancar() {
        when(filmeRepository.buscarPorId(999L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> filmeService.deletar(999L));
        verify(filmeRepository, never()).deletar(anyLong());
    }

    @Test
    void listarTodosDeveMapearDtos() {
        when(filmeRepository.listarTodosComAtores()).thenReturn(java.util.List.of(filme));
        assertEquals(1, filmeService.listarTodos().size());
    }

    @Test
    void listarPaginadoDeveRetornarPaginacaoDTO() {
        when(filmeRepository.listarPaginadoComAtores(0, 10)).thenReturn(java.util.List.of(filme));
        when(filmeRepository.contarTotal()).thenReturn(1L);

        var pagina = filmeService.listarPaginado(0, 10);
        assertEquals(1, pagina.getConteudo().size());
        assertEquals(1L, pagina.getTotalElementos());
    }

    @Test
    void buscarPorTituloDeveDelegarRepositorio() {
        when(filmeRepository.buscarPorTituloComAtores("matrix")).thenReturn(java.util.List.of(filme));
        assertEquals(1, filmeService.buscarPorTitulo("matrix").size());
    }

    @Test
    void buscarPorGeneroDeveDelegarRepositorio() {
        when(filmeRepository.buscarPorGeneroComAtores(1L)).thenReturn(java.util.List.of(filme));
        assertEquals(1, filmeService.buscarPorGenero(1L).size());
    }

    @Test
    void buscarPorDiretorDeveDelegarRepositorio() {
        when(filmeRepository.buscarPorDiretorComAtores("nolan")).thenReturn(java.util.List.of(filme));
        assertEquals(1, filmeService.buscarPorDiretor("nolan").size());
    }

    @Test
    void criarSemGeneroIdDeveLancarIllegalArgument() {
        FilmeDTO dto = new FilmeDTO();
        dto.setTitulo("x");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> filmeService.criar(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("generoid"));
    }
}
