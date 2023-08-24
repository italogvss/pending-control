package itaipu.gss.cpo.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;

import itaipu.gss.cpo.model.Estado;
import itaipu.gss.cpo.model.Grupo;
import itaipu.gss.cpo.model.GrupoTipo;
import itaipu.gss.cpo.model.Pendencia;
import itaipu.gss.cpo.model.Usuario;
import itaipu.gss.framework.db.DAO;
import itaipu.gss.framework.db.Parameter;
import itaipu.gss.framework.exception.AppException;
import itaipu.gss.framework.gui.CRUDDao;
import itaipu.gss.framework.gui.CRUDDaoEager;
import itaipu.gss.framework.log.AppLogger;

public class CRUDDaoPendencia extends DAO<Pendencia> implements CRUDDao<Pendencia>, CRUDDaoEager<Pendencia> {
	
	private GrupoTipo grupoTipo;
	
	public CRUDDaoPendencia(GrupoTipo grupoTipo) {
		this.grupoTipo = grupoTipo;
	}

	@Override
	protected Class<Pendencia> getModelClass() {
		return Pendencia.class;
	}

	
	@Override
	public List<Pendencia> list(Parameter... params) throws AppException {
		String queryStr = (String) params[0].getValue();
		Boolean includeDoneItems = (Boolean) params[1].getValue();
		
		CRUDDaoEstado daoEstado = new CRUDDaoEstado();
		List<Estado> estadoList = daoEstado.getEstadoList(includeDoneItems);
		
		Integer queryInt = null;
		try {
			queryInt = Integer.valueOf(queryStr);
		} catch (NumberFormatException e) {
			AppLogger.logDebugMessage("QueryStr is not a number: " + queryStr);
		}
		
		// Cria uma lista de usuários que têm o nome incluído na lista.
//		if (!queryStr.isEmpty()) {
//			@SuppressWarnings("unchecked")
//			List<User> userList = (List<User>) AppSecurity.getInstance().getUserList();
//			List<String> loginsFromName = new ArrayList<String>();
//			for (User user : userList) {
//				if (user.getName().toLowerCase().contains(queryStr.toLowerCase())) {
//					loginsFromName.add(user.getName().toLowerCase());
//				}
//			}
//		}
		
		queryStr = queryStr.toLowerCase();
		queryStr = "%" + queryStr + "%";
		queryStr = queryStr.replaceAll("\\*", "%");
		queryStr = queryStr.replaceAll(" ", "%");

		CriteriaBuilder  cb = getSession().getCriteriaBuilder();
		CriteriaQuery<Pendencia> query = cb.createQuery(Pendencia.class);
		Root<Pendencia> root = query.from(Pendencia.class);
		Join<Pendencia, Estado> estado = root.join("estado");
		Join<Pendencia, Grupo> grupo = root.join("grupo");
		Join<Pendencia, Usuario> responsavel = root.join("responsavel");
	    query.select(root)
        	.where(
        		cb.equal(grupo.get("grupoTipo"), this.grupoTipo),
        		root.get("estado").in(estadoList),
        		cb.or(
	                cb.like(cb.lower(root.get("descricao")), queryStr),
	                cb.like(cb.lower(root.get("nome")), queryStr),
	                cb.like(cb.lower(estado.get("nome")), queryStr),
	                cb.like(cb.lower(grupo.get("nome")), queryStr),
	                cb.like(cb.lower(responsavel.get("login")), queryStr),
	                cb.equal(root.get("id"), queryInt)))
        	.orderBy(cb.asc(root.get("id")), cb.asc(root.get("nome")));
		
		List<Pendencia> pendencias =  getSession().createQuery(query).getResultList();		
				
		// Inicializa a lista de acompanhamentos.
		for (Pendencia pendencia : pendencias) {
			Hibernate.initialize(pendencia.getAcompanhamentoList());
		}
		
		closeSession();
		
		return pendencias;
	}

	@Override
	public Pendencia openEager(Pendencia model) {
		Pendencia pendencia = (Pendencia) model;
		pendencia = (Pendencia) getSession().get(pendencia.getClass(), pendencia.getId());
		
		Hibernate.initialize(pendencia.getAcompanhamentoList());
		Hibernate.initialize(pendencia.getDocumentoList());
		Hibernate.initialize(pendencia.getRegistroList());
		Hibernate.initialize(pendencia.getEstado().getPapeis());
		Hibernate.initialize(pendencia.getEstado().getEstadoProcedenteList());
		Hibernate.initialize(pendencia.getResponsavel().getPapelList());
		Hibernate.initialize(pendencia.getResponsavel().getGrupoList());
		Hibernate.initialize(pendencia.getAreasEnvolvidas());
		Hibernate.initialize(pendencia.getAnexoList());
		Hibernate.initialize(pendencia.getSetoresDeApoio());
		if (pendencia.getResponsavel2() != null) {
			Hibernate.initialize(pendencia.getResponsavel2().getPapelList());
			Hibernate.initialize(pendencia.getResponsavel2().getGrupoList());
		}
		
		for (Estado estado : pendencia.getEstado().getEstadoProcedenteList()) {
			Hibernate.initialize(estado.getPapeis());
		}
		
		closeSession();
		
		return pendencia;
	}

}
