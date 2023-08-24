package itaipu.gss.cpo.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import org.jdatepicker.impl.DateFormatter;
import org.jdatepicker.impl.DateLabelFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import itaipu.gss.cpo.controller.CRUDControllerArea;
import itaipu.gss.cpo.controller.CRUDControllerGrupo;
import itaipu.gss.cpo.controller.CRUDControllerPendencia;
import itaipu.gss.cpo.controller.CRUDControllerSetor;
import itaipu.gss.cpo.controller.CRUDControllerUsuario;
import itaipu.gss.cpo.model.Acompanhamento;
import itaipu.gss.cpo.model.Anexo;
import itaipu.gss.cpo.model.Area;
import itaipu.gss.cpo.model.Documento;
import itaipu.gss.cpo.model.Estado;
import itaipu.gss.cpo.model.Frequencia;
import itaipu.gss.cpo.model.Grupo;
import itaipu.gss.cpo.model.GrupoTipo;
import itaipu.gss.cpo.model.Pendencia;
import itaipu.gss.cpo.model.Prioridade;
import itaipu.gss.cpo.model.Registro;
import itaipu.gss.cpo.model.Setor;
import itaipu.gss.cpo.model.Usuario;
import itaipu.gss.framework.exception.AppException;
import itaipu.gss.framework.exception.UserInputException;
import itaipu.gss.framework.gui.AppController;
import itaipu.gss.framework.gui.CRUDDialog;
import itaipu.gss.framework.gui.CRUDSearch;
import itaipu.gss.framework.gui.CRUDTableDialog;
import itaipu.gss.framework.gui.CRUDTableListener;
import itaipu.gss.framework.gui.CRUDTableSearchDialog;
import itaipu.gss.framework.gui.ComboBox;
import itaipu.gss.framework.gui.ObjectSelector;
import itaipu.gss.framework.gui.TableScrollPane;
import itaipu.gss.framework.gui.TextEditor;
import itaipu.gss.framework.gui.TextField;
import itaipu.gss.framework.log.AppLogger;
//import itaipu.gss.tsdbexplorer.view.TableModelSetor;

@SuppressWarnings("serial")
public class CRUDDialogPendencia extends CRUDDialog<Pendencia> {

	private TextField nomeTextField;
	private TextEditor descricaoTextEditor;
	private ObjectSelectorResponsavel responsavelObjectSelector;
	private ObjectSelectorResponsavel responsavelObjectSelector2;
	private CRUDTableDialog<Documento> documentoListCrudTableDialog;
	private CRUDTableSearchDialog<Setor> setoresDeApoioCrudTableSearchDialog;
	private CRUDTableDialog<Acompanhamento> acompanhamentoListCrudTableDialog;
	private CRUDTableDialog<Anexo> anexoListCrudTableDialog;
	private TableScrollPane<Registro> registroListTable;
	private ComboBox<Estado> estadoComboBox;
	private JLabel grupoLabel;
	private ObjectSelector<Grupo> grupoObjectSelector;
	private JTabbedPane tabbedPane;
	private JLabel setorResponsavelLabel;
	private ObjectSelector<Setor> setorResponsavelObjectSelector;
	private JLabel localizacaoLabel;
	private TextField localizacaoTextField;
	private ComboBox<Frequencia> frequenciaObjectSelector;
	private JLabel frequenciaLabel;
	private CRUDTableSearchDialog<Area> areasEnvolvidasCrudTableSearchDialog;
	private JDatePickerImpl emissaoDatePicker;
	private JDatePanelImpl emissaoDatePanel;
	private JDatePickerImpl previsaoConclusaoDatePicker;
	private JDatePanelImpl previsaoConclusaoDatePanel;
	private JPanel panel;
	private Usuario usuario;
	private JLabel lblDataPrevistaDe;
	private JPanel panelPrevisaoConclusao;
	private JLabel lblPrioridade;
	private ComboBox<Prioridade> cbPrioridade;
	private Component rigidArea;

	private String title;
	private JLabel label;
	private JLabel label_1;

	public CRUDDialogPendencia(Window parent, List<Frequencia> frequenciaList, Usuario usuario, GrupoTipo grupoTipo,
			List<Prioridade> prioridadeList, String titulo) {
		super(parent);
		this.usuario = usuario;
		this.title = titulo;

		initComponents(frequenciaList, grupoTipo, prioridadeList, parent);
	}

	private void initComponents(List<Frequencia> frequenciaList, GrupoTipo grupoTipo, List<Prioridade> prioridadeList,
			Window parent) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 170, 170, 29, 118, 175 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 32, 0, 0, 0, 30, 30, 0, 25, 0, 145, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		getMainPanel().setLayout(gridBagLayout);

		JLabel nomeLabel = new JLabel("Título:");
		GridBagConstraints gbc_nomeLabel = new GridBagConstraints();
		gbc_nomeLabel.anchor = GridBagConstraints.WEST;
		gbc_nomeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nomeLabel.gridx = 0;
		gbc_nomeLabel.gridy = 0;
		getMainPanel().add(nomeLabel, gbc_nomeLabel);

		this.nomeTextField = new TextField();
		GridBagConstraints gbc_nomeTextField = new GridBagConstraints();
		gbc_nomeTextField.gridwidth = 5;
		gbc_nomeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nomeTextField.insets = new Insets(0, 0, 5, 0);
		gbc_nomeTextField.gridx = 0;
		gbc_nomeTextField.gridy = 1;
		getMainPanel().add(nomeTextField, gbc_nomeTextField);

		UtilDateModel dateModel = new UtilDateModel();
		dateModel.setValue(Calendar.getInstance().getTime());
		Properties languageProperties = new Properties();
		try {
			languageProperties.load(getClass().getResourceAsStream("/org/jdatepicker/i18n/Text_pt.properties"));
		} catch (IOException e1) {
			AppLogger.logWarningMessage("Could not load JDatePicker i18n properties file on DialogSelectDate class.");
		}
		emissaoDatePanel = new JDatePanelImpl(dateModel, languageProperties, new Locale("pt", "BR"));

		JLabel emissaoLabel = new JLabel("Data de emiss\u00E3o:");
		GridBagConstraints gbc_emissaoLabel = new GridBagConstraints();
		gbc_emissaoLabel.anchor = GridBagConstraints.WEST;
		gbc_emissaoLabel.insets = new Insets(0, 0, 5, 5);
		gbc_emissaoLabel.gridx = 0;
		gbc_emissaoLabel.gridy = 2;
		getMainPanel().add(emissaoLabel, gbc_emissaoLabel);
		previsaoConclusaoDatePanel = new JDatePanelImpl(new UtilDateModel(), languageProperties,
				new Locale("pt", "BR"));

		lblDataPrevistaDe = new JLabel("Data Prevista Conclus\u00E3o:");
		GridBagConstraints gbc_lblDataPrevistaDe = new GridBagConstraints();
		gbc_lblDataPrevistaDe.anchor = GridBagConstraints.WEST;
		gbc_lblDataPrevistaDe.insets = new Insets(0, 0, 5, 5);
		gbc_lblDataPrevistaDe.gridx = 1;
		gbc_lblDataPrevistaDe.gridy = 2;
		getMainPanel().add(lblDataPrevistaDe, gbc_lblDataPrevistaDe);

		lblPrioridade = new JLabel("Prioridade:");
		GridBagConstraints gbc_lblPrioridade = new GridBagConstraints();
		gbc_lblPrioridade.anchor = GridBagConstraints.WEST;
		gbc_lblPrioridade.gridwidth = 2;
		gbc_lblPrioridade.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrioridade.gridx = 3;
		gbc_lblPrioridade.gridy = 2;
		getMainPanel().add(lblPrioridade, gbc_lblPrioridade);
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		getMainPanel().add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		emissaoDatePicker = new JDatePickerImpl(emissaoDatePanel, new DateLabelFormatter());
		panel.add(emissaoDatePicker);
		emissaoDatePicker.setTextEditable(true);

		previsaoConclusaoDatePicker = new JDatePickerImpl(previsaoConclusaoDatePanel, new DateFormatter());
		previsaoConclusaoDatePicker.setTextEditable(true);
		panelPrevisaoConclusao = new JPanel();
		GridBagConstraints gbc_panelPrevisaoConclusao = new GridBagConstraints();
		gbc_panelPrevisaoConclusao.insets = new Insets(0, 0, 5, 5);
		gbc_panelPrevisaoConclusao.fill = GridBagConstraints.BOTH;
		gbc_panelPrevisaoConclusao.gridx = 1;
		gbc_panelPrevisaoConclusao.gridy = 3;
		getMainPanel().add(panelPrevisaoConclusao, gbc_panelPrevisaoConclusao);
		panelPrevisaoConclusao.setLayout(new BoxLayout(panelPrevisaoConclusao, BoxLayout.X_AXIS));
		panelPrevisaoConclusao.add(previsaoConclusaoDatePicker);

		cbPrioridade = new ComboBox<>();
		cbPrioridade.setItems(prioridadeList);
		GridBagConstraints gbc_cbPrioridade = new GridBagConstraints();
		gbc_cbPrioridade.gridwidth = 2;
		gbc_cbPrioridade.insets = new Insets(0, 0, 5, 0);
		gbc_cbPrioridade.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbPrioridade.gridx = 3;
		gbc_cbPrioridade.gridy = 3;
		getMainPanel().add(cbPrioridade, gbc_cbPrioridade);

		setorResponsavelLabel = new JLabel("Setor respons\u00E1vel:");
		GridBagConstraints gbc_setorResponsavelLabel = new GridBagConstraints();
		gbc_setorResponsavelLabel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_setorResponsavelLabel.insets = new Insets(0, 0, 5, 5);
		gbc_setorResponsavelLabel.gridx = 0;
		gbc_setorResponsavelLabel.gridy = 4;
		getMainPanel().add(setorResponsavelLabel, gbc_setorResponsavelLabel);

		localizacaoLabel = new JLabel("Cód. Localiza\u00E7\u00E3o SOM:");
		GridBagConstraints gbc_localizacaoLabel = new GridBagConstraints();
		gbc_localizacaoLabel.gridwidth = 2;
		gbc_localizacaoLabel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_localizacaoLabel.insets = new Insets(0, 0, 5, 0);
		gbc_localizacaoLabel.gridx = 3;
		gbc_localizacaoLabel.gridy = 4;
		getMainPanel().add(localizacaoLabel, gbc_localizacaoLabel);

		setorResponsavelObjectSelector = new ObjectSelector<Setor>(new CRUDControllerSetor(), parent);
		GridBagConstraints gbc_setorResponsavelObjectSelector = new GridBagConstraints();
		gbc_setorResponsavelObjectSelector.gridwidth = 2;
		gbc_setorResponsavelObjectSelector.insets = new Insets(0, 0, 5, 5);
		gbc_setorResponsavelObjectSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_setorResponsavelObjectSelector.gridx = 0;
		gbc_setorResponsavelObjectSelector.gridy = 5;
		getMainPanel().add(setorResponsavelObjectSelector, gbc_setorResponsavelObjectSelector);
		this.setorResponsavelObjectSelector.disableButtonNewModel();

		localizacaoTextField = new TextField();
		GridBagConstraints gbc_localizacaoObjectSelector = new GridBagConstraints();
		gbc_localizacaoObjectSelector.gridwidth = 2;
		gbc_localizacaoObjectSelector.insets = new Insets(0, 0, 5, 0);
		gbc_localizacaoObjectSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_localizacaoObjectSelector.gridx = 3;
		gbc_localizacaoObjectSelector.gridy = 5;
		getMainPanel().add(localizacaoTextField, gbc_localizacaoObjectSelector);

		grupoLabel = new JLabel("Grupo do Respons\u00E1vel:");
		GridBagConstraints gbc_grupoLabel = new GridBagConstraints();
		gbc_grupoLabel.anchor = GridBagConstraints.WEST;
		gbc_grupoLabel.insets = new Insets(0, 0, 5, 5);
		gbc_grupoLabel.gridx = 0;
		gbc_grupoLabel.gridy = 6;
		getMainPanel().add(grupoLabel, gbc_grupoLabel);

		JLabel responsavelLabel = new JLabel("Respons\u00E1veis:");
		GridBagConstraints gbc_responsavelLabel = new GridBagConstraints();
		gbc_responsavelLabel.gridwidth = 2;
		gbc_responsavelLabel.anchor = GridBagConstraints.WEST;
		gbc_responsavelLabel.insets = new Insets(0, 0, 5, 0);
		gbc_responsavelLabel.gridx = 3;
		gbc_responsavelLabel.gridy = 6;
		getMainPanel().add(responsavelLabel, gbc_responsavelLabel);

		grupoObjectSelector = new ObjectSelector<Grupo>(new CRUDControllerGrupo() {
			
			@Override
			public List<Grupo> search(String searchText) throws AppException {
				return (List<Grupo>) super.getGrupoUsuarioList(searchText, grupoTipo);
			}
			
		}, parent);
		GridBagConstraints gbc_grupoObjectSelector = new GridBagConstraints();
		gbc_grupoObjectSelector.gridwidth = 2;
		gbc_grupoObjectSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_grupoObjectSelector.insets = new Insets(0, 0, 5, 5);
		gbc_grupoObjectSelector.gridx = 0;
		gbc_grupoObjectSelector.gridy = 7;
		getMainPanel().add(grupoObjectSelector, gbc_grupoObjectSelector);
		this.grupoObjectSelector.disableButtonNewModel();

		label = new JLabel("1:");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 7;
		getMainPanel().add(label, gbc_label);

		this.responsavelObjectSelector = new ObjectSelectorResponsavel(new CRUDControllerUsuario() {
			@Override
			public List<Usuario> search(String searchText) throws AppException {
				Grupo grupo = CRUDDialogPendencia.this.grupoObjectSelector.getObject();
				return super.searchByGroup(searchText, grupo);
			}
		}, this);

		GridBagConstraints gbc_responsavelObjectSelector = new GridBagConstraints();
		gbc_responsavelObjectSelector.gridwidth = 2;
		gbc_responsavelObjectSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_responsavelObjectSelector.insets = new Insets(0, 0, 5, 0);
		gbc_responsavelObjectSelector.gridx = 3;
		gbc_responsavelObjectSelector.gridy = 7;
		getMainPanel().add(responsavelObjectSelector, gbc_responsavelObjectSelector);
		this.responsavelObjectSelector.disableButtonNewModel();

		this.responsavelObjectSelector2 = new ObjectSelectorResponsavel(new CRUDControllerUsuario() {

			@Override
			public List<Usuario> search(String searchText) throws AppException {
				Grupo grupo = CRUDDialogPendencia.this.grupoObjectSelector.getObject();
				return super.searchByGroup(searchText, grupo);
			}
		}, this);
		GridBagConstraints gbc_responsavelObjectSelector2 = new GridBagConstraints();
		gbc_responsavelObjectSelector2.gridwidth = 2;
		gbc_responsavelObjectSelector2.fill = GridBagConstraints.HORIZONTAL;
		gbc_responsavelObjectSelector2.insets = new Insets(0, 0, 5, 0);
		gbc_responsavelObjectSelector2.gridx = 3;
		gbc_responsavelObjectSelector2.gridy = 8;
		getMainPanel().add(responsavelObjectSelector2, gbc_responsavelObjectSelector2);
		this.responsavelObjectSelector.setEnabled(true);
		this.responsavelObjectSelector.setFocusable(true);
		this.requestFocus(true);
		this.responsavelObjectSelector2.disableButtonNewModel();

		label_1 = new JLabel("2:");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 2;
		gbc_label_1.gridy = 8;
		getMainPanel().add(label_1, gbc_label_1);

		JLabel estadoLabel = new JLabel("Estado:");
		GridBagConstraints gbc_estadoLabel = new GridBagConstraints();
		gbc_estadoLabel.anchor = GridBagConstraints.WEST;
		gbc_estadoLabel.insets = new Insets(0, 0, 5, 5);
		gbc_estadoLabel.gridx = 0;
		gbc_estadoLabel.gridy = 9;
		getMainPanel().add(estadoLabel, gbc_estadoLabel);

		frequenciaLabel = new JLabel("Frequ\u00EAncia de Acompanhamento:");
		GridBagConstraints gbc_frequenciaLabel = new GridBagConstraints();
		gbc_frequenciaLabel.gridwidth = 2;
		gbc_frequenciaLabel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_frequenciaLabel.insets = new Insets(0, 0, 5, 0);
		gbc_frequenciaLabel.gridx = 3;
		gbc_frequenciaLabel.gridy = 9;
		getMainPanel().add(frequenciaLabel, gbc_frequenciaLabel);

		this.estadoComboBox = new ComboBox<Estado>();
		GridBagConstraints gbc_estadoComboBox = new GridBagConstraints();
		gbc_estadoComboBox.gridwidth = 2;
		gbc_estadoComboBox.fill = GridBagConstraints.BOTH;
		gbc_estadoComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_estadoComboBox.gridx = 0;
		gbc_estadoComboBox.gridy = 10;
		getMainPanel().add(estadoComboBox, gbc_estadoComboBox);

		frequenciaObjectSelector = new ComboBox<Frequencia>();
		frequenciaObjectSelector.setItems(frequenciaList);
		GridBagConstraints gbc_frequenciaObjectSelector = new GridBagConstraints();
		gbc_frequenciaObjectSelector.gridwidth = 2;
		gbc_frequenciaObjectSelector.insets = new Insets(0, 0, 5, 0);
		gbc_frequenciaObjectSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_frequenciaObjectSelector.gridx = 3;
		gbc_frequenciaObjectSelector.gridy = 10;
		getMainPanel().add(frequenciaObjectSelector, gbc_frequenciaObjectSelector);

		rigidArea = Box.createRigidArea(new Dimension(10, 10));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea.gridx = 0;
		gbc_rigidArea.gridy = 11;
		getMainPanel().add(rigidArea, gbc_rigidArea);

		this.descricaoTextEditor = new TextEditor("Descrição");
		GridBagConstraints descricaoScrollPaneGridBagConstraints = new GridBagConstraints();
		descricaoScrollPaneGridBagConstraints.insets = new Insets(0, 0, 5, 0);
		descricaoScrollPaneGridBagConstraints.gridwidth = 5;
		descricaoScrollPaneGridBagConstraints.fill = GridBagConstraints.BOTH;
		descricaoScrollPaneGridBagConstraints.gridx = 0;
		descricaoScrollPaneGridBagConstraints.gridy = 12;
		getMainPanel().add(descricaoTextEditor, descricaoScrollPaneGridBagConstraints);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMaximumSize(new Dimension(0, 200));
		GridBagConstraints tabbedPaneGridBagConstraints = new GridBagConstraints();
		tabbedPaneGridBagConstraints.gridwidth = 5;
		tabbedPaneGridBagConstraints.fill = GridBagConstraints.BOTH;
		tabbedPaneGridBagConstraints.gridx = 0;
		tabbedPaneGridBagConstraints.gridy = 13;
		getMainPanel().add(tabbedPane, tabbedPaneGridBagConstraints);

		JPanel documentoListPanel = new JPanel();
		JPanel acompanhamentoListPanel = new JPanel();
		JPanel anexoListPanel = new JPanel();
		JPanel registroListPanel = new JPanel();
		JPanel setoresDeApoioListPanel = new JPanel();
		JPanel areasEnvolvidasPanel = new JPanel();

		tabbedPane.addTab(new String("Acompanhamentos"), acompanhamentoListPanel);
		tabbedPane.addTab(new String("Anexos"), anexoListPanel);
		tabbedPane.addTab(new String("Documentos"), documentoListPanel);
		tabbedPane.addTab(new String("Setores de Apoio"), setoresDeApoioListPanel);
		tabbedPane.addTab(new String("Áreas Envolvidas"), areasEnvolvidasPanel);
		tabbedPane.addTab(new String("Histórico"), registroListPanel);

		this.documentoListCrudTableDialog = new CRUDTableDialog<Documento>(new TableModelDocumento(),
				new CRUDDialogDocumento(this));
		this.documentoListCrudTableDialog.disableMoveUpDown();
		documentoListPanel.setLayout(new BoxLayout(documentoListPanel, BoxLayout.X_AXIS));
		documentoListPanel.add(this.documentoListCrudTableDialog);

		this.setoresDeApoioCrudTableSearchDialog = new CRUDTableSearchDialog<Setor>(new TableModelSetor(), parent,
				new CRUDControllerSetor() {

					@Override
					protected CRUDSearch<Setor> buildDialogSearch(Window parent) {
						CRUDSearchDialogSetor crudSearchDialogSetor = new CRUDSearchDialogSetor(parent,
								"Setores de Apoio");
						crudSearchDialogSetor.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
						return crudSearchDialogSetor;
					}

				});
		this.setoresDeApoioCrudTableSearchDialog.setDialog(new CRUDDialogSetor(this));

		this.setoresDeApoioCrudTableSearchDialog.setAllowDuplicates(false);
		this.setoresDeApoioCrudTableSearchDialog.disableMoveUpDown();
		setoresDeApoioListPanel.setLayout(new BoxLayout(setoresDeApoioListPanel, BoxLayout.X_AXIS));
		setoresDeApoioListPanel.add(this.setoresDeApoioCrudTableSearchDialog);
		this.setoresDeApoioCrudTableSearchDialog.disableButtonNewModel();

		this.acompanhamentoListCrudTableDialog = new CRUDTableDialog<Acompanhamento>(new TableModelAcompanhamento(),
				new CRUDDialogAcompanhamento(this));
		this.acompanhamentoListCrudTableDialog.disableMoveUpDown();
		this.acompanhamentoListCrudTableDialog.setListener(new CRUDTableListener() {

			@Override
			public boolean actionRemove(Object object) {
				Acompanhamento acompanhamento = (Acompanhamento) object;
				boolean canDelete = acompanhamento.getId() == null;
				if (!canDelete)
					JOptionPane.showMessageDialog(CRUDDialogPendencia.this,
							"Não é possível apagar um acompanhamento que já foi gravado."
									+ "\nPor favor, edite ou crie um novo acompanhamento.");
				return canDelete;
			}

			@Override
			public boolean actionEdit(Object object) {
				return true;
			}

			@Override
			public boolean actionAdd(Object object) {
				return true;
			}
		});

		acompanhamentoListPanel.setLayout(new BoxLayout(acompanhamentoListPanel, BoxLayout.X_AXIS));
		acompanhamentoListPanel.add(this.acompanhamentoListCrudTableDialog);

		this.anexoListCrudTableDialog = new CRUDTableDialog<Anexo>(new TableModelAnexo(), new CRUDDialogAnexo(this)) {
			@Override
			protected void actionEditClick(Object object) {
				Anexo anexo = (Anexo) object;
				try {
					String fileName = AppController.getInstance().getApplicationName() + "_" + anexo.getId() + ".tmp."
							+ anexo.getTipo().getExtensao().toLowerCase();

					File file = new File(System.getProperty("java.io.tmpdir") + fileName);
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					fileOutputStream.write(anexo.getArquivo());
					Desktop.getDesktop().open(file);
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		this.anexoListCrudTableDialog.disableMoveUpDown();
		anexoListPanel.setLayout(new BorderLayout(0, 0));
		anexoListPanel.add(this.anexoListCrudTableDialog);

		// Esta tabela mostra o histórico de alterações e por isso não tem Dialog de
		// cadastro.
		this.registroListTable = new TableScrollPane<Registro>(new TableModelRegistro());

		registroListPanel.setLayout(new BoxLayout(registroListPanel, BoxLayout.X_AXIS));
		registroListPanel.add(this.registroListTable);

		this.areasEnvolvidasCrudTableSearchDialog = new CRUDTableSearchDialog<Area>(new TableModelArea(), parent,
				new CRUDControllerArea() {

					@Override
					protected CRUDSearch<Area> buildDialogSearch(Window parent) {
						CRUDSearchDialogArea crudSearchDialogArea = new CRUDSearchDialogArea(parent, "Áreas");
						crudSearchDialogArea.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
						return crudSearchDialogArea;
					}

				});

		this.areasEnvolvidasCrudTableSearchDialog.setDialog(new CRUDDialogArea(this));
		this.areasEnvolvidasCrudTableSearchDialog.setAllowDuplicates(false);
		this.areasEnvolvidasCrudTableSearchDialog.disableMoveUpDown();
		areasEnvolvidasPanel.setLayout(new BoxLayout(areasEnvolvidasPanel, BoxLayout.X_AXIS));
		areasEnvolvidasPanel.add(this.areasEnvolvidasCrudTableSearchDialog);
		this.areasEnvolvidasCrudTableSearchDialog.disableButtonNewModel();
	}

	@Override
	protected Dimension getDimension() {
		return new Dimension(700, 700);
	}

	@Override
	protected Pendencia buildModel() {
		return new Pendencia();
	}

	@Override
	protected String getTitulo() {
		return "Pendência";
	}
	
	
	public Object getGrupoResponsavel() {
		return this.grupoObjectSelector.getObject();
	}

	@Override
	protected void loadFields(Pendencia model) {
		this.tabbedPane.setSelectedIndex(0);

		this.nomeTextField.setText(model.getNome());
		this.cbPrioridade.setSelectedItem(model.getPrioridade());
		((UtilDateModel) this.emissaoDatePicker.getModel()).setValue(model.getEmissao());
		((UtilDateModel) this.previsaoConclusaoDatePicker.getModel()).setValue(model.getDataPrevistaConclusao());
		this.responsavelObjectSelector.setSelectedObject(model.getResponsavel());
		this.responsavelObjectSelector2.setSelectedObject(model.getResponsavel2());
		this.areasEnvolvidasCrudTableSearchDialog.setItems(model.getAreasEnvolvidas());
		this.grupoObjectSelector.setSelectedObject(model.getGrupo());
		this.setorResponsavelObjectSelector.setSelectedObject(model.getSetorResponsavel());
		this.localizacaoTextField.setText(model.getLocalizacao());
		this.frequenciaObjectSelector.setSelectedItem(model.getFrequenciaAcompanhamento());

		// Define o primeiro estado de uma pendência.
		if (model.getEstado() == null) {
		
			List<Estado> estadoInicialList = ((CRUDControllerPendencia) getController()).getEstadoInicialList();
			estadoInicialList .forEach(estado -> this.estadoComboBox.addItem(estado));

			if (this.estadoComboBox.getItemCount() > 0) {
				this.estadoComboBox.setSelectedIndex(0);
			} else {
				AppLogger.logErrorMessage("Nenhum estado disponível para a criação de uma nova pendência");
				JOptionPane.showMessageDialog(this,
						"Falha ao carregar os estados da aplicação. Por favor contate o Administrador.");
			}
		} else {
			List<Estado> estadoProcedenteList = new ArrayList<Estado>();
			estadoProcedenteList.add(model.getEstado());
			estadoProcedenteList.addAll(model.getEstado().getEstadoProcedenteList());
			this.estadoComboBox.setItems(estadoProcedenteList);
			this.estadoComboBox.setSelectedItem(model.getEstado());
		}

		this.descricaoTextEditor.setText(model.getDescricao());
		this.documentoListCrudTableDialog.setItems(model.getDocumentoList());
		this.acompanhamentoListCrudTableDialog.setItems(model.getAcompanhamentoList());
		this.anexoListCrudTableDialog.setItems(model.getAnexoList());
		this.registroListTable.setItems(model.getRegistroList());
		this.setoresDeApoioCrudTableSearchDialog.setItems(model.getSetoresDeApoio());

		// O dialog acompanhamento precisa saber quem é a pendencia para saber a
		// frequencia de acompanhamento.
		// E então definir se ainda pode ser alterado.
		((CRUDDialogAcompanhamento) this.acompanhamentoListCrudTableDialog.getDialog()).setPendencia(model);

		// Set Dialog title.
		if (model.getId() != null) {
			this.setTitle(this.title + " #" + model.getId());
		} else {
			this.setTitle(this.title + " - Novo Cadastro");
		}
	}

	@Override
	protected Pendencia actionOk(Pendencia pendencia) throws UserInputException {
		pendencia.setNome(this.nomeTextField.getText());
		pendencia.setPrioridade(cbPrioridade.getSelectedObject());
		pendencia.setEmissao(((UtilDateModel) emissaoDatePicker.getModel()).getValue());
		pendencia.setDataPrevistaConclusao(((UtilDateModel) previsaoConclusaoDatePicker.getModel()).getValue());
		pendencia.setFrequenciaAcompanhamento(this.frequenciaObjectSelector.getSelectedObject());
		pendencia.setSetorResponsavel(this.setorResponsavelObjectSelector.getObject());
		pendencia.setLocalizacao(this.localizacaoTextField.getText());
		pendencia.setGrupo(this.grupoObjectSelector.getObject());
		pendencia.setResponsavel(this.responsavelObjectSelector.getObject());
		pendencia.setResponsavel2(this.responsavelObjectSelector2.getObject());
		pendencia.setEstado(this.estadoComboBox.getSelectedObject(), this.usuario);
		pendencia.setDescricao(this.descricaoTextEditor.getText());
		pendencia.setAcompanhamentoList(this.acompanhamentoListCrudTableDialog.getItems());
		pendencia.setAnexoList(this.anexoListCrudTableDialog.getItems());
		pendencia.setDocumentoList(this.documentoListCrudTableDialog.getItems());
		pendencia.setSetoresDeApoio(this.setoresDeApoioCrudTableSearchDialog.getItems());
		pendencia.setAreasEnvolvidas(this.areasEnvolvidasCrudTableSearchDialog.getItems());

		pendencia.setHasChanges(this.getCRUDWindowListener().hasChanges());

		if (pendencia.hasChanges()) {
			pendencia.addRegistroList(new Registro(pendencia));
		}

		return pendencia;
	}
}
