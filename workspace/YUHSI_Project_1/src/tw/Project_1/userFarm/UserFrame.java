package tw.Project_1.userFarm;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;

import tw.Project_1.material.Hospital;
import tw.Project_1.material.HospitalDao;
import tw.Project_1.material.fileHandling.DataReadingAndWriting;
import tw.Project_1.userFarm.panel.RegionSelectionForm;
import tw.Project_1.userFarm.panel.LogJPanel;
import tw.Project_1.userFarm.panel.VisibleJpanel;

public class UserFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Hospital> hospitals = null;
	private HospitalDao hospitalDao;
	private VisibleJpanel visible;
	private DataReadingAndWriting dataReadingAndWriting;
	// private SelectStringJDialog selectStringJDialog ;

	public List<Hospital> getHospitals() {
		return this.hospitals;
	}

	public void setHospitals(List<Hospital> list) {
		this.hospitals = list;
	}

	public UserFrame() {
		this.hospitalDao = new HospitalDao();
		this.dataReadingAndWriting = new DataReadingAndWriting();
		visible = new VisibleJpanel(this.hospitalDao);
		createFrame();
	}

//=======================================================
	//
	private void createFrame() {
		Container contentPane = this.getContentPane();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout(1, 5, 5));
		//

		contentPane.add(keyWord());
		contentPane.add(visible());
		//
		this.setSize(1400, 770);
		this.setLocationRelativeTo(null);
		this.setTitle("公費疫苗施打處資料後台管理系統");
		this.setResizable(false);
		this.setVisible(true);
		openSqlConnection();
	}

//=======================================================
//=======================================================
	// 右邊視窗，顯示
	private JPanel visible() {

		JPanel createJPanel = visible.createJPanel();
		return createJPanel;
	}

	// 左邊視窗,包含查詢功能與Log功能
	private JPanel keyWord() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(350, 800));

		LogJPanel logJPanel = new LogJPanel(this);
		JPanel createLogJPanel = logJPanel.createLogJPanel();
		panel.add(this.allKeyWord());
		panel.add(createLogJPanel);

		return panel;
	}

//=======================================================
//=======================================================
	public JPanel allKeyWord() {
		JPanel panel = new JPanel();
		Border blackline = BorderFactory.createLineBorder(Color.gray);
		panel.setBorder(blackline);

		// 默認尺寸
		panel.setPreferredSize(new Dimension(350, 200));
		panel.add(keyWordTitlePanel());
		panel.add(keyWordpDistPane());
		panel.add(keyPanelNamePanel());
		panel.add(keyWordPanelforExport());
		panel.setVisible(true);
		return panel;
	}

	// ===================================================
	// 標題ok
	// ===================================================
	private JPanel keyWordTitlePanel() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout(0));
		// jPanel.setPreferredSize(new Dimension(345, 30));
		// jPanel.setPreferredSize(new Dimension(350, 15));
		JLabel jLabel = new JLabel("查詢系統");
		jPanel.add(jLabel);
		return jPanel;
	}

	// ===================================================
	// 地區查詢ok
	// ===================================================
	private JPanel keyWordpDistPane() {
		RegionSelectionForm regionSelectionForm = new RegionSelectionForm(hospitalDao, "查所有地區");
		JPanel panel = regionSelectionForm.getDistJPanel();
		panel.setPreferredSize(new Dimension(345, 30));
		panel.setLayout(new FlowLayout(0));
		JComboBox<String> cityComboBox = regionSelectionForm.getCityComboBox();
		JComboBox<String> distComboBox = regionSelectionForm.getDistComboBox();
		//
		JButton jButton = new JButton("查詢");
		jButton.addActionListener((e) -> {
			String cityString = (String) cityComboBox.getSelectedItem();
			String distString = (String) distComboBox.getSelectedItem();
			if (cityString != null && distString != null) {
				try {
					if (distString.equals("查所有地區")) {
						List<Hospital> Hospitals = hospitalDao.selectHospitals(cityString, "all");
						visible.changeJlist(Hospitals);
					} else {
						List<Hospital> Hospitals = hospitalDao.selectHospitals(cityString, distString);
						visible.changeJlist(Hospitals);
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			} else {

				JOptionPane.showMessageDialog(null, "請選擇縣市地區", "查詢錯誤", JOptionPane.INFORMATION_MESSAGE);
			}

		});
		//

		panel.add(jButton);

		return panel;
	}

	// ===================================================
	// 診所名稱查詢ok
	// ===================================================
	private JPanel keyPanelNamePanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(345, 30));
		panel.setLayout(new FlowLayout(0));
		panel.setFont(new FontUIResource("新細明體", Font.ITALIC, 20));

		JLabel hospitalName = new JLabel("診所名稱 : ");
		JTextField hospitalTextField = new JTextField(17);
		JButton inquireButton = new JButton("查詢");
		inquireButton.addActionListener((e) -> {
			try {

				String text = hospitalTextField.getText();
				if (text != null) {
					List<Hospital> selectHospitals = this.hospitalDao.selectHospitals(text);
					if (selectHospitals.size() > 0) {
						visible.changeJlist(selectHospitals);
					} else {
						JOptionPane.showMessageDialog(null, "查無該筆資料", "查詢錯誤", JOptionPane.INFORMATION_MESSAGE);
					}

				} else {
					JOptionPane.showMessageDialog(null, "請輸入需要查詢找所名稱", "查詢錯誤", JOptionPane.INFORMATION_MESSAGE);
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		});
		panel.add(hospitalName);
		panel.add(hospitalTextField);
		panel.add(inquireButton);

		return panel;

	}

	// ===================================================
	// 匯出ok
	// ===================================================
	private JPanel keyWordPanelforExport() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(0));
		panel.setPreferredSize(new Dimension(345, 30));

		JLabel hospitalName = new JLabel("匯出選取資料 : ");
		JButton csv = new JButton("csv檔案");
		csv.addActionListener((e) -> {
			List<Hospital> listSelectionListeners = visible.getListSelectionListeners();
			if (listSelectionListeners.size() > 0) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setSelectedFile(new File("test.csv"));
				jFileChooser.showSaveDialog(null);
				File selectedFile = jFileChooser.getSelectedFile();
				if (selectedFile != null) {
					try {
						dataReadingAndWriting.exportCsvForHospitals(listSelectionListeners, selectedFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			} else {
				JOptionPane.showMessageDialog(null, "請至少要選一項資料", "查詢錯誤", JOptionPane.INFORMATION_MESSAGE);
			}

		});
		JButton json = new JButton("Json檔案");
		json.addActionListener((e) -> {
			List<Hospital> listSelectionListeners = visible.getListSelectionListeners();
			if (listSelectionListeners.size() > 0) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setSelectedFile(new File("test.json"));
				jFileChooser.showSaveDialog(null);
				File selectedFile = jFileChooser.getSelectedFile();

				if (selectedFile != null) {
					try {
						dataReadingAndWriting.exportJsonForHospital(listSelectionListeners, selectedFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			} else {
				JOptionPane.showMessageDialog(null, "請至少要選一項資料", "查詢錯誤", JOptionPane.INFORMATION_MESSAGE);
			}

		});

		panel.add(hospitalName, BorderLayout.WEST);
		panel.add(csv, BorderLayout.WEST);
		panel.add(json, BorderLayout.WEST);
		return panel;

	}

//=====================================================================================
	private void openSqlConnection() {
		try {
			if (hospitalDao.connectionIsClosed()) {
				hospitalDao.openSqlConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// ================繼承方法改寫==========================================================

	@Override
	public void setDefaultCloseOperation(int operation) {
		try {
			this.hospitalDao.closeSqlConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		super.setDefaultCloseOperation(operation);
	}

	
	public HospitalDao getHospitalDao() {
		return hospitalDao;
	}

	public VisibleJpanel getVisible() {
		return visible;
	}

}
