package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "idade")
    private String idade;

    @JsonIgnoreProperties(value = { "cpf", "cpfs", "cpfs", "cpf" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ContaClinica cpf;

    @OneToMany(mappedBy = "paciente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    private Set<EnderecoPaciente> cpfs = new HashSet<>();

    @OneToMany(mappedBy = "paciente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    private Set<ContatoPaciente> cpfs = new HashSet<>();

    @JsonIgnoreProperties(value = { "cpf", "cpfs", "cpfs", "cpf" }, allowSetters = true)
    @OneToOne(mappedBy = "cpf")
    private ContaClinica cpf;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente id(Long id) {
        this.id = id;
        return this;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Paciente cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return this.nome;
    }

    public Paciente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return this.idade;
    }

    public Paciente idade(String idade) {
        this.idade = idade;
        return this;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public ContaClinica getCpf() {
        return this.cpf;
    }

    public Paciente cpf(ContaClinica contaClinica) {
        this.setCpf(contaClinica);
        return this;
    }

    public void setCpf(ContaClinica contaClinica) {
        this.cpf = contaClinica;
    }

    public Set<EnderecoPaciente> getCpfs() {
        return this.cpfs;
    }

    public Paciente cpfs(Set<EnderecoPaciente> enderecoPacientes) {
        this.setCpfs(enderecoPacientes);
        return this;
    }

    public Paciente addCpf(EnderecoPaciente enderecoPaciente) {
        this.cpfs.add(enderecoPaciente);
        enderecoPaciente.setPaciente(this);
        return this;
    }

    public Paciente removeCpf(EnderecoPaciente enderecoPaciente) {
        this.cpfs.remove(enderecoPaciente);
        enderecoPaciente.setPaciente(null);
        return this;
    }

    public void setCpfs(Set<EnderecoPaciente> enderecoPacientes) {
        if (this.cpfs != null) {
            this.cpfs.forEach(i -> i.setPaciente(null));
        }
        if (enderecoPacientes != null) {
            enderecoPacientes.forEach(i -> i.setPaciente(this));
        }
        this.cpfs = enderecoPacientes;
    }

    public Set<ContatoPaciente> getCpfs() {
        return this.cpfs;
    }

    public Paciente cpfs(Set<ContatoPaciente> contatoPacientes) {
        this.setCpfs(contatoPacientes);
        return this;
    }

    public Paciente addCpf(ContatoPaciente contatoPaciente) {
        this.cpfs.add(contatoPaciente);
        contatoPaciente.setPaciente(this);
        return this;
    }

    public Paciente removeCpf(ContatoPaciente contatoPaciente) {
        this.cpfs.remove(contatoPaciente);
        contatoPaciente.setPaciente(null);
        return this;
    }

    public void setCpfs(Set<ContatoPaciente> contatoPacientes) {
        if (this.cpfs != null) {
            this.cpfs.forEach(i -> i.setPaciente(null));
        }
        if (contatoPacientes != null) {
            contatoPacientes.forEach(i -> i.setPaciente(this));
        }
        this.cpfs = contatoPacientes;
    }

    public ContaClinica getCpf() {
        return this.cpf;
    }

    public Paciente cpf(ContaClinica contaClinica) {
        this.setCpf(contaClinica);
        return this;
    }

    public void setCpf(ContaClinica contaClinica) {
        if (this.cpf != null) {
            this.cpf.setCpf(null);
        }
        if (cpf != null) {
            cpf.setCpf(this);
        }
        this.cpf = contaClinica;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return id != null && id.equals(((Paciente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", cpf='" + getCpf() + "'" +
            ", nome='" + getNome() + "'" +
            ", idade='" + getIdade() + "'" +
            "}";
    }
}
