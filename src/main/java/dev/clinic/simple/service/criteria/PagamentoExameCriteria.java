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
 * Criteria class for the {@link dev.clinic.simple.domain.PagamentoExame} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.PagamentoExameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pagamento-exames?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PagamentoExameCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter formapagamento;

    private StringFilter conteudo;

    private BooleanFilter seliquidado;

    private LongFilter exameId;

    public PagamentoExameCriteria() {}

    public PagamentoExameCriteria(PagamentoExameCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.formapagamento = other.formapagamento == null ? null : other.formapagamento.copy();
        this.conteudo = other.conteudo == null ? null : other.conteudo.copy();
        this.seliquidado = other.seliquidado == null ? null : other.seliquidado.copy();
        this.exameId = other.exameId == null ? null : other.exameId.copy();
    }

    @Override
    public PagamentoExameCriteria copy() {
        return new PagamentoExameCriteria(this);
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

    public StringFilter getFormapagamento() {
        return formapagamento;
    }

    public StringFilter formapagamento() {
        if (formapagamento == null) {
            formapagamento = new StringFilter();
        }
        return formapagamento;
    }

    public void setFormapagamento(StringFilter formapagamento) {
        this.formapagamento = formapagamento;
    }

    public StringFilter getConteudo() {
        return conteudo;
    }

    public StringFilter conteudo() {
        if (conteudo == null) {
            conteudo = new StringFilter();
        }
        return conteudo;
    }

    public void setConteudo(StringFilter conteudo) {
        this.conteudo = conteudo;
    }

    public BooleanFilter getSeliquidado() {
        return seliquidado;
    }

    public BooleanFilter seliquidado() {
        if (seliquidado == null) {
            seliquidado = new BooleanFilter();
        }
        return seliquidado;
    }

    public void setSeliquidado(BooleanFilter seliquidado) {
        this.seliquidado = seliquidado;
    }

    public LongFilter getExameId() {
        return exameId;
    }

    public LongFilter exameId() {
        if (exameId == null) {
            exameId = new LongFilter();
        }
        return exameId;
    }

    public void setExameId(LongFilter exameId) {
        this.exameId = exameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PagamentoExameCriteria that = (PagamentoExameCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(formapagamento, that.formapagamento) &&
            Objects.equals(conteudo, that.conteudo) &&
            Objects.equals(seliquidado, that.seliquidado) &&
            Objects.equals(exameId, that.exameId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, formapagamento, conteudo, seliquidado, exameId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagamentoExameCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (formapagamento != null ? "formapagamento=" + formapagamento + ", " : "") +
            (conteudo != null ? "conteudo=" + conteudo + ", " : "") +
            (seliquidado != null ? "seliquidado=" + seliquidado + ", " : "") +
            (exameId != null ? "exameId=" + exameId + ", " : "") +
            "}";
    }
}
