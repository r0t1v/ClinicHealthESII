package dev.clinic.simple.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link dev.clinic.simple.domain.Exame} entity.
 */
public class ExameDTO implements Serializable {

    private Long id;

    private String tipoexame;

    private String valorexame;

    private String descontoconvenio;

    private String nomemmedico;

    @Lob
    private String prerequisito;

    private ZonedDateTime datasolicitacao;

    private ZonedDateTime dataresultado;

    private MedicoDTO nomemedico;

    private ResultadoExameDTO tipoexame;

    private ContaClinicaDTO contaClinica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoexame() {
        return tipoexame;
    }

    public void setTipoexame(String tipoexame) {
        this.tipoexame = tipoexame;
    }

    public String getValorexame() {
        return valorexame;
    }

    public void setValorexame(String valorexame) {
        this.valorexame = valorexame;
    }

    public String getDescontoconvenio() {
        return descontoconvenio;
    }

    public void setDescontoconvenio(String descontoconvenio) {
        this.descontoconvenio = descontoconvenio;
    }

    public String getNomemmedico() {
        return nomemmedico;
    }

    public void setNomemmedico(String nomemmedico) {
        this.nomemmedico = nomemmedico;
    }

    public String getPrerequisito() {
        return prerequisito;
    }

    public void setPrerequisito(String prerequisito) {
        this.prerequisito = prerequisito;
    }

    public ZonedDateTime getDatasolicitacao() {
        return datasolicitacao;
    }

    public void setDatasolicitacao(ZonedDateTime datasolicitacao) {
        this.datasolicitacao = datasolicitacao;
    }

    public ZonedDateTime getDataresultado() {
        return dataresultado;
    }

    public void setDataresultado(ZonedDateTime dataresultado) {
        this.dataresultado = dataresultado;
    }

    public MedicoDTO getNomemedico() {
        return nomemedico;
    }

    public void setNomemedico(MedicoDTO nomemedico) {
        this.nomemedico = nomemedico;
    }

    public ResultadoExameDTO getTipoexame() {
        return tipoexame;
    }

    public void setTipoexame(ResultadoExameDTO tipoexame) {
        this.tipoexame = tipoexame;
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
        if (!(o instanceof ExameDTO)) {
            return false;
        }

        ExameDTO exameDTO = (ExameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExameDTO{" +
            "id=" + getId() +
            ", tipoexame='" + getTipoexame() + "'" +
            ", valorexame='" + getValorexame() + "'" +
            ", descontoconvenio='" + getDescontoconvenio() + "'" +
            ", nomemmedico='" + getNomemmedico() + "'" +
            ", prerequisito='" + getPrerequisito() + "'" +
            ", datasolicitacao='" + getDatasolicitacao() + "'" +
            ", dataresultado='" + getDataresultado() + "'" +
            ", nomemedico=" + getNomemedico() +
            ", tipoexame=" + getTipoexame() +
            ", contaClinica=" + getContaClinica() +
            "}";
    }
}
