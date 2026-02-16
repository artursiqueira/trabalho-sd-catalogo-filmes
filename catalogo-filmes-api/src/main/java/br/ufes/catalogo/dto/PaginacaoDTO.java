package br.ufes.catalogo.dto;

import java.util.List;

public class PaginacaoDTO<T> {
    private List<T> conteudo;
    private int paginaAtual;
    private int tamanhoPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean primeira;
    private boolean ultima;

    public PaginacaoDTO() {
    }

    public PaginacaoDTO(List<T> conteudo, int paginaAtual, int tamanhoPagina, long totalElementos) {
        this.conteudo = conteudo;
        this.paginaAtual = paginaAtual;
        this.tamanhoPagina = tamanhoPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas = (int) Math.ceil((double) totalElementos / tamanhoPagina);
        this.primeira = paginaAtual == 0;
        this.ultima = paginaAtual == totalPaginas - 1;
    }

    public List<T> getConteudo() {
        return conteudo;
    }

    public void setConteudo(List<T> conteudo) {
        this.conteudo = conteudo;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(int paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    public int getTamanhoPagina() {
        return tamanhoPagina;
    }

    public void setTamanhoPagina(int tamanhoPagina) {
        this.tamanhoPagina = tamanhoPagina;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(long totalElementos) {
        this.totalElementos = totalElementos;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public boolean isPrimeira() {
        return primeira;
    }

    public void setPrimeira(boolean primeira) {
        this.primeira = primeira;
    }

    public boolean isUltima() {
        return ultima;
    }

    public void setUltima(boolean ultima) {
        this.ultima = ultima;
    }
}
