package dev.clinic.simple.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link dev.clinic.simple.domain.Exame} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.ExameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exames?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExameCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tipoexame;

    private StringFilter valorexame;

    private StringFilter descontoconvenio;

    private StringFilter nomemmedico;

    private ZonedDateTimeFilter datasolicitacao;

    private ZonedDateTimeFilter dataresultado;

    private LongFilter nomemedicoId;

    private LongFilter tipoexameId;

    private LongFilter valorexameId;

    private LongFilter contaClinicaId;

    public ExameCriteria() {}

    public ExameCriteria(ExameCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipoexame = other.tipoexame == null ? null : other.tipoexame.copy();
        this.valorexame = other.valorexame == null ? null : other.valorexame.copy();
        this.descontoconvenio = other.descontoconvenio == null ? null : other.descontoconvenio.copy();
        this.nomemmedico = other.nomemmedico == null ? null : other.nomemmedico.copy();
        this.datasolicitacao = other.datasolicitacao == null ? null : other.datasolicitacao.copy();
        this.dataresultado = other.dataresultado == null ? null : other.dataresultado.copy();
        this.nomemedicoId = other.nomemedicoId == null ? null : other.nomemedicoId.copy();
        this.tipoexameId = other.tipoexameId == null ? null : other.tipoexameId.copy();
        this.valorexameId = other.valorexameId == null ? null : other.valorexameId.copy();
        this.contaClinicaId = other.contaClinicaId == null ? null : other.contaClinicaId.copy();
    }

    @Override
    public ExameCriteria copy() {
        return new ExameCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTipoexame() {
        return tipoexame;
    }

    public StringFilter tipoexame() {
        if (tipoexame == null) {
            tipoexame = new StringFilter();
        }
        return tipoexame;
    }

    public void setTipoexame(StringFilter tipoexame) {
        this.tipoexame = tipoexame;
    }

    public StringFilter getValorexame() {
        return valorexame;
    }

    public StringFilter valorexame() {
        if (valorexame == null) {
            valorexame = new StringFilter();
        }
        return valorexame;
    }

    public void setValorexame(StringFilter valorexame) {
        this.valorexame = valorexame;
    }

    public StringFilter getDescontoconvenio() {
        return descontoconvenio;
    }

    public StringFilter descontoconvenio() {
        if (descontoconvenio == null) {
            descontoconvenio = new StringFilter();
        }
        return descontoconvenio;
    }

    public void setDescontoconvenio(StringFilter descontoconvenio) {
        this.descontoconvenio = descontoconvenio;
    }

    public StringFilter getNomemmedico() {
        return nomemmedico;
    }

    public StringFilter nomemmedico() {
        if (nomemmedico == null) {
            nomemmedico = new StringFilter();
        }
        return nomemmedico;
    }

    public void setNomemmedico(StringFilter nomemmedico) {
        this.nomemmedico = nomemmedico;
    }

    public ZonedDateTimeFilter getDatasolicitacao() {
        return datasolicitacao;
    }

    public ZonedDateTimeFilter datasolicitacao() {
        if (datasolicitacao == null) {
            datasolicitacao = new ZonedDateTimeFilter();
        }
        return datasolicitacao;
    }

    public void setDatasolicitacao(ZonedDateTimeFilter datasolicitacao) {
        this.datasolicitacao = datasolicitacao;
    }

    public ZonedDateTimeFilter getDataresultado() {
        return dataresultado;
    }

    public ZonedDateTimeFilter dataresultado() {
        if (dataresultado == null) {
            dataresultado = new ZonedDateTimeFilter();
        }
        return dataresultado;
    }

    public void setDataresultado(ZonedDateTimeFilter dataresultado) {
        this.dataresultado = dataresultado;
    }

    public LongFilter getNomemedicoId() {
        return nomemedicoId;
    }

    public LongFilter nomemedicoId() {
        if (nomemedicoId == null) {
            nomemedicoId = new LongFilter();
        }
        return nomemedicoId;
    }

    public void setNomemedicoId(LongFilter nomemedicoId) {
        this.nomemedicoId = nomemedicoId;
    }

    public LongFilter getTipoexameId() {
        return tipoexameId;
    }

    public LongFilter tipoexameId() {
        if (tipoexameId == null) {
            tipoexameId = new LongFilter();
        }
        return tipoexameId;
    }

    public void setTipoexameId(LongFilter tipoexameId) {
        this.tipoexameId = tipoexameId;
    }

    public LongFilter getValorexameId() {
        return valorexameId;
    }

    public LongFilter valorexameId() {
        if (valorexameId == null) {
            valorexameId = new LongFilter();
        }
        return valorexameId;
    }

    public void setValorexameId(LongFilter valorexameId) {
        this.valorexameId = valorexameId;
    }

    public LongFilter getContaClinicaId() {
        return contaClinicaId;
    }

    public LongFilter contaClinicaId() {
        if (contaClinicaId == null) {
            contaClinicaId = new LongFilter();
        }
        return contaClinicaId;
    }

    public void setContaClinicaId(LongFilter contaClinicaId) {
        this.contaClinicaId = contaClinicaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExameCriteria that = (ExameCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoexame, that.tipoexame) &&
            Objects.equals(valorexame, that.valorexame) &&
            Objects.equals(descontoconvenio, that.descontoconvenio) &&
            Objects.equals(nomemmedico, that.nomemmedico) &&
            Objects.equals(datasolicitacao, that.datasolicitacao) &&
            Objects.equals(dataresultado, that.dataresultado) &&
            Objects.equals(nomemedicoId, that.nomemedicoId) &&
            Objects.equals(tipoexameId, that.tipoexameId) &&
            Objects.equals(valorexameId, that.valorexameId) &&
            Objects.equals(contaClinicaId, that.contaClinicaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipoexame,
            valorexame,
            descontoconvenio,
            nomemmedico,
            datasolicitacao,
            dataresultado,
            nomemedicoId,
            tipoexameId,
            valorexameId,
            contaClinicaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExameCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipoexame != null ? "tipoexame=" + tipoexame + ", " : "") +
            (valorexame != null ? "valorexame=" + valorexame + ", " : "") +
            (descontoconvenio != null ? "descontoconvenio=" + descontoconvenio + ", " : "") +
            (nomemmedico != null ? "nomemmedico=" + nomemmedico + ", " : "") +
            (datasolicitacao != null ? "datasolicitacao=" + datasolicitacao + ", " : "") +
            (dataresultado != null ? "dataresultado=" + dataresultado + ", " : "") +
            (nomemedicoId != null ? "nomemedicoId=" + nomemedicoId + ", " : "") +
            (tipoexameId != null ? "tipoexameId=" + tipoexameId + ", " : "") +
            (valorexameId != null ? "valorexameId=" + valorexameId + ", " : "") +
            (contaClinicaId != null ? "contaClinicaId=" + contaClinicaId + ", " : "") +
            "}";
    }
}
