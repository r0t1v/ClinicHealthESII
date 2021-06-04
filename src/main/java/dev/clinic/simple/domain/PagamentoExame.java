package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PagamentoExame.
 */
@Entity
@Table(name = "pagamento_exame")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PagamentoExame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "formapagamento")
    private String formapagamento;

    @Column(name = "conteudo")
    private String conteudo;

    @Column(name = "seliquidado")
    private Boolean seliquidado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "nomemedico", "tipoexame", "valorexames", "contaClinica" }, allowSetters = true)
    private Exame exame;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PagamentoExame id(Long id) {
        this.id = id;
        return this;
    }

    public String getFormapagamento() {
        return this.formapagamento;
    }

    public PagamentoExame formapagamento(String formapagamento) {
        this.formapagamento = formapagamento;
        return this;
    }

    public void setFormapagamento(String formapagamento) {
        this.formapagamento = formapagamento;
    }

    public String getConteudo() {
        return this.conteudo;
    }

    public PagamentoExame conteudo(String conteudo) {
        this.conteudo = conteudo;
        return this;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Boolean getSeliquidado() {
        return this.seliquidado;
    }

    public PagamentoExame seliquidado(Boolean seliquidado) {
        this.seliquidado = seliquidado;
        return this;
    }

    public void setSeliquidado(Boolean seliquidado) {
        this.seliquidado = seliquidado;
    }

    public Exame getExame() {
        return this.exame;
    }

    public PagamentoExame exame(Exame exame) {
        this.setExame(exame);
        return this;
    }

    public void setExame(Exame exame) {
        this.exame = exame;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagamentoExame)) {
            return false;
        }
        return id != null && id.equals(((PagamentoExame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagamentoExame{" +
            "id=" + getId() +
            ", formapagamento='" + getFormapagamento() + "'" +
            ", conteudo='" + getConteudo() + "'" +
            ", seliquidado='" + getSeliquidado() + "'" +
            "}";
    }
}
