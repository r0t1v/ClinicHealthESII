package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.clinic.simple.domain.EnderecoPaciente} entity.
 */
public class EnderecoPacienteDTO implements Serializable {

    private Long id;

    private String cidade;

    private String logradouro;

    private String bairro;

    private Integer numero;

    private String referencia;

    private String cep;

    private PacienteDTO paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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
        if (!(o instanceof EnderecoPacienteDTO)) {
            return false;
        }

        EnderecoPacienteDTO enderecoPacienteDTO = (EnderecoPacienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enderecoPacienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnderecoPacienteDTO{" +
            "id=" + getId() +
            ", cidade='" + getCidade() + "'" +
            ", logradouro='" + getLogradouro() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", numero=" + getNumero() +
            ", referencia='" + getReferencia() + "'" +
            ", cep='" + getCep() + "'" +
            ", paciente=" + getPaciente() +
            "}";
    }
}
