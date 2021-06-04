package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Exame.
 */
@Entity
@Table(name = "exame")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipoexame")
    private String tipoexame;

    @Column(name = "valorexame")
    private String valorexame;

    @Column(name = "descontoconvenio")
    private String descontoconvenio;

    @Column(name = "nomemmedico")
    private String nomemmedico;

    @Lob
    @Column(name = "prerequisito")
    private String prerequisito;

    @Column(name = "datasolicitacao")
    private ZonedDateTime datasolicitacao;

    @Column(name = "dataresultado")
    private ZonedDateTime dataresultado;

    @JsonIgnoreProperties(value = { "nome" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Medico nomemedico;

    @OneToOne
    @JoinColumn(unique = true)
    private ResultadoExame tipoexame;

    @OneToMany(mappedBy = "exame")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exame" }, allowSetters = true)
    private Set<PagamentoExame> valorexames = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cpf", "cpfs", "cpfs", "cpf" }, allowSetters = true)
    private ContaClinica contaClinica;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exame id(Long id) {
        this.id = id;
        return this;
    }

    public String getTipoexame() {
        return this.tipoexame;
    }

    public Exame tipoexame(String tipoexame) {
        this.tipoexame = tipoexame;
        return this;
    }

    public void setTipoexame(String tipoexame) {
        this.tipoexame = tipoexame;
    }

    public String getValorexame() {
        return this.valorexame;
    }

    public Exame valorexame(String valorexame) {
        this.valorexame = valorexame;
        return this;
    }

    public void setValorexame(String valorexame) {
        this.valorexame = valorexame;
    }

    public String getDescontoconvenio() {
        return this.descontoconvenio;
    }

    public Exame descontoconvenio(String descontoconvenio) {
        this.descontoconvenio = descontoconvenio;
        return this;
    }

    public void setDescontoconvenio(String descontoconvenio) {
        this.descontoconvenio = descontoconvenio;
    }

    public String getNomemmedico() {
        return this.nomemmedico;
    }

    public Exame nomemmedico(String nomemmedico) {
        this.nomemmedico = nomemmedico;
        return this;
    }

    public void setNomemmedico(String nomemmedico) {
        this.nomemmedico = nomemmedico;
    }

    public String getPrerequisito() {
        return this.prerequisito;
    }

    public Exame prerequisito(String prerequisito) {
        this.prerequisito = prerequisito;
        return this;
    }

    public void setPrerequisito(String prerequisito) {
        this.prerequisito = prerequisito;
    }

    public ZonedDateTime getDatasolicitacao() {
        return this.datasolicitacao;
    }

    public Exame datasolicitacao(ZonedDateTime datasolicitacao) {
        this.datasolicitacao = datasolicitacao;
        return this;
    }

    public void setDatasolicitacao(ZonedDateTime datasolicitacao) {
        this.datasolicitacao = datasolicitacao;
    }

    public ZonedDateTime getDataresultado() {
        return this.dataresultado;
    }

    public Exame dataresultado(ZonedDateTime dataresultado) {
        this.dataresultado = dataresultado;
        return this;
    }

    public void setDataresultado(ZonedDateTime dataresultado) {
        this.dataresultado = dataresultado;
    }

    public Medico getNomemedico() {
        return this.nomemedico;
    }

    public Exame nomemedico(Medico medico) {
        this.setNomemedico(medico);
        return this;
    }

    public void setNomemedico(Medico medico) {
        this.nomemedico = medico;
    }

    public ResultadoExame getTipoexame() {
        return this.tipoexame;
    }

    public Exame tipoexame(ResultadoExame resultadoExame) {
        this.setTipoexame(resultadoExame);
        return this;
    }

    public void setTipoexame(ResultadoExame resultadoExame) {
        this.tipoexame = resultadoExame;
    }

    public Set<PagamentoExame> getValorexames() {
        return this.valorexames;
    }

    public Exame valorexames(Set<PagamentoExame> pagamentoExames) {
        this.setValorexames(pagamentoExames);
        return this;
    }

    public Exame addValorexame(PagamentoExame pagamentoExame) {
        this.valorexames.add(pagamentoExame);
        pagamentoExame.setExame(this);
        return this;
    }

    public Exame removeValorexame(PagamentoExame pagamentoExame) {
        this.valorexames.remove(pagamentoExame);
        pagamentoExame.setExame(null);
        return this;
    }

    public void setValorexames(Set<PagamentoExame> pagamentoExames) {
        if (this.valorexames != null) {
            this.valorexames.forEach(i -> i.setExame(null));
        }
        if (pagamentoExames != null) {
            pagamentoExames.forEach(i -> i.setExame(this));
        }
        this.valorexames = pagamentoExames;
    }

    public ContaClinica getContaClinica() {
        return this.contaClinica;
    }

    public Exame contaClinica(ContaClinica contaClinica) {
        this.setContaClinica(contaClinica);
        return this;
    }

    public void setContaClinica(ContaClinica contaClinica) {
        this.contaClinica = contaClinica;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exame)) {
            return false;
        }
        return id != null && id.equals(((Exame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exame{" +
            "id=" + getId() +
            ", tipoexame='" + getTipoexame() + "'" +
            ", valorexame='" + getValorexame() + "'" +
            ", descontoconvenio='" + getDescontoconvenio() + "'" +
            ", nomemmedico='" + getNomemmedico() + "'" +
            ", prerequisito='" + getPrerequisito() + "'" +
            ", datasolicitacao='" + getDatasolicitacao() + "'" +
            ", dataresultado='" + getDataresultado() + "'" +
            "}";
    }
}
