package itaipu.gss.cpo.controller;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import itaipu.gss.cpo.dao.CRUDDaoEstado;
import itaipu.gss.cpo.dao.CRUDDaoFrequencia;
import itaipu.gss.cpo.dao.CRUDDaoGrupo;
import itaipu.gss.cpo.dao.CRUDDaoPendencia;
import itaipu.gss.cpo.dao.CRUDDaoPrioridade;
import itaipu.gss.cpo.dao.CRUDDaoUsuario;
import itaipu.gss.cpo.model.Estado;
import itaipu.gss.cpo.model.Grupo;
import itaipu.gss.cpo.model.GrupoTipo;
import itaipu.gss.cpo.model.Papel;
import itaipu.gss.cpo.model.Pendencia;
import itaipu.gss.cpo.model.Usuario;
import itaipu.gss.cpo.view.CRUDDialogPendencia;
import itaipu.gss.cpo.view.CRUDSearchDialogPendencia;
import itaipu.gss.framework.commons.Email;
import itaipu.gss.framework.exception.AppException;
import itaipu.gss.framework.gui.CRUDController;
import itaipu.gss.framework.gui.CRUDDao;
import itaipu.gss.framework.gui.CRUDDialogAction;
import itaipu.gss.framework.gui.CRUDSearch;
import itaipu.gss.framework.gui.TextEditor;
import itaipu.gss.framework.log.AppLogger;

public class CRUDControllerPendencia extends CRUDController<Pendencia> {
	

	protected GrupoTipo getGrupoTipo() {
		return new GrupoTipo(GrupoTipo.ID_PENDENCIA);
	}
	
	protected String getSearchTitle() {
		return "Pend�ncias";
	}
	
	protected String getTitulo() {
		return "Pend�ncia";
	}

	@Override
	protected CRUDDialogAction<Pendencia> buildDialogCadastro() {
		CRUDDaoFrequencia daoFrequencia = new CRUDDaoFrequencia();
		CRUDDaoPrioridade daoPrioridade = new CRUDDaoPrioridade();
		CRUDDaoUsuario daoUsuario = new CRUDDaoUsuario();
		
		return new CRUDDialogPendencia(this.getDialogSearch(), 
				daoFrequencia.listAll(), 
				daoUsuario.getUsuarioLogado(), 
				getGrupoTipo(),
				daoPrioridade.listAll(), 
				getTitulo());
	}

	@Override
	protected CRUDSearch<Pendencia> buildDialogSearch(Window parent) {
		return new CRUDSearchDialogPendencia(parent, getSearchTitle());
	}

	@Override
	protected CRUDDao<Pendencia> buildDAO() {
		return new CRUDDaoPendencia(this.getGrupoTipo());
	}
	
	protected String getMessageCadastroHeader(Pendencia pendencia) {
		return "A <b>PEND�NCIA #"+pendencia.getId()+"</b> foi criada.";
	}
	
	protected String getMessageAtualizacaoHeader(Pendencia pendencia) {
		return "A <b>PEND�NCIA #"+pendencia.getId()+"</b> foi atualizada.";
	}

	protected String getSubjectCadastro(Pendencia pendencia) {
		return "Nova pend�ncia cadastrada";
	}
	
	protected String getSubjectAtualizacao(Pendencia pendencia) {
		return "Pend�ncia #" + pendencia.getId() + " atualizada";
	}
	
	private String getMessageCadastro(Pendencia pendencia) {
		String message = getMessageCadastroHeader(pendencia)
			+ "<br><br><b>T�tulo:</b> "+ pendencia.getNome()
			+ "<br><b>Estado:</b> " + pendencia.getEstado().getNome()
			+ "<br><b>Respons�vel 1:</b> " + pendencia.getResponsavel().getNome()
			+ (pendencia.getResponsavel2() != null ? "<br><b>Respons�vel 2:</b> " + pendencia.getResponsavel2().getNome() : "")
			+ "<br><br><b>Descri��o:</b> " + pendencia.getDescricao();
			
		if(TextEditor.getTextLength(pendencia.getEstado().getAcaoRequerida()) > 0) {
			message += "<b>A��o Requerida:</b> " + pendencia.getEstado().getAcaoRequerida();
		}
		return message;
	}
	
	private String getMessageAtualizacao(Pendencia pendencia) {
		String message = getMessageAtualizacaoHeader(pendencia)
				+ "<br><br><b>T�tulo:</b> "+ pendencia.getNome()
				+ "<br><b>Estado:</b> " + pendencia.getEstado().getNome()
				+ "<br><b>Respons�vel 1:</b> " + pendencia.getResponsavel().getNome()
				+ (pendencia.getResponsavel2() != null ? "<br><b>Respons�vel 2:</b> " + pendencia.getResponsavel2().getNome() : "")
				+ "<br><br><b>Descri��o:</b> " + pendencia.getDescricao();	
		
		if(TextEditor.getTextLength(pendencia.getEstado().getAcaoRequerida()) > 0) {
			message += "<b>A��o Requerida:</b> " + pendencia.getEstado().getAcaoRequerida();
		}
		return message;
	}

	@Override
	public void validate(Object object) throws AppException {
		Pendencia pendencia = (Pendencia) object;
		Estado estadoNovo = pendencia.getEstado();
		
		CRUDDaoEstado daoEstado = new CRUDDaoEstado();
		estadoNovo = (Estado) daoEstado.reload(estadoNovo.getIdentificador());
		
		CRUDDaoUsuario daoUsuario = new CRUDDaoUsuario();
		
		// Verifica se foi alterado o estado da pend�ncia e se o usu�rio tem permiss�o para fazer esta mudan�a.
		verificaPapeis(estadoNovo, daoUsuario.getUsuarioLogado());		
	}
	
	@Override
	protected void afterEdit(Pendencia pendencia) throws AppException {
		sendEmail(pendencia);
	}
	
	
	@Override
	protected void afterSave(Pendencia pendencia) throws AppException {
		sendEmail(pendencia);
	}

	private void sendEmail(Pendencia pendencia) throws AppException {
		CRUDDaoEstado daoEstado = new CRUDDaoEstado();
		CRUDDaoUsuario daoUsuario = new CRUDDaoUsuario();
		CRUDDaoGrupo daoGrupo = new CRUDDaoGrupo();
		
		// Registra a altera��o na pend�ncia.
		if (pendencia.hasChanges()) {
			// Verifica se a pend�ncia mudou de estado e notifica o respons�vel e grupo se for o caso.
			if (!pendencia.getEstado().equals(pendencia.getEstadoAnterior())) {
				// Inicializa uma vari�vel para armazenar a lista de destinat�rios.
				List<Usuario> usuarioToList = new ArrayList<Usuario>();
				// Verifica se os pr�ximos grupos devem ser notificados
				if (pendencia.getEstado().getNotificarGrupo()) {
					List<Papel> papelProcedenteList = new ArrayList<Papel>();
					Estado novoEstado = daoEstado.open(pendencia.getEstado());
					for (Estado estadoProcedente : novoEstado.getEstadoProcedenteList()) {
						papelProcedenteList.addAll(estadoProcedente.getPapeis());
					}
					
					Grupo grupo = pendencia.getGrupo();										
					List<Usuario> usuarioList = daoUsuario.list("");
					
					// Verifica quais usu�rios podem passar a pend�ncia para o pr�ximo estado
					for (Usuario usuario : usuarioList) {
						if (usuario.getGrupoList().contains(grupo)
								&& !Collections.disjoint(papelProcedenteList, usuario.getPapelList())) {
							// Adiciona o usu�rio na lista de destinat�rios.						
							usuarioToList.add(usuario);
						}
					}
				}
				
				// Acrescenta o respons�vel, se ele ainda n�o foi
				if (!usuarioToList.contains(pendencia.getResponsavel())) {
					usuarioToList.add(pendencia.getResponsavel());
				}
				
				// Acrescenta o respons�vel, se ele ainda n�o foi
				if (pendencia.getResponsavel2() != null && !usuarioToList.contains(pendencia.getResponsavel2())) {
					usuarioToList.add(pendencia.getResponsavel2());
				}
				
				// Transforma em uma lista de strings
				List<String> toList = new ArrayList<>();
				boolean isCurrentUserInTolist = false;
				Usuario usuarioLogado = daoUsuario.getUsuarioLogado();
				for (Usuario usuario : usuarioToList) {
					toList.add(usuario.getEmail());
					if (usuario.getLogin().equals(usuarioLogado.getLogin())) {
						isCurrentUserInTolist = true;
					}
				}
				
				// Add current user in CC list
				List<String> ccList = new ArrayList<>();
				if (!isCurrentUserInTolist) {
					ccList.add(usuarioLogado.getEmail());
				}
				
				// Add watchers in CC
				List<Grupo> grupoObservadoresList = daoGrupo.listWatchers();
				for (Grupo grupoObservador : grupoObservadoresList) {
					for (Usuario usuarioObservador : grupoObservador.getUsuarioList()) {
						if (!ccList.contains(usuarioObservador.getEmail())) {
							ccList.add(usuarioObservador.getEmail());
						}
					}
				}
				
				if (toList.size() > 0) {
					// Verifica se � a primeira pend�ncia.
					if (pendencia.getEstadoAnterior() == null) {
						// Envia um email com o conte�do da pend�ncia para o respons�vel.			
						Email.send(toList, ccList, getSubjectCadastro(pendencia), getMessageCadastro(pendencia));

					} else {
						// Envia um email com o conte�do da pend�ncia para o respons�vel.													
						Email.send(toList, ccList, getSubjectAtualizacao(pendencia) , getMessageAtualizacao(pendencia));
					}
				}
			}			
		}
	}
	
	private boolean verificaPapeis(Estado estadoNovo, Usuario usuario) throws AppException {
		AppLogger.logDebugMessage("Verifica��o do papel para o estado novo: " + estadoNovo.getNome());
		
		// Percorre a lista de papeis de um estado
		for (Papel papelEstado : estadoNovo.getPapeis()) {
			// Verifica se o usu�rio cont�m aquele papel
			if (usuario.getPapelList().contains(papelEstado)) {
				return true;
			}			
		}
		// Lan�a uma exce��o com o erro.
		throw new AppException("O usu�rio '" + usuario.getLogin() 
			+ "' n�o tem permiss�o para salvar altera��es em pend�ncia com o estado '" 
				+ estadoNovo.getNome() + "'.");
	}

	@Override
	public void delete(Pendencia pendencia) throws AppException {
		// Verifica se o usu�rio � do grupo da pend�ncia.
		CRUDDaoUsuario daoUsuario = new CRUDDaoUsuario();
		Usuario currentUser = daoUsuario.getUsuarioLogado();
		if (!currentUser.getGrupoList().contains(pendencia.getGrupo())) {
			throw new AppException("Usu�rio '"+ currentUser.getLogin() 
				+ "' n�o pertence ao grupo da pend�ncia: " + pendencia.getGrupo().getNome());
		}
		
		// Verifica se o usu�rio tem ao menos um pap�l que permita apagar uma pend�ncia
		boolean canDelete = false;
		for (Papel papel : currentUser.getPapelList()) {
			if (papel.getCanDelete()) {
				canDelete = true;
				break;
			}
		}
		
		if (canDelete) {
			super.delete(pendencia);;
		} else {
			throw new AppException("Usu�rio '"+ currentUser.getLogin() 
				+ "' n�o tem papel para REMOVER.");
		}
	}
	
	@Override
	protected boolean hasRoleEdit(Pendencia pendencia) {
		CRUDDaoUsuario daoUsuario = new CRUDDaoUsuario();
		Usuario usuario = daoUsuario.getUsuarioLogado();
		boolean hasRoleEdit = usuario.getGrupoList().contains(pendencia.getGrupo());
		if (!hasRoleEdit) {
			AppLogger.logErrorMessage("N�o � poss�vel abrir o registro #" + pendencia.getId() + ". O usu�rio n�o est� no grupo: " + pendencia.getGrupo());
		}
		
		return hasRoleEdit;
	}
	
	public List<Estado> getEstadoInicialList() {
		// As op��es dispon�veis para o primeiro estado ser�o os estados
		// que n�o t�m nenhum antecedente. (ou seja, n�o s�o precedentes a nenhum outro
		// estado).
		CRUDControllerEstado crudControllerEstado = new CRUDControllerEstado();
		List<Estado> estadoList = crudControllerEstado.listAll();
		// Cria uma lista com todos os estados que s�o precedentes de outro estado
		List<Estado> estadoPrecedenteList = new ArrayList<Estado>();
		List<Estado> estadoInicialList = new ArrayList<Estado>();
		for (Estado estado : estadoList) {
			estadoPrecedenteList.addAll(estado.getEstadoProcedenteList());
		}
		// Verifica estados que n�o s�o precedentes e acrescenta no combobox.
		for (Estado estado : estadoList) {
			if (!estadoPrecedenteList.contains(estado)) {
				estadoInicialList.add(estado);
			}
		}
		
		return estadoInicialList;
	}
	
	@Override
	protected boolean hasRoleInsert() {
		CRUDDaoUsuario daoUsuario = new CRUDDaoUsuario();
		Usuario usuario = daoUsuario.getUsuarioLogado();
		
		for (Estado estado : getEstadoInicialList()) {
			for (Papel papel : estado.getPapeis()) {
				if (usuario.getPapelList().contains(papel)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Check if user can delete the data.
	 * @return true if the user can delete the data.
	 */
	protected boolean hasRoleDelete(Pendencia pendencia) {
		return hasRoleEdit(pendencia);
	}
}
