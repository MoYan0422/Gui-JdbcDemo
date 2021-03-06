package tw.Project_1.userFarm.panel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;

import tw.Project_1.material.Hospital;
import tw.Project_1.material.HospitalDao;
import tw.Project_1.material.fileHandling.DataReadingAndWriting;
import tw.Project_1.material.fileHandling.MyFileFilter;
import tw.Project_1.userFarm.UserFrame;

public class LogJPanel {
	private UserFrame userFrame;
	private HospitalDao hospitalDao;
	private VisibleJpanel visible;

	public LogJPanel(UserFrame userFrame) {
		this.userFrame = userFrame;
		this.hospitalDao = userFrame.getHospitalDao();
		this.visible = userFrame.getVisible();
	}

//===========================================================	
	public JPanel createLogJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(1, 0, 0));
		panel.setPreferredSize(new Dimension(350, 500));

		panel.add(insertOneJPanel());
		panel.add(updataJPanel());

		return panel;
	}

//===========================================================		
	private JPanel insertOneJPanel() {
		JPanel insertOneJPanel = new JPanel();
		insertOneJPanel.setLayout(new FlowLayout(1));
		insertOneJPanel.setPreferredSize(new Dimension(350, 450));
		Border blackline = BorderFactory.createLineBorder(Color.gray);
		insertOneJPanel.setBorder(blackline);
		//
		// ===================================================
		// 標題欄位
		// ===================================================
		JPanel titleJPanel = new JPanel();
		titleJPanel.setLayout(new FlowLayout(1));
		JLabel titleJLabel = new JLabel("新增資料系統");
		titleJPanel.add(titleJLabel);
		//
		// ===================================================
		// 欄位加入主要面板區
		// ===================================================

		insertOneJPanel.add(titleJPanel);
		insertOneJPanel.add(insertHospitalJDialog());
		return insertOneJPanel;

	}

// ===========================================================
	public JPanel insertHospitalJDialog() {

		JPanel insertHospitalJPanel = new JPanel();

		insertHospitalJPanel.setLayout(new FlowLayout(0));
		insertHospitalJPanel.setPreferredSize(new Dimension(348, 250));
		//
		// ===================================================
		// 名稱欄位
		// ===================================================
		JPanel nameJPanel = new JPanel();
		nameJPanel.setLayout(new FlowLayout(0));
		JLabel nemeJLabel = new JLabel("名稱 : ");
		JTextField nameJTextField = new JTextField(15);
		nameJPanel.add(nemeJLabel);
		nameJPanel.add(nameJTextField);
		//
		// ===================================================
		// 電話欄位
		// ===================================================
		JPanel phoneNumberJPanel = new JPanel();
		phoneNumberJPanel.setLayout(new FlowLayout(0));
		JLabel phoneNumberJLabel = new JLabel("電話 : ");
		JLabel distNumberJLabel = new JLabel("-");
		JComboBox<String> distNumberJComboBox = distNumber();
		JTextField phoneNumberJTextField = new JTextField(10);
		phoneNumberJPanel.add(phoneNumberJLabel);
		phoneNumberJPanel.add(distNumberJComboBox);
		phoneNumberJPanel.add(distNumberJLabel);
		phoneNumberJPanel.add(phoneNumberJTextField);

		// ===================================================
		// 地區欄位
		// ===================================================
		RegionSelectionForm distJP = new RegionSelectionForm(this.hospitalDao, null);
		JPanel distJPanel = distJP.getDistJPanel();
		JComboBox<String> cityComboBox = distJP.getCityComboBox();
		JComboBox<String> distComboBox = distJP.getDistComboBox();
		//
		// ===================================================
		// 地址欄位
		// ===================================================
		JPanel addressJPanel = new JPanel();
		addressJPanel.setLayout(new FlowLayout(0));
		JLabel addressJLabel = new JLabel("地址 : ");
		JTextField addressJTextField = new JTextField(25);
		addressJPanel.add(addressJLabel);
		addressJPanel.add(addressJTextField);
		//
		// ===================================================
		// 按鍵欄位
		// ===================================================
		JPanel insertOneJPanel = new JPanel();
		insertOneJPanel.setLayout(new FlowLayout(0));
		insertOneJPanel.setPreferredSize(new Dimension(300, 40));
		JButton insertOnejButton = new JButton("新增資料");
		insertOnejButton.addActionListener((e) -> {
			// 優化 先取得是否正確再獲取
			String hospitalName = nameJTextField.getText();
			String distNumber = (String) distNumberJComboBox.getSelectedItem();
			String telephoneNumber = phoneNumberJTextField.getText();
			String adderssString = addressJTextField.getText();
			// 需要正則判斷
			if (telephoneNumber.matches("^\\d{7,8}$") && hospitalName.matches("^[\\u4e00-\\u9fa5]{2,}$")
					&& adderssString.matches("^[\\u4E00-\\u9FA5]{3}+[\\u4E00-\\u9FA5A-Za-z0-9]{1,}$")) {

				String cituNameString = (String) cityComboBox.getSelectedItem();
				String distnameString = (String) distComboBox.getSelectedItem();
				String Addr = cituNameString + distnameString + adderssString;

				int yesOrNo = JOptionPane.showConfirmDialog(null, "確認是否新增 : " + nameJTextField.getText(), "新增",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (yesOrNo == 0) {
					Hospital hospital = new Hospital();

					hospital.setCity(cituNameString);
					hospital.setDist(distnameString);
					hospital.setHospitalName(hospitalName);
					hospital.setAddr(Addr);
					hospital.setTelephoneNumber(distNumber + "-" + telephoneNumber);
					LinkedList<Hospital> hospitals = new LinkedList<Hospital>();
					hospitals.add(hospital);
					try {
						hospitalDao.sqlAddHospitals(hospitals);
						visible.intsetItems(hospitals);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}

			} else {
				JOptionPane.showMessageDialog(null, "格式錯誤", "新增", JOptionPane.PLAIN_MESSAGE);
			}

		});
		insertOneJPanel.add(insertOnejButton);
		//
		// ===================================================
		// 按鍵欄位
		// ===================================================
		JPanel insertMoreJPanel = new JPanel();
		insertMoreJPanel.setLayout(new FlowLayout(0));
		insertMoreJPanel.setPreferredSize(new Dimension(300, 40));
		JButton insertMorejbutter = new JButton("匯入多筆資料");
		insertMorejbutter.addActionListener((e) -> {
			JFileChooser jFileChooser = new JFileChooser();
			MyFileFilter myFileFilter = new MyFileFilter();
			jFileChooser.setAcceptAllFileFilterUsed(true);
			jFileChooser.addChoosableFileFilter(myFileFilter);
			jFileChooser.showOpenDialog(null);
			File file = jFileChooser.getSelectedFile();
			if (file != null) {
				DataReadingAndWriting dataReadingAndWriting = new DataReadingAndWriting();
				try {
					dataReadingAndWriting.insertDataForReadFile(file);
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				}

			}

		});
		insertMoreJPanel.add(insertMorejbutter);

		insertHospitalJPanel.add(nameJPanel);
		insertHospitalJPanel.add(phoneNumberJPanel);
		insertHospitalJPanel.add(distJPanel);
		insertHospitalJPanel.add(addressJPanel);
		insertHospitalJPanel.add(insertOneJPanel);
		insertHospitalJPanel.add(insertMoreJPanel);

		return insertHospitalJPanel;
	}

// ===========================================================
	private JPanel updataJPanel() {
		JPanel insertMoreJPanel = new JPanel();
		insertMoreJPanel.setLayout(new FlowLayout(0));
		insertMoreJPanel.setPreferredSize(new Dimension(350, 40));
		Border blackline = BorderFactory.createLineBorder(Color.gray);
		insertMoreJPanel.setBorder(blackline);

		//
		JButton insertMorejbutter = new JButton("修改所選資料");
		insertMorejbutter.addActionListener((e) -> {
			List<Hospital> listSelectionListeners = visible.getListSelectionListeners();
			if (listSelectionListeners.size() == 1) {
				for (Hospital hospital : listSelectionListeners) {
					updataJDialog(hospital);
				}

			} else if (listSelectionListeners.size() > 1) {
				JOptionPane.showMessageDialog(null, "一次只能修改一個", "修改", JOptionPane.PLAIN_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "請選取想要修改的資料", "修改", JOptionPane.PLAIN_MESSAGE);
			}

		});
		JButton deleteDatajbutter = new JButton("刪除所選資料");
		deleteDatajbutter.addActionListener((e) -> {
			List<Hospital> listSelectionListeners = visible.getListSelectionListeners();
			if (listSelectionListeners.size() > 0) {
				String deleteString = "將刪除:" + listSelectionListeners.size() + "筆資料";
				int yesOrNo = JOptionPane.showConfirmDialog(null, deleteString, "刪除", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (yesOrNo == 0) {
					hospitalDao.sqlDeleteHospital(listSelectionListeners);
					visible.deleteItems(listSelectionListeners);
				}
			} else {
				JOptionPane.showMessageDialog(null, "請選取想要刪除的資料", "刪除", JOptionPane.PLAIN_MESSAGE);
			}

		});

		insertMoreJPanel.add(insertMorejbutter);
		insertMoreJPanel.add(deleteDatajbutter);
		return insertMoreJPanel;
	}

	// ==================================================================================================
	// ==================================================================================================
	// ==================================================================================================
	// ==================================================================================================
	private void updataJDialog(Hospital hospital) {
		JDialog updataJDialog = new JDialog(userFrame, "修改", true);
		updataJDialog.setResizable(true);
		// updataJDialog.setLayout(new FlowLayout(1));
		updataJDialog.setLocationRelativeTo(null);
		updataJDialog.setSize(370, 300);

		// updataJDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		updataJDialog.setLocationRelativeTo(null);
		Container contentPane = updataJDialog.getContentPane();
		//
		JPanel updataJPanel = new JPanel();
		updataJPanel.setLayout(new FlowLayout(0));
		updataJPanel.setPreferredSize(new Dimension(370, 300));
		Border blackline = BorderFactory.createLineBorder(Color.gray);
		updataJPanel.setBorder(blackline);
		// ===================================================
		// 名稱欄位
		// ===================================================
		JPanel nameJPanel = new JPanel();
		nameJPanel.setLayout(new FlowLayout(0));
		nameJPanel.setPreferredSize(new Dimension(300, 30));
		JLabel nemeJLabel = new JLabel("名稱 : " + hospital.getHospitalName());
		nemeJLabel.setFont(new FontUIResource("新細明體", Font.BOLD, 16));
		nameJPanel.add(nemeJLabel);
		//
		// ===================================================
		// 電話欄位
		// ===================================================
		JPanel phoneNumberJPanel = new JPanel();
		phoneNumberJPanel.setLayout(new FlowLayout(0));
		JLabel phoneNumberJLabel = new JLabel("電話 : ");
		JComboBox<String> distNumberComboBox = distNumber();
		JLabel distNumberJLabel = new JLabel("-");
		JTextField phoneNumberJTextField = new JTextField(10);
		String[] phoneNumber = hospital.getTelephoneNumber().split("-");
		distNumberComboBox.setSelectedItem(phoneNumber[0]);
		phoneNumberJTextField.setText(phoneNumber[1]);
		phoneNumberJPanel.add(phoneNumberJLabel);
		phoneNumberJPanel.add(distNumberComboBox);
		phoneNumberJPanel.add(distNumberJLabel);
		phoneNumberJPanel.add(phoneNumberJTextField);

		// ===================================================
		// 地區欄位
		// ===================================================
		RegionSelectionForm distJP = new RegionSelectionForm(this.hospitalDao, null);
		JPanel distJPanel = distJP.getDistJPanel();
		JComboBox<String> cityComboBox = distJP.getCityComboBox(hospital.getCity());

		JComboBox<String> distComboBox = distJP.getDistComboBox(hospital.getDist());
		//
		// ===================================================
		// 地址欄位
		// ===================================================
		JPanel addressJPanel = new JPanel();
		addressJPanel.setLayout(new FlowLayout(0));
		JLabel addressJLabel = new JLabel("地址 : ");
		JTextField addressJTextField = new JTextField(25);
		// 地址會重複顯示縣市地區.去掉它們
		String addrString = hospital.getAddr();
		addrString = addrString.replaceAll(hospital.getCity(), "");
		addrString = addrString.replaceAll(hospital.getDist(), "");
		//
		addressJTextField.setText(addrString);
		addressJPanel.add(addressJLabel);
		addressJPanel.add(addressJTextField);
		//
		// ===================================================
		// 按鍵欄位
		// ===================================================
		JPanel insertOneJPanel = new JPanel();
		insertOneJPanel.setLayout(new FlowLayout(0));
		insertOneJPanel.setPreferredSize(new Dimension(300, 40));
		JButton insertOnejButton = new JButton("更新資料");

		insertOnejButton.addActionListener((e) -> {

			String cituNameString = (String) cityComboBox.getSelectedItem();
			String distnameString = (String) distComboBox.getSelectedItem();
			String addressString = addressJTextField.getText();
			String distNumberText = (String) distNumberComboBox.getSelectedItem();
			String phoneNumberText = phoneNumberJTextField.getText();

			try {
				

				if (phoneNumberText.matches("^\\d{7,8}$")
						&& addressString.matches("^[\\u4E00-\\u9FA5]{3}+[\\u4E00-\\u9FA5A-Za-z0-9]{1,}$")) {

					hospital.setCity(cituNameString);
					hospital.setDist(distnameString);
					// 前面顯示時去掉縣市地區要加回來
					hospital.setAddr(cituNameString + distnameString + addressString);
					hospital.setTelephoneNumber(distNumberText + "-" + phoneNumberText);
					List<Hospital> hospitals = new LinkedList<Hospital>();
					hospitals.add(hospital);

					int sqlAddHospital = hospitalDao.sqlUpdateHospitals(hospitals);
					visible.updateUI();
					updataJDialog.dispose();
					
					if (sqlAddHospital < 1) {
						JOptionPane.showMessageDialog(null, "修改失敗", "錯誤", JOptionPane.INFORMATION_MESSAGE);

					}
				} else {

					JOptionPane.showMessageDialog(null, "格式錯誤", "錯誤", JOptionPane.INFORMATION_MESSAGE);
				}

				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		});
		insertOneJPanel.add(insertOnejButton);

		updataJPanel.add(nameJPanel);
		updataJPanel.add(phoneNumberJPanel);
		updataJPanel.add(distJPanel);
		updataJPanel.add(addressJPanel);
		updataJPanel.add(insertOneJPanel);

		contentPane.add(updataJPanel);
		updataJDialog.setVisible(true);
	}

	private JComboBox<String> distNumber() {
		JComboBox<String> distNumberJComboBox = new JComboBox<String>();
		distNumberJComboBox.addItem("02");
		distNumberJComboBox.addItem("03");
		distNumberJComboBox.addItem("037");
		distNumberJComboBox.addItem("04");
		distNumberJComboBox.addItem("049");
		distNumberJComboBox.addItem("05");
		distNumberJComboBox.addItem("06");
		distNumberJComboBox.addItem("07");
		distNumberJComboBox.addItem("08");
		distNumberJComboBox.addItem("089");
		distNumberJComboBox.addItem("082");
		distNumberJComboBox.addItem("0826");
		distNumberJComboBox.addItem("0836");
		return distNumberJComboBox;

	}
}
