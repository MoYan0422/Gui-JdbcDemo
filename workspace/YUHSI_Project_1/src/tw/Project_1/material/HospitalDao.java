package tw.Project_1.material;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

public class HospitalDao {
	private Connection connection;
	private String databaseName = "YUHSI_Project_1";
	private String user = "sa";
	private String password = "P@ssw0rd!";

	// 開啟連線
	public void openSqlConnection() throws SQLException {
		if (connection == null) {
			String userURL = "jdbc:sqlserver://localhost:1433;" + "databaseName=" + databaseName + ";" + "user=" + user
					+ ";" + "password=" + password + ";" + "TrustServerCertificate=true;";
			this.connection = DriverManager.getConnection(userURL);
		}
	}

	// 關閉連線
	public void closeSqlConnection() throws SQLException {
		if (connection != null) {
			this.connection.close();
		}
	}

	// 是否有連線
	public boolean connectionIsClosed() throws SQLException {
		return connection == null;
	}

	// 顯示全部資料
	public List<Hospital> selectHospitals() throws SQLException {
		String sqlString = "SELECT * FROM fratc";
		PreparedStatement prepareStatement = connection.prepareStatement(sqlString);
		ResultSet rs = prepareStatement.executeQuery();
		List<Hospital> rsToList = RsToList(rs);
		// int size = rsToList.size();
		// String outString = "總共 "+size+" 筆資料。";
		// JOptionPane.showConfirmDialog(null, outString, "資訊",
		// JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
		return rsToList;
	}

	// 用地區去查找資料
	public List<Hospital> selectHospitals(String city, String dist) throws SQLException {
		String sqlString = "SELECT * FROM fratc WHERE city = ? AND dist = ? ";
		String sqlStringAllDist = "SELECT * FROM fratc WHERE city = ? ";
		ResultSet rs;
		if (dist.equals("all")) {
			PreparedStatement prepareStatement = connection.prepareStatement(sqlStringAllDist);
			prepareStatement.setString(1, city);
			rs = prepareStatement.executeQuery();

		} else {
			PreparedStatement prepareStatement = connection.prepareStatement(sqlString);
			prepareStatement.setString(1, city);
			prepareStatement.setString(2, dist);
			rs = prepareStatement.executeQuery();
		}
		List<Hospital> rsToList = RsToList(rs);
		return rsToList;
	}

	// 用診所名稱查找
	public List<Hospital> selectHospitals(String hospitalName) throws SQLException {
		String sqlString = "SELECT * FROM fratc WHERE hospitalName like  ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
		preparedStatement.setString(1, hospitalName);
		ResultSet rs = preparedStatement.executeQuery();
		List<Hospital> rsToList = RsToList(rs);
		return rsToList;
	}

	private List<Hospital> RsToList(ResultSet rs) throws SQLException {
		List<Hospital> hospitals = new LinkedList<Hospital>();
		while (rs.next()) {
			Hospital hospital = new Hospital();
			hospital.setCity(rs.getString("city"));
			hospital.setDist(rs.getString("dist"));
			hospital.setHospitalName(rs.getString("hospitalName"));
			hospital.setAddr(rs.getString("addr"));
			hospital.setTelephoneNumber(rs.getString("telephoneNumber"));
			hospitals.add(hospital);

		}
		return hospitals;
	}

	public int sqlAddHospital(Hospital hospital) throws SQLException {
		String sqlString = "INSERT INTO fratc(city,dist,hospitalName,addr,telephoneNumber) " + "values (?,?,?,?,?);";
		int row = 0;
		PreparedStatement prepareStatement = connection.prepareStatement(sqlString);
		prepareStatement.setString(1, hospital.getCity());
		prepareStatement.setString(2, hospital.getDist());
		prepareStatement.setString(3, hospital.getHospitalName());
		prepareStatement.setString(4, hospital.getAddr());
		prepareStatement.setString(5, hospital.getTelephoneNumber());

		try {
			row = prepareStatement.executeUpdate();
		} catch (SQLException e) {
			return row = 0;

		}
		return row;
	}

	public int sqlAddHospitals(List<Hospital> hospitals) throws SQLException {
		int row = 0;
		for (Hospital hospital : hospitals) {
			String sqlString = "INSERT INTO fratc(city,dist,hospitalName,addr,telephoneNumber) "
					+ "values (?,?,?,?,?);";
			PreparedStatement prepareStatement = connection.prepareStatement(sqlString);
			prepareStatement.setString(1, hospital.getCity());
			prepareStatement.setString(2, hospital.getDist());
			prepareStatement.setString(3, hospital.getHospitalName());
			prepareStatement.setString(4, hospital.getAddr());
			prepareStatement.setString(5, hospital.getTelephoneNumber());

			try {
				row += prepareStatement.executeUpdate();
			} catch (SQLException e) {
				if (hospitals.size() < 2) {
					JOptionPane.showMessageDialog(null, hospital.getHospitalName() + e.getLocalizedMessage(), "錯誤", JOptionPane.PLAIN_MESSAGE);
				} else {
					int yesOrNo = JOptionPane.showConfirmDialog(null,
							hospital.getHospitalName() + e.getLocalizedMessage() + "是否跳過該筆資料繼續?", "錯誤",
							JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (yesOrNo != 0) {
						return row;
					}
				}

			}

		}
		JOptionPane.showMessageDialog(null, "共新增" + row + "筆", "新增", JOptionPane.PLAIN_MESSAGE);
		return row;

	}

	public int sqlUpdateHospital(Hospital hospital) throws SQLException {
		int row = 0;
		String sql = "UPDATE fratc SET city = ?,dist = ?,addr = ? ,telephoneNumber = ? WHERE hospitalName = ?";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.setString(1, hospital.getCity());
		prepareStatement.setString(2, hospital.getDist());
		prepareStatement.setString(3, hospital.getAddr());
		prepareStatement.setString(4, hospital.getTelephoneNumber());
		prepareStatement.setString(5, hospital.getHospitalName());
		row = prepareStatement.executeUpdate();
		return row;
	}

	public int sqlUpdateHospitals(List<Hospital> hospitals) throws SQLException {
		int row = 0;
		for (Hospital hospital : hospitals) {
			row += sqlUpdateHospital(hospital);
		}
		JOptionPane.showMessageDialog(null, "成功修改" + row + "筆", "修改", JOptionPane.PLAIN_MESSAGE);
		return row;
	}

	public int sqlDeleteHospital(String hospitalName) throws SQLException {
		int row = 0;
		String sql = "DELETE FROM fratc WHERE hospitalName = ?";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.setString(1, hospitalName);
		row = prepareStatement.executeUpdate();
		return row;
	}

	public int sqlDeleteHospital(Hospital hospital) throws SQLException {
		return sqlDeleteHospital(hospital.getHospitalName());
	}

	public int sqlDeleteHospital(List<Hospital> hospitals) {
		int row = 0;
		for (Hospital hospital : hospitals) {
			try {
				row += sqlDeleteHospital(hospital);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null, "成功刪除" + row + "筆", "刪除", JOptionPane.PLAIN_MESSAGE);
		return row;
	}
	// ----------------------------------------------------------------------

	public void beginTranSaction() throws SQLException {
		String sqlString = "BEGIN TRANSACTION";
		PreparedStatement prepareStatement = connection.prepareStatement(sqlString);
		prepareStatement.execute();
	}

	public void commit() throws SQLException {
		String sql = "COMMIT";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.execute();
	}

	public void rollBack() throws SQLException {
		String sql = "ROLLBACK";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.execute();
	}

	public List<String> selectDLists(String city) throws SQLException {
		String sqlString = "SELECT dist FROM cityAndDist WHERE city = ?";
		PreparedStatement prepareStatement = connection.prepareStatement(sqlString);
		prepareStatement.setString(1, city);
		ResultSet rs = prepareStatement.executeQuery();
		LinkedList<String> dists = new LinkedList<String>();
		while (rs.next()) {
			dists.add(rs.getString("dist"));
		}
		return dists;
	}
}
