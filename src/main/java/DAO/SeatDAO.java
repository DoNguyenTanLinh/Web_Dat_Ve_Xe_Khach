package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Seat;

import java.util.ArrayList;
public class SeatDAO {

	public SeatDAO() {
		// TODO Auto-generated constructor stub
	}

	public Seat getSeat(Connection con, String phoneNumber) {
		String idSeat = "select seat_id from Ticket join Customer on Customer.id= Ticket.customer_id where phone_number = "
				+ phoneNumber;
		String _idSeat = null;
		try {
			PreparedStatement pstm1 = con.prepareStatement(idSeat);
			ResultSet rs1 = pstm1.executeQuery();

			if (rs1.next()) {
				_idSeat = rs1.getString("seat_id");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		String sql1 = "SELECT number_chair,status FROM Seat WHERE id = " + _idSeat;

		Seat seat = new Seat();
		PreparedStatement pre = null;
		ResultSet res;
		try {
			pre = con.prepareStatement(sql1);
			res = pre.executeQuery();
			while (res.next()) {
				String number_chair = res.getString("number_chair");
				int status = res.getInt("status");
				seat.setNumber_chair(number_chair);
				seat.setStatus(status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

		}
		return seat;

	}

	public Seat getIdSeat(Connection conn, String idGhe) throws SQLException {

		String sql = "Select id from Seat " + "where id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);

		pstm.setString(1, idGhe);
		ResultSet rs = pstm.executeQuery();

		if (rs.next()) {
			int id = rs.getInt("id");
			Seat seat = new Seat();
			seat.setId(id);
			return seat;
		}
		return null;
	}
	
	public static ArrayList<Seat> getListSeat(Connection con, int trip_id)
	{
		ArrayList<Seat> list = new ArrayList<Seat>();
		String sql = "SELECT * FROM Seat WHERE trip_id =  " + trip_id;
		
		PreparedStatement pre = null;
		try {
			pre = con.prepareStatement(sql);
			ResultSet res = pre.executeQuery();
			while (res.next()) {
				Seat seat = new Seat();
				seat.setId(res.getInt("id"));
				seat.setNumber_chair(res.getString("number_chair"));
				seat.setStatus(res.getInt("status"));
				list.add(seat);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void insertSeat(Connection conn, String numberChair, int garage_id) throws SQLException {
		String sqlAddTrip = "insert into Seat (number_chair, status, trip_id) values (?,?,?)";
		PreparedStatement pre = null;
		int status = 0;
		try {
			conn.setAutoCommit(false);
			pre = conn.prepareStatement(sqlAddTrip);
			pre.setString(1, numberChair);
			pre.setInt(2, status);
			pre.setInt(3, garage_id);
			pre.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
