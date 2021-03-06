package tw.Project_1.userFarm.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import tw.Project_1.material.Hospital;
import tw.Project_1.material.HospitalDao;

public class VisibleJpanel {

	private HospitalDao hospitalDao;

	private JList<Hospital> jList;

	private DefaultListModel<Hospital> defaultListModel;

	public VisibleJpanel(HospitalDao hospitalDao) {
		this.hospitalDao = hospitalDao;
	}

	public JPanel createJPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(1000, 800));

		panel.add(createVisibleJPanel());

		panel.add(creatAdministratorsJPanel());
		return panel;
	}

	private JPanel createVisibleJPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(1000, 650));
		panel.setLayout(new GridLayout(1, 1));

		this.jList = new JList<Hospital>();
		this.defaultListModel = new DefaultListModel<Hospital>();
		this.jList.setModel(this.defaultListModel);
		// hospitalsCangeTOJlist(hospitals);

		// hospitalsCangeTOJlist(this.hospitals);
		//this.jList.setFont(new FontUIResource("新細明體", Font.PLAIN, 20));
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(jList);
		jScrollPane.setPreferredSize(new Dimension(1000, 650));
		panel.add(jScrollPane);

		return panel;

	}

	private JPanel creatAdministratorsJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(1));
		panel.setPreferredSize(new Dimension(1000, 40));
		Border blackline = BorderFactory.createLineBorder(Color.gray);
		panel.setBorder(blackline);

		JButton showALLButton = new JButton("顯示全部資料");
		showALLButton.addActionListener((e) -> {
			try {

				List<Hospital> selectHospitals = this.hospitalDao.selectHospitals();
				this.changeJlist(selectHospitals);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
		panel.add(showALLButton);

		return panel;

	}

	public void changeJlist(List<Hospital> hospitals) {
		// 判斷源欄位是否有數值
		if (!this.defaultListModel.isEmpty()) {
			// 有數值全清掉
			this.defaultListModel.removeAllElements();
			// this.jList.updateUI();
		}
		// 判斷是否有數值再加，不然會有空指針異常
		if (hospitals != null) {
			for (Hospital hospital : hospitals) {
				this.defaultListModel.addElement(hospital);

				// 沒加休眠會報空指針異常，但又突然不會了
				// try {
				// Thread.currentThread().sleep(5);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
	}
	
	public void updateUI() {
		this.jList.updateUI();
	}

	public void intsetItem(Hospital hospital) {
		this.defaultListModel.addElement(hospital);

	}

	public void intsetItems(List<Hospital> hospitals) {
		for (Hospital hospital : hospitals) {
			this.defaultListModel.addElement(hospital);
		}

	}

	public void deleteItem(Hospital hospital) {
		this.defaultListModel.removeElement(hospital);

	}

	public void deleteItems(List<Hospital> hospitals) {
		for (Hospital hospital : hospitals) {
			this.defaultListModel.removeElement(hospital);
		}
	}

	public List<Hospital> getListSelectionListeners() {

		List<Hospital> selectedValuesList = jList.getSelectedValuesList();
		return selectedValuesList;
	}

}
