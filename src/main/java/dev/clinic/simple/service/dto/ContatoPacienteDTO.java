package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.clinic.simple.domain.ContatoPaciente} entity.
 */
public class ContatoPacienteDTO implements Serializable {

    private Long id;

    private String tipo;

    private String conteudo;

    private PacienteDTO paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContatoPacienteDTO)) {
            return false;
        }

        ContatoPacienteDTO contatoPacienteDTO = (ContatoPacienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contatoPacienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContatoPacienteDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", conteudo='" + getConteudo() + "'" +
            ", paciente=" + getPaciente() +
            "}";
    }
}
