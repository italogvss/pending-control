package itaipu.gss.cpo.view;

import java.awt.Dimension;
import java.awt.Window;
import java.util.List;

import itaipu.gss.cpo.model.Pendencia;
import itaipu.gss.framework.db.Parameter;
import itaipu.gss.framework.exception.AppException;
import itaipu.gss.framework.gui.ButtonMaximize;
import itaipu.gss.framework.gui.CRUDSearchDialog;
import itaipu.gss.framework.gui.CheckBox;
import itaipu.gss.framework.gui.TableModel;

@SuppressWarnings("serial")
public class CRUDSearchDialogPendencia extends CRUDSearchDialog<Pendencia> {

	private CheckBox cbxItensConcluidos;

	public CRUDSearchDialogPendencia(Window parent, String title) {
		super(parent, title);
		initComponents();
	}
	
	private void initComponents() {
		cbxItensConcluidos = new CheckBox("Exibir itens concluídos");
		cbxItensConcluidos.setSelected(false);
		addFilter(cbxItensConcluidos);
		
		addToButtonsPanel(new ButtonMaximize(this));
	}

	@Override
	protected TableModel<Pendencia> buildTableModel() {
		return new TableModelPendencia();
	}

	@Override
	protected String getButtonNewModelName() {
		return new String("Registrar Pendência");
	}
	
	@Override
	protected Dimension getDimension() {
		return new Dimension(1300, 700);
	}
	
	@Override
    protected List<Pendencia> actionSearch() throws AppException {
        try {
            return getObserver().search(
                    new Parameter("searchStr", getSearchStr()),
                    new Parameter("includeDoneItems", cbxItensConcluidos.isSelected())
            		);                
        } catch (AppException e ) {
            throw e;
        }
    }

}
