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
 * Criteria class for the {@link dev.clinic.simple.domain.ContatoPaciente} entity. This class is used
 * in {@link dev.clinic.simple.web.rest.ContatoPacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contato-pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContatoPacienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tipo;

    private StringFilter conteudo;

    private LongFilter pacienteId;

    public ContatoPacienteCriteria() {}

    public ContatoPacienteCriteria(ContatoPacienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.conteudo = other.conteudo == null ? null : other.conteudo.copy();
        this.pacienteId = other.pacienteId == null ? null : other.pacienteId.copy();
    }

    @Override
    public ContatoPacienteCriteria copy() {
        return new ContatoPacienteCriteria(this);
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

    public StringFilter getTipo() {
        return tipo;
    }

    public StringFilter tipo() {
        if (tipo == null) {
            tipo = new StringFilter();
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
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
        final ContatoPacienteCriteria that = (ContatoPacienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(conteudo, that.conteudo) &&
            Objects.equals(pacienteId, that.pacienteId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, conteudo, pacienteId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContatoPacienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (conteudo != null ? "conteudo=" + conteudo + ", " : "") +
            (pacienteId != null ? "pacienteId=" + pacienteId + ", " : "") +
            "}";
    }
}
