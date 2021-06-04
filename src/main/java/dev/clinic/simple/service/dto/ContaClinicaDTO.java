package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link dev.clinic.simple.domain.ContaClinica} entity.
 */
public class ContaClinicaDTO implements Serializable {

    private Long id;

    @NotNull
    private String cpf;

    private String senha;

    private PacienteDTO cpf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PacienteDTO getCpf() {
        return cpf;
    }

    public void setCpf(PacienteDTO cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaClinicaDTO)) {
            return false;
        }

        ContaClinicaDTO contaClinicaDTO = (ContaClinicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contaClinicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaClinicaDTO{" +
            "id=" + getId() +
            ", cpf='" + getCpf() + "'" +
            ", senha='" + getSenha() + "'" +
            ", cpf=" + getCpf() +
            "}";
    }
}
