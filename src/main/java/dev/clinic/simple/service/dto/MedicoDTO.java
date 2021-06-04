package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.clinic.simple.domain.Medico} entity.
 */
public class MedicoDTO implements Serializable {

    private Long id;

    private Integer crm;

    private Integer nome;

    private String graduacao;

    private String atuacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCrm() {
        return crm;
    }

    public void setCrm(Integer crm) {
        this.crm = crm;
    }

    public Integer getNome() {
        return nome;
    }

    public void setNome(Integer nome) {
        this.nome = nome;
    }

    public String getGraduacao() {
        return graduacao;
    }

    public void setGraduacao(String graduacao) {
        this.graduacao = graduacao;
    }

    public String getAtuacao() {
        return atuacao;
    }

    public void setAtuacao(String atuacao) {
        this.atuacao = atuacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicoDTO)) {
            return false;
        }

        MedicoDTO medicoDTO = (MedicoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicoDTO{" +
            "id=" + getId() +
            ", crm=" + getCrm() +
            ", nome=" + getNome() +
            ", graduacao='" + getGraduacao() + "'" +
            ", atuacao='" + getAtuacao() + "'" +
            "}";
    }
}
