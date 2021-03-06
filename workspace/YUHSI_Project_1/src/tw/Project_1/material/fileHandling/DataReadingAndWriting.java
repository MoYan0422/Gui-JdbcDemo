package tw.Project_1.material.fileHandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;

import tw.Project_1.material.Hospital;
import tw.Project_1.material.HospitalDao;

public class DataReadingAndWriting {

	public DataReadingAndWriting() {
	}

	public void insertDataForReadFile(File path) throws SQLException, IOException {
		HospitalDao hospitalDao = new HospitalDao();
		List<Hospital> flieReader = this.fileFilter(path);
		hospitalDao.openSqlConnection();
		hospitalDao.sqlAddHospitals(flieReader);
		hospitalDao.closeSqlConnection();
	}

	private List<Hospital> fileFilter(File path) throws IOException {
		String fileName = path.getName();
		fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<Hospital> hospitals = new LinkedList<Hospital>();
		if (fileName.equals("csv")) {
			hospitals = insertHospitalForCsv(path);
		} else if (fileName.equals("json")){
			hospitals = insertHospitalForJson(path);
		}else {
			JOptionPane.showMessageDialog(null, "資料格式錯誤", "錯誤", JOptionPane.INFORMATION_MESSAGE);
		}
		
		return hospitals;
	}

	private List<Hospital> insertHospitalForCsv(File path) throws IOException {
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(isr);
		bufferedReader.readLine();
		List<Hospital> hospitals = new LinkedList<Hospital>();
		CSVParser parse;
		try {
			parse = CSVFormat.DEFAULT.parse(bufferedReader);
			for (CSVRecord cr : parse) {
				hospitals.add(new Hospital(cr.get(0), cr.get(1), cr.get(2), cr.get(3), cr.get(4)));
			}parse.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "資料格式錯誤", "錯誤", JOptionPane.INFORMATION_MESSAGE);
		}

		
		bufferedReader.close();
		
		return hospitals;
	}

	private List<Hospital> insertHospitalForJson(File path) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(isr);
		List<Hospital> hospitals = new LinkedList<Hospital>();
		Gson gson = new Gson();
		Hospital[] fromJson = gson.fromJson(bufferedReader, Hospital[].class);
		for (Hospital hospital : fromJson) {
			hospitals.add(hospital);
		}
		bufferedReader.close();
		
		return hospitals;
	}

	public void exportCsvForHospitals(List<Hospital> hospitals, File path) throws IOException {
		FileWriter fileWriter = new FileWriter(path);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write("縣市別,鄉鎮市區別,診所名稱,診所地址,診所電話");
		bufferedWriter.newLine();
		for (Hospital hospital : hospitals) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(hospital.getCity() + ',');
			stringBuilder.append(hospital.getDist() + ',');
			stringBuilder.append(hospital.getHospitalName() + ',');
			stringBuilder.append(hospital.getAddr() + ',');
			stringBuilder.append(hospital.getTelephoneNumber());
			bufferedWriter.write(stringBuilder.toString());
			bufferedWriter.newLine();
		}
		bufferedWriter.flush();
		bufferedWriter.close();
		fileWriter.close();

	}

	public void exportJsonForHospital(List<Hospital> hospitals, File path) throws IOException {
		FileWriter fileWriter = new FileWriter(path);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		Gson gson = new Gson();
		
		gson.toJson(hospitals, bufferedWriter);
		
		bufferedWriter.flush();
		bufferedWriter.close();
		fileWriter.close();
	}

}
