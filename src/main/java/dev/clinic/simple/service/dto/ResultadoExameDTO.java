package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.clinic.simple.domain.ResultadoExame} entity.
 */
public class ResultadoExameDTO implements Serializable {

    private Long id;

    private String descricao;

    private String prescricao;

    private String indicacao;

    private String contraindicacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public String getIndicacao() {
        return indicacao;
    }

    public void setIndicacao(String indicacao) {
        this.indicacao = indicacao;
    }

    public String getContraindicacao() {
        return contraindicacao;
    }

    public void setContraindicacao(String contraindicacao) {
        this.contraindicacao = contraindicacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultadoExameDTO)) {
            return false;
        }

        ResultadoExameDTO resultadoExameDTO = (ResultadoExameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resultadoExameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultadoExameDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", prescricao='" + getPrescricao() + "'" +
            ", indicacao='" + getIndicacao() + "'" +
            ", contraindicacao='" + getContraindicacao() + "'" +
            "}";
    }
}
