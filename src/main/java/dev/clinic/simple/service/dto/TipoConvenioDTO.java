package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.clinic.simple.domain.TipoConvenio} entity.
 */
public class TipoConvenioDTO implements Serializable {

    private Long id;

    private String convenio;

    private Integer codigoconvenio;

    private String nomeconvenio;

    private String contato;

    private ContaClinicaDTO contaClinica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Integer getCodigoconvenio() {
        return codigoconvenio;
    }

    public void setCodigoconvenio(Integer codigoconvenio) {
        this.codigoconvenio = codigoconvenio;
    }

    public String getNomeconvenio() {
        return nomeconvenio;
    }

    public void setNomeconvenio(String nomeconvenio) {
        this.nomeconvenio = nomeconvenio;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public ContaClinicaDTO getContaClinica() {
        return contaClinica;
    }

    public void setContaClinica(ContaClinicaDTO contaClinica) {
        this.contaClinica = contaClinica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoConvenioDTO)) {
            return false;
        }

        TipoConvenioDTO tipoConvenioDTO = (TipoConvenioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoConvenioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoConvenioDTO{" +
            "id=" + getId() +
            ", convenio='" + getConvenio() + "'" +
            ", codigoconvenio=" + getCodigoconvenio() +
            ", nomeconvenio='" + getNomeconvenio() + "'" +
            ", contato='" + getContato() + "'" +
            ", contaClinica=" + getContaClinica() +
            "}";
    }
}
