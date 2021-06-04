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
 * Criteria class for the {@link dev.clinic.simple.domain.EnderecoPaciente} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.EnderecoPacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /endereco-pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EnderecoPacienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cidade;

    private StringFilter logradouro;

    private StringFilter bairro;

    private IntegerFilter numero;

    private StringFilter referencia;

    private StringFilter cep;

    private LongFilter pacienteId;

    public EnderecoPacienteCriteria() {}

    public EnderecoPacienteCriteria(EnderecoPacienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cidade = other.cidade == null ? null : other.cidade.copy();
        this.logradouro = other.logradouro == null ? null : other.logradouro.copy();
        this.bairro = other.bairro == null ? null : other.bairro.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.referencia = other.referencia == null ? null : other.referencia.copy();
        this.cep = other.cep == null ? null : other.cep.copy();
        this.pacienteId = other.pacienteId == null ? null : other.pacienteId.copy();
    }

    @Override
    public EnderecoPacienteCriteria copy() {
        return new EnderecoPacienteCriteria(this);
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

    public StringFilter getCidade() {
        return cidade;
    }

    public StringFilter cidade() {
        if (cidade == null) {
            cidade = new StringFilter();
        }
        return cidade;
    }

    public void setCidade(StringFilter cidade) {
        this.cidade = cidade;
    }

    public StringFilter getLogradouro() {
        return logradouro;
    }

    public StringFilter logradouro() {
        if (logradouro == null) {
            logradouro = new StringFilter();
        }
        return logradouro;
    }

    public void setLogradouro(StringFilter logradouro) {
        this.logradouro = logradouro;
    }

    public StringFilter getBairro() {
        return bairro;
    }

    public StringFilter bairro() {
        if (bairro == null) {
            bairro = new StringFilter();
        }
        return bairro;
    }

    public void setBairro(StringFilter bairro) {
        this.bairro = bairro;
    }

    public IntegerFilter getNumero() {
        return numero;
    }

    public IntegerFilter numero() {
        if (numero == null) {
            numero = new IntegerFilter();
        }
        return numero;
    }

    public void setNumero(IntegerFilter numero) {
        this.numero = numero;
    }

    public StringFilter getReferencia() {
        return referencia;
    }

    public StringFilter referencia() {
        if (referencia == null) {
            referencia = new StringFilter();
        }
        return referencia;
    }

    public void setReferencia(StringFilter referencia) {
        this.referencia = referencia;
    }

    public StringFilter getCep() {
        return cep;
    }

    public StringFilter cep() {
        if (cep == null) {
            cep = new StringFilter();
        }
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public LongFilter getPacienteId() {
        return pacienteId;
    }

    public LongFilter pacienteId() {
        if (pacienteId == null) {
            pacienteId = new LongFilter();
        }
        return pacienteId;
    }

    public void setPacienteId(LongFilter pacienteId) {
        this.pacienteId = pacienteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EnderecoPacienteCriteria that = (EnderecoPacienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cidade, that.cidade) &&
            Objects.equals(logradouro, that.logradouro) &&
            Objects.equals(bairro, that.bairro) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(referencia, that.referencia) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(pacienteId, that.pacienteId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cidade, logradouro, bairro, numero, referencia, cep, pacienteId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnderecoPacienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cidade != null ? "cidade=" + cidade + ", " : "") +
            (logradouro != null ? "logradouro=" + logradouro + ", " : "") +
            (bairro != null ? "bairro=" + bairro + ", " : "") +
            (numero != null ? "numero=" + numero + ", " : "") +
            (referencia != null ? "referencia=" + referencia + ", " : "") +
            (cep != null ? "cep=" + cep + ", " : "") +
            (pacienteId != null ? "pacienteId=" + pacienteId + ", " : "") +
            "}";
    }
}
