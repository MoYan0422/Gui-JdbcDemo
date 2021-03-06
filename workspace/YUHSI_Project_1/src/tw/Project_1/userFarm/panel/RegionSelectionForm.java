package tw.Project_1.userFarm.panel;

import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tw.Project_1.material.HospitalDao;

public class RegionSelectionForm {
	private HospitalDao hospitalDao;
	private JPanel distJPanel;
	private JComboBox<String> cityComboBox;
	private JComboBox<String> distComboBox;
	private String allDiatOptionName;

	public RegionSelectionForm(HospitalDao hospitalDao, String allDiatOptionName) {
		this.hospitalDao = hospitalDao;
		this.allDiatOptionName = allDiatOptionName;
	}

	public JPanel getDistJPanel() {

		distJPanel = new JPanel();
		distJPanel.setLayout(new FlowLayout(0));
		// 縣市欄位
		JLabel cityLabel = new JLabel("縣市 : ");
		cityComboBox = cityLabel();
		// cityComboBox.setSelectedIndex(1);

		// 地區欄位
		JLabel distLabel = new JLabel("    地區 : ");
		distComboBox = new JComboBox<String>();
		this.setDistItems(allDiatOptionName);
		// distComboBox.addItem("請先選縣市");
		// distComboBox.setSelectedIndex(-1);

		cityComboBox.addActionListener((e) -> {
			this.setDistItems(allDiatOptionName);
		});
		distJPanel.add(cityLabel);
		distJPanel.add(cityComboBox);
		distJPanel.add(distLabel);
		distJPanel.add(distComboBox);

		return distJPanel;
	}

	public JComboBox<String> getCityComboBox() {
		return cityComboBox;
	}
	public JComboBox<String> getCityComboBox(String city) {
		cityComboBox.setSelectedItem(city);
		return cityComboBox;
	}
	

	public JComboBox<String> getDistComboBox() {
		return distComboBox;
	}
	public JComboBox<String> getDistComboBox(String dist) {
		setDistItems(null);
		distComboBox.setSelectedItem(dist);
		return distComboBox;
	}

	private JComboBox<String> cityLabel() {
		JComboBox<String> cityComboBox = new JComboBox<String>();

		cityComboBox.addItem("臺北市");
		cityComboBox.addItem("新北市");
		cityComboBox.addItem("基隆市");
		cityComboBox.addItem("桃園市");
		cityComboBox.addItem("新竹市");
		cityComboBox.addItem("新竹縣");
		cityComboBox.addItem("宜蘭縣");
		cityComboBox.addItem("苗栗縣");
		cityComboBox.addItem("臺中市");
		cityComboBox.addItem("彰化縣");
		cityComboBox.addItem("南投縣");
		cityComboBox.addItem("雲林縣");
		cityComboBox.addItem("嘉義市");
		cityComboBox.addItem("嘉義縣");
		cityComboBox.addItem("臺南市");
		cityComboBox.addItem("高雄市");
		cityComboBox.addItem("屏東縣");
		cityComboBox.addItem("臺東縣");
		cityComboBox.addItem("花蓮縣");
		cityComboBox.addItem("澎湖縣");
		cityComboBox.addItem("連江縣");
		cityComboBox.addItem("金門縣");
		
		
		
		
		
		
		
		
		
		
		
		return cityComboBox;
	}

	private void setDistItems(String allDiatOptionName) {
		distComboBox.removeAllItems();
		String cityString = (String) cityComboBox.getSelectedItem();
		try {
			if (hospitalDao.connectionIsClosed()) {
				hospitalDao.openSqlConnection();
			}
			List<String> selectDLists = hospitalDao.selectDLists(cityString);
			for (String distName : selectDLists) {
				distName = distName.replaceAll(" ", "");
				distComboBox.addItem(distName);
			}
			if (allDiatOptionName != null) {
				distComboBox.addItem(allDiatOptionName);
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
