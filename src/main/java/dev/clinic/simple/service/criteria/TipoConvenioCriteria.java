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

/**
 * Criteria class for the {@link dev.clinic.simple.domain.TipoConvenio} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.TipoConvenioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tipo-convenios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TipoConvenioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter convenio;

    private IntegerFilter codigoconvenio;

    private StringFilter nomeconvenio;

    private StringFilter contato;

    private LongFilter contaClinicaId;

    public TipoConvenioCriteria() {}

    public TipoConvenioCriteria(TipoConvenioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.convenio = other.convenio == null ? null : other.convenio.copy();
        this.codigoconvenio = other.codigoconvenio == null ? null : other.codigoconvenio.copy();
        this.nomeconvenio = other.nomeconvenio == null ? null : other.nomeconvenio.copy();
        this.contato = other.contato == null ? null : other.contato.copy();
        this.contaClinicaId = other.contaClinicaId == null ? null : other.contaClinicaId.copy();
    }

    @Override
    public TipoConvenioCriteria copy() {
        return new TipoConvenioCriteria(this);
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

    public StringFilter getConvenio() {
        return convenio;
    }

    public StringFilter convenio() {
        if (convenio == null) {
            convenio = new StringFilter();
        }
        return convenio;
    }

    public void setConvenio(StringFilter convenio) {
        this.convenio = convenio;
    }

    public IntegerFilter getCodigoconvenio() {
        return codigoconvenio;
    }

    public IntegerFilter codigoconvenio() {
        if (codigoconvenio == null) {
            codigoconvenio = new IntegerFilter();
        }
        return codigoconvenio;
    }

    public void setCodigoconvenio(IntegerFilter codigoconvenio) {
        this.codigoconvenio = codigoconvenio;
    }

    public StringFilter getNomeconvenio() {
        return nomeconvenio;
    }

    public StringFilter nomeconvenio() {
        if (nomeconvenio == null) {
            nomeconvenio = new StringFilter();
        }
        return nomeconvenio;
    }

    public void setNomeconvenio(StringFilter nomeconvenio) {
        this.nomeconvenio = nomeconvenio;
    }

    public StringFilter getContato() {
        return contato;
    }

    public StringFilter contato() {
        if (contato == null) {
            contato = new StringFilter();
        }
        return contato;
    }

    public void setContato(StringFilter contato) {
        this.contato = contato;
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
        final TipoConvenioCriteria that = (TipoConvenioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(convenio, that.convenio) &&
            Objects.equals(codigoconvenio, that.codigoconvenio) &&
            Objects.equals(nomeconvenio, that.nomeconvenio) &&
            Objects.equals(contato, that.contato) &&
            Objects.equals(contaClinicaId, that.contaClinicaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, convenio, codigoconvenio, nomeconvenio, contato, contaClinicaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoConvenioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (convenio != null ? "convenio=" + convenio + ", " : "") +
            (codigoconvenio != null ? "codigoconvenio=" + codigoconvenio + ", " : "") +
            (nomeconvenio != null ? "nomeconvenio=" + nomeconvenio + ", " : "") +
            (contato != null ? "contato=" + contato + ", " : "") +
            (contaClinicaId != null ? "contaClinicaId=" + contaClinicaId + ", " : "") +
            "}";
    }
}
