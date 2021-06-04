package dev.clinic.simple.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ResultadoExame.
 */
@Entity
@Table(name = "resultado_exame")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResultadoExame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "prescricao")
    private String prescricao;

    @Column(name = "indicacao")
    private String indicacao;

    @Column(name = "contraindicacao")
    private String contraindicacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResultadoExame id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public ResultadoExame descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPrescricao() {
        return this.prescricao;
    }

    public ResultadoExame prescricao(String prescricao) {
        this.prescricao = prescricao;
        return this;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public String getIndicacao() {
        return this.indicacao;
    }

    public ResultadoExame indicacao(String indicacao) {
        this.indicacao = indicacao;
        return this;
    }

    public void setIndicacao(String indicacao) {
        this.indicacao = indicacao;
    }

    public String getContraindicacao() {
        return this.contraindicacao;
    }

    public ResultadoExame contraindicacao(String contraindicacao) {
        this.contraindicacao = contraindicacao;
        return this;
    }

    public void setContraindicacao(String contraindicacao) {
        this.contraindicacao = contraindicacao;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultadoExame)) {
            return false;
        }
        return id != null && id.equals(((ResultadoExame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultadoExame{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", prescricao='" + getPrescricao() + "'" +
            ", indicacao='" + getIndicacao() + "'" +
            ", contraindicacao='" + getContraindicacao() + "'" +
            "}";
    }
}
