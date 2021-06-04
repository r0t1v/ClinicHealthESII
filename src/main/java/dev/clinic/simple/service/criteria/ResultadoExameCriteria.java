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
 * Criteria class for the {@link dev.clinic.simple.domain.ResultadoExame} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.ResultadoExameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resultado-exames?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ResultadoExameCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descricao;

    private StringFilter prescricao;

    private StringFilter indicacao;

    private StringFilter contraindicacao;

    public ResultadoExameCriteria() {}

    public ResultadoExameCriteria(ResultadoExameCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.prescricao = other.prescricao == null ? null : other.prescricao.copy();
        this.indicacao = other.indicacao == null ? null : other.indicacao.copy();
        this.contraindicacao = other.contraindicacao == null ? null : other.contraindicacao.copy();
    }

    @Override
    public ResultadoExameCriteria copy() {
        return new ResultadoExameCriteria(this);
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

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public StringFilter getPrescricao() {
        return prescricao;
    }

    public StringFilter prescricao() {
        if (prescricao == null) {
            prescricao = new StringFilter();
        }
        return prescricao;
    }

    public void setPrescricao(StringFilter prescricao) {
        this.prescricao = prescricao;
    }

    public StringFilter getIndicacao() {
        return indicacao;
    }

    public StringFilter indicacao() {
        if (indicacao == null) {
            indicacao = new StringFilter();
        }
        return indicacao;
    }

    public void setIndicacao(StringFilter indicacao) {
        this.indicacao = indicacao;
    }

    public StringFilter getContraindicacao() {
        return contraindicacao;
    }

    public StringFilter contraindicacao() {
        if (contraindicacao == null) {
            contraindicacao = new StringFilter();
        }
        return contraindicacao;
    }

    public void setContraindicacao(StringFilter contraindicacao) {
        this.contraindicacao = contraindicacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ResultadoExameCriteria that = (ResultadoExameCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(prescricao, that.prescricao) &&
            Objects.equals(indicacao, that.indicacao) &&
            Objects.equals(contraindicacao, that.contraindicacao)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, prescricao, indicacao, contraindicacao);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultadoExameCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (prescricao != null ? "prescricao=" + prescricao + ", " : "") +
            (indicacao != null ? "indicacao=" + indicacao + ", " : "") +
            (contraindicacao != null ? "contraindicacao=" + contraindicacao + ", " : "") +
            "}";
    }
}
