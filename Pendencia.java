package itaipu.gss.cpo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import itaipu.gss.framework.exception.AppException;
import itaipu.gss.framework.exception.UserInputException;
import itaipu.gss.framework.gui.TextEditor;

@Entity
@Table(name = "pendencia")
public class Pendencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "emissao")
	private Date emissao = new Date();
	
	@Column(name = "data_prevista_conclusao")
	private Date dataPrevistaConclusao;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "evento")
	private String evento = "";

	@ManyToOne
	@JoinColumn(name = "grupo")
	private Grupo grupo;
	
	@Column(name = "localizacao")
	private String localizacao;
	
	@ManyToOne
	@JoinColumn(name = "acompanhamento_frequencia")
	private Frequencia frequenciaAcompanhamento;
	
	@ManyToOne
	@JoinColumn(name = "setor_responsavel")
	private Setor setorResponsavel;

	@ManyToOne
	@JoinColumn(name = "responsavel")
	private Usuario responsavel;
	
	@ManyToOne
	@JoinColumn(name = "responsavel_2")
	private Usuario responsavel2;
	
	@ManyToOne
	@JoinColumn(name = "prioridade_id")
	private Prioridade prioridade;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pendencia")
	private List<Documento> documentoList = new ArrayList<Documento>();
	
	@ManyToMany
	@JoinTable(name = "pendencia_area", joinColumns = @JoinColumn(name = "pendencia"), inverseJoinColumns = @JoinColumn(name = "area"))
	private List<Area> areasEnvolvidas = new ArrayList<Area>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pendencia")
	private List<Acompanhamento> acompanhamentoList = new ArrayList<Acompanhamento>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pendencia")
	private List<Registro> registroList = new ArrayList<Registro>();

	@ManyToOne
	@JoinColumn(name = "estado")
	private Estado estado;

	@ManyToMany
	@JoinTable(name = "pendencia_setor", joinColumns = @JoinColumn(name = "pendencia"), inverseJoinColumns = @JoinColumn(name = "setor_responsavel"))
	private List<Setor> setoresDeApoio = new ArrayList<Setor>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pendencia")
	List<Anexo> anexoList = new ArrayList<Anexo>();

	@Transient
	private boolean hasChanges;
	
	@Transient
	private Estado estadoAnterior = null;

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) throws UserInputException {
		if (nome == null || "".equals(nome)) {
			throw new UserInputException("Por favor, informe o Título.");
		}
		
		if (nome.length() >= 255) {
			throw new UserInputException("Por favor, informe o Título com menos de 255 caracteres.");
		}
			
		this.nome = nome;
	}

	public Date getEmissao() {
		return this.emissao;
	}

	public void setEmissao(Date emissao) throws UserInputException  {
		if (emissao == null) {
			throw new UserInputException("Por favor, informe a Data de Emissão.");
		}
		this.emissao = emissao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws AppException {
		
		if (descricao == null || TextEditor.getTextLength(descricao) < 5) {
			throw new AppException("Por favor, informe uma descrição com mais de 5 caracteres.");
		}
		
		String plainText = TextEditor.formattedToPlainText(descricao);
		
		if (plainText.length() >= 5000) {
			throw new AppException("Por favor, informe uma descrição com menos de 5.000 caracteres.");
		}
		
		if (descricao.length() >= 10000) {
			throw new AppException("Por favor, reduza o tamanho da descrição. A descrição formatada tem um tamanho maior do que 10.000: " + descricao.length());
		}		
		
		this.descricao = descricao;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}
	
	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) throws UserInputException {
		if (grupo == null) {
			throw new UserInputException("Por favor, selecione o Grupo do Responsável.");
		}
		this.grupo = grupo;
	}
	
	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public Frequencia getFrequenciaAcompanhamento() {
		return frequenciaAcompanhamento;
	}

	public void setFrequenciaAcompanhamento(Frequencia frequenciaAcompanhamento) throws UserInputException {
		if (frequenciaAcompanhamento == null) {
			throw new UserInputException("Por favor, selecione a Frequência de Acompanhamento.");
		}
		this.frequenciaAcompanhamento = frequenciaAcompanhamento;
	}

	public Setor getSetorResponsavel() {
		return setorResponsavel;
	}

	public void setSetorResponsavel(Setor setorResponsavel) throws UserInputException {
		if (setorResponsavel == null) {
			throw new UserInputException("Por favor, selecione o Setor Responsável.");
		}
		this.setorResponsavel = setorResponsavel;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public Usuario getResponsavel2() {
		return responsavel2;
	}
	
	public void setResponsavel(Usuario responsavel) throws UserInputException {
		if (responsavel == null) {
			throw new UserInputException("Por favor, selecione o Responsável.");
		}
		
		if (!responsavel.getGrupoList().contains(this.grupo)) {
			throw new UserInputException("O Responsável não está no grupo selecionado. Por favor, verifique.");
		}
		
		this.responsavel = responsavel;
	}
	
	public void setResponsavel2(Usuario responsavel2) throws UserInputException {
		if (responsavel2 != null) {
			if (responsavel.equals(responsavel2)) {
				throw new UserInputException("Os dois responsaveis não podem ser os mesmos.");
			}
			
			if (!responsavel2.getGrupoList().contains(this.grupo)) {
				throw new UserInputException("O Responsável não está no grupo selecionado. Por favor, verifique.");
			}
		}
		
		this.responsavel2 = responsavel2;
	}
	

	public List<Documento> getDocumentoList() {
		return documentoList;
	}

	public void setDocumentoList(List<Documento> documentoList) {
		this.documentoList = documentoList;
	}
	
	public List<Area> getAreasEnvolvidas() {
		return areasEnvolvidas;
	}

	public void setAreasEnvolvidas(List<Area> areasEnvolvidas) {
		this.areasEnvolvidas = areasEnvolvidas;
	}

	public List<Acompanhamento> getAcompanhamentoList() {
		return acompanhamentoList;
	}

	public void setAcompanhamentoList(List<Acompanhamento> acompanhamentoList) {
		this.acompanhamentoList = acompanhamentoList;
	}

	public List<Registro> getRegistroList() {
		return registroList;
	}

	public void setRegistroList(List<Registro> registroList) {
		this.registroList = registroList;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estadoNovo, Usuario usuario) throws AppException {
		boolean canChange = false;
		// Percorre a lista de papeis de um estado
		for (Papel papelEstado : estadoNovo.getPapeis()) {
			// Verifica se o usuário contém aquele papel
			if (usuario.getPapelList().contains(papelEstado)) {
				canChange = true;
				break;
			}			
		}
		// Lança uma exceção com o erro.
		if (!canChange)
			throw new AppException("O usuário '" + usuario.getLogin() 
				+ "' não tem permissão para salvar alterações em pendência com o estado '" 
				+ estadoNovo.getNome() + "'.");
		// Armazena o estado anterior em uma variável temporária.
		this.estadoAnterior = this.estado;	
		this.estado = estadoNovo;				
	}
	
	public List<Setor> getSetoresDeApoio() {
		return setoresDeApoio;
	}

	public void setSetoresDeApoio(List<Setor> setoresDeApoio) {
		this.setoresDeApoio = setoresDeApoio;
	}

	public void addRegistroList(Registro registro) {
		this.registroList.add(registro);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pendencia other = (Pendencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.nome;
	}
	
	public List<Anexo> getAnexoList() {
		return anexoList;
	}

	public void setAnexoList(List<Anexo> anexoList) {
		this.anexoList = anexoList;
	}

	public Estado getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(Estado estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public boolean hasChanges() {
		return this.hasChanges;
	}
	
	public void setHasChanges(boolean hasChanges) {
		this.hasChanges = hasChanges;
	}

	public Integer getId() {
		return id;
	}

	public Date getDataPrevistaConclusao() {
		return dataPrevistaConclusao;
	}
	
	public Prioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) throws UserInputException {
		if (prioridade == null) {
			throw new UserInputException("Por favor, selecione a prioridade");
		}
		this.prioridade = prioridade;
	}

	public void setDataPrevistaConclusao(Date dataPrevistaConclusao) throws AppException {
		if (dataPrevistaConclusao == null)
			throw new AppException("Por favor, informe a data prevista de conclusão.");
		
		if (dataPrevistaConclusao.before(this.emissao))
			throw new AppException("A data prevista de conclusão deve ser igual ou posterior a data de emissão.");
		
		this.dataPrevistaConclusao = dataPrevistaConclusao;
	}

	public Acompanhamento getUltimoAcompanhamento() {
		if (!this.acompanhamentoList.isEmpty()) {
			return this.acompanhamentoList.get(this.acompanhamentoList.size() - 1);
		} else {
			return null;
		}
	}
	
}
