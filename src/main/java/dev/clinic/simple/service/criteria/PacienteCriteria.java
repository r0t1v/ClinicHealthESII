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
 * Criteria class for the {@link dev.clinic.simple.domain.Paciente} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.PacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PacienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cpf;

    private StringFilter nome;

    private StringFilter idade;

    private LongFilter cpfId;

    private LongFilter cpfId;

    private LongFilter cpfId;

    private LongFilter cpfId;

    public PacienteCriteria() {}

    public PacienteCriteria(PacienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.idade = other.idade == null ? null : other.idade.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
        this.cpfId = other.cpfId == null ? null : other.cpfId.copy();
    }

    @Override
    public PacienteCriteria copy() {
        return new PacienteCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getIdade() {
        return idade;
    }

    public StringFilter idade() {
        if (idade == null) {
            idade = new StringFilter();
        }
        return idade;
    }

    public void setIdade(StringFilter idade) {
        this.idade = idade;
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
        final PacienteCriteria that = (PacienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(idade, that.idade) &&
            Objects.equals(cpfId, that.cpfId) &&
            Objects.equals(cpfId, that.cpfId) &&
            Objects.equals(cpfId, that.cpfId) &&
            Objects.equals(cpfId, that.cpfId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf, nome, idade, cpfId, cpfId, cpfId, cpfId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (idade != null ? "idade=" + idade + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            (cpfId != null ? "cpfId=" + cpfId + ", " : "") +
            "}";
    }
}
