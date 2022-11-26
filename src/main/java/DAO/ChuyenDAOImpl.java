package DAO;


import java.sql.*;


public class ChuyenDAOImpl implements ChuyenDAO {
	public ChuyenDAOImpl() {
	}
	@Override
	public void addKhachHang(Connection con, String fullname, String phone_number, String email, String password) {
		//String sql1 = "INSERT into Trip(departure,destination,departure_time,price) VALUES (?,?,?,?)";S
		String sqlThongTinKhachHang = "insert into Customer (fullname, phone_number, email,password) values (?,?,?,?)";
		PreparedStatement pre = null;
		try {
			con.setAutoCommit(false);
			pre = con.prepareStatement(sqlThongTinKhachHang);
			pre.setString(1, fullname);
			pre.setString(2, phone_number);
			pre.setString(3, email);
			pre.setString(4,password);
			pre.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
//			ConnectionPool.getInstance().setDefaulAutoCommit(con);
//			ConnectionPool.getInstance().closePre(pre);
//			ConnectionPool.getInstance().freeConnection(con);
		}
	}
	
	@Override
	public void addChuyen(Connection con, int idChuyen, int idGhe, int idKhahcHang) {
		//String sqlThongTinKhachHang = "insert into Customer (fullname, phone_number, email,password) values (?,?,?,?)";
		String sqlAddChuyen = "INSERT into Ticket(trip_id,seat_id,customer_id) VALUES (?,?,?)";
		PreparedStatement pre = null;
		try {
			con.setAutoCommit(false);
			pre = con.prepareStatement(sqlAddChuyen);
			pre.setInt(1, idChuyen);
			pre.setInt(2, idGhe);
			pre.setInt(3, idKhahcHang);
			pre.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
//			ConnectionPool.getInstance().setDefaulAutoCommit(con);
//			ConnectionPool.getInstance().closePre(pre);
//			ConnectionPool.getInstance().freeConnection(con);
		}
	}
	
	
}
