package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.clinic.simple.domain.PagamentoExame} entity.
 */
public class PagamentoExameDTO implements Serializable {

    private Long id;

    private String formapagamento;

    private String conteudo;

    private Boolean seliquidado;

    private ExameDTO exame;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormapagamento() {
        return formapagamento;
    }

    public void setFormapagamento(String formapagamento) {
        this.formapagamento = formapagamento;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Boolean getSeliquidado() {
        return seliquidado;
    }

    public void setSeliquidado(Boolean seliquidado) {
        this.seliquidado = seliquidado;
    }

    public ExameDTO getExame() {
        return exame;
    }

    public void setExame(ExameDTO exame) {
        this.exame = exame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagamentoExameDTO)) {
            return false;
        }

        PagamentoExameDTO pagamentoExameDTO = (PagamentoExameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pagamentoExameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagamentoExameDTO{" +
            "id=" + getId() +
            ", formapagamento='" + getFormapagamento() + "'" +
            ", conteudo='" + getConteudo() + "'" +
            ", seliquidado='" + getSeliquidado() + "'" +
            ", exame=" + getExame() +
            "}";
    }
}
