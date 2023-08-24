package itaipu.gss.cpo.view;

import java.util.Comparator;

import javax.swing.JLabel;

import itaipu.gss.cpo.model.Pendencia;
import itaipu.gss.framework.gui.TableColumnProperties;
import itaipu.gss.framework.gui.TableModel;
import itaipu.gss.framework.gui.TableModelRowComparatorDateTime;
import itaipu.gss.framework.locale.Formatter;

@SuppressWarnings("serial")
public class TableModelPendencia extends TableModel<Pendencia> {

	@Override
	protected String[] createColumnLabels() {
		return new String[] { 
				"#", 
				"Título", 
				"Emissão", 
				"Grupo", 
				"Responsável",
				"Acompanhamento",
				"Estado" 
			};
	}

	@Override
	protected TableColumnProperties[] getTableColumnProperties() {
		return new TableColumnProperties[] {
				new TableColumnProperties(0, 60, JLabel.CENTER),
				new TableColumnProperties(2, 100, JLabel.CENTER),
				new TableColumnProperties(3, 150, JLabel.CENTER),
				new TableColumnProperties(4, 200, JLabel.CENTER),
				new TableColumnProperties(5, 120, JLabel.CENTER),
				new TableColumnProperties(6, 150, JLabel.CENTER),
			};
	}

	@Override
	public Object getValueAt(Pendencia model, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return model.getId();
		case 1:
			return model.getNome();
		case 2:
			return Formatter.dateToString(model.getEmissao());
		case 3:
			return model.getGrupo().getNome();
		case 4:
			return model.getResponsavel().getNome();
		case 5:
			return model.getUltimoAcompanhamento() != null ? Formatter.dateToString(model.getUltimoAcompanhamento().getData()) : "";
		case 6:
			return model.getEstado();
		default:
			return new String();
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Integer.class;
		} else {
			return Object.class;
		}
	}

	//TODO: parse antes do compare e throws exception.
	public Comparator<?> getRowSorterComparatorAt(int columnIndex) {
		if (columnIndex == 2 || columnIndex == 5) {
			return new TableModelRowComparatorDateTime(columnIndex);
		} else {
			return null;
		}
	}

}
