package br.ufes.catalogo.dto;

public class TokenDTO {
    private String token;
    private String tipo = "Bearer";
    private Long expiracaoEm;

    public TokenDTO() {
    }

    public TokenDTO(String token, Long expiracaoEm) {
        this.token = token;
        this.expiracaoEm = expiracaoEm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getExpiracaoEm() {
        return expiracaoEm;
    }

    public void setExpiracaoEm(Long expiracaoEm) {
        this.expiracaoEm = expiracaoEm;
    }
}
