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
 * Criteria class for the {@link dev.clinic.simple.domain.Medico} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.MedicoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medicos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MedicoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter crm;

    private IntegerFilter nome;

    private StringFilter graduacao;

    private StringFilter atuacao;

    private LongFilter nomeId;

    public MedicoCriteria() {}

    public MedicoCriteria(MedicoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.crm = other.crm == null ? null : other.crm.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.graduacao = other.graduacao == null ? null : other.graduacao.copy();
        this.atuacao = other.atuacao == null ? null : other.atuacao.copy();
        this.nomeId = other.nomeId == null ? null : other.nomeId.copy();
    }

    @Override
    public MedicoCriteria copy() {
        return new MedicoCriteria(this);
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

    public IntegerFilter getCrm() {
        return crm;
    }

    public IntegerFilter crm() {
        if (crm == null) {
            crm = new IntegerFilter();
        }
        return crm;
    }

    public void setCrm(IntegerFilter crm) {
        this.crm = crm;
    }

    public IntegerFilter getNome() {
        return nome;
    }

    public IntegerFilter nome() {
        if (nome == null) {
            nome = new IntegerFilter();
        }
        return nome;
    }

    public void setNome(IntegerFilter nome) {
        this.nome = nome;
    }

    public StringFilter getGraduacao() {
        return graduacao;
    }

    public StringFilter graduacao() {
        if (graduacao == null) {
            graduacao = new StringFilter();
        }
        return graduacao;
    }

    public void setGraduacao(StringFilter graduacao) {
        this.graduacao = graduacao;
    }

    public StringFilter getAtuacao() {
        return atuacao;
    }

    public StringFilter atuacao() {
        if (atuacao == null) {
            atuacao = new StringFilter();
        }
        return atuacao;
    }

    public void setAtuacao(StringFilter atuacao) {
        this.atuacao = atuacao;
    }

    public LongFilter getNomeId() {
        return nomeId;
    }

    public LongFilter nomeId() {
        if (nomeId == null) {
            nomeId = new LongFilter();
        }
        return nomeId;
    }

    public void setNomeId(LongFilter nomeId) {
        this.nomeId = nomeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MedicoCriteria that = (MedicoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(crm, that.crm) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(graduacao, that.graduacao) &&
            Objects.equals(atuacao, that.atuacao) &&
            Objects.equals(nomeId, that.nomeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, crm, nome, graduacao, atuacao, nomeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (crm != null ? "crm=" + crm + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (graduacao != null ? "graduacao=" + graduacao + ", " : "") +
            (atuacao != null ? "atuacao=" + atuacao + ", " : "") +
            (nomeId != null ? "nomeId=" + nomeId + ", " : "") +
            "}";
    }
}
