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
 * Criteria class for the {@link dev.clinic.simple.domain.ContaClinica} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.ContaClinicaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conta-clinicas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContaClinicaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cpf;

    private StringFilter senha;

    private LongFilter cpfId;

    private LongFilter cpfId;

    private LongFilter cpfId;

    private LongFilter cpfId;

    public ContaClinicaCriteria() {}

    public ContaClinicaCriteria(ContaClinicaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.senha = other.senha == null ? null : other.senha.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
    }

    @Override
    public ContaClinicaCriteria copy() {
        return new ContaClinicaCriteria(this);
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

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getSenha() {
        return senha;
    }

    public StringFilter senha() {
        if (senha == null) {
            senha = new StringFilter();
        }
        return senha;
    }

    public void setSenha(StringFilter senha) {
        this.senha = senha;
    }

    public LongFilter getCpfId() {
        return cpfId;
    }

    public LongFilter cpfId() {
        if (cpfId == null) {
            cpfId = new LongFilter();
        }
        return cpfId;
    }

    public void setCpfId(LongFilter cpfId) {
        this.cpfId = cpfId;
    }

    public LongFilter getCpfId() {
        return cpfId;
    }

    public LongFilter cpfId() {
        if (cpfId == null) {
            cpfId = new LongFilter();
        }
        return cpfId;
    }

    public void setCpfId(LongFilter cpfId) {
        this.cpfId = cpfId;
    }

    public LongFilter getCpfId() {
        return cpfId;
    }

    public LongFilter cpfId() {
        if (cpfId == null) {
            cpfId = new LongFilter();
        }
        return cpfId;
    }

    public void setCpfId(LongFilter cpfId) {
        this.cpfId = cpfId;
    }

    public LongFilter getCpfId() {
        return cpfId;
    }

    public LongFilter cpfId() {
        if (cpfId == null) {
            cpfId = new LongFilter();
        }
        return cpfId;
    }

    public void setCpfId(LongFilter cpfId) {
        this.cpfId = cpfId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContaClinicaCriteria that = (ContaClinicaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(senha, that.senha) &&
            Objects.equals(cpfId, that.cpfId) &&
            Objects.equals(cpfId, that.cpfId) &&
            Objects.equals(cpfId, that.cpfId) &&
            Objects.equals(cpfId, that.cpfId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf, senha, cpfId, cpfId, cpfId, cpfId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaClinicaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (senha != null ? "senha=" + senha + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            "}";
    }
}
