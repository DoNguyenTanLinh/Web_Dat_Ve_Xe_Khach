package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Seat;
import model.Trip;

public class TripDAO {

	public TripDAO() {
		// TODO Auto-generated constructor stub
	}

	public Trip getTripByTicket(Connection conn, String phoneNumber) throws SQLException {

		String idTrip = "select trip_id,seat_id from Ticket join Customer on Customer.id= Ticket.customer_id where phone_number = "
				+ phoneNumber;
		PreparedStatement pstm1 = conn.prepareStatement(idTrip);
		ResultSet rs1 = pstm1.executeQuery();
		String _idTrip = null;

		if (rs1.next()) {
			_idTrip = rs1.getString("trip_id");
		}
		// get trip
		String trip = "select * from Trip where Trip.id =" + _idTrip;
		PreparedStatement pstm2 = conn.prepareStatement(trip);
		ResultSet rs2 = pstm2.executeQuery();

		if (rs2.next()) {
			String departure = rs2.getString("departure");
			String destination = rs2.getString("destination");
			String departure_time = rs2.getString("departure_time");
			int price = rs2.getInt("price");
			int idGara = rs2.getInt("garage_id");
			int numseat = rs2.getInt("num_seat");
			String tripBoard = rs2.getString("trip_board");

			Trip tr = new Trip();
			tr.setDeparture(departure);
			tr.setDestination(destination);
			tr.setDeparture_time(departure_time);
			tr.setPrice(price);
			tr.setGarageID(idGara);
			tr.setNum_seat(numseat);
			tr.setTrip_board(tripBoard);
			return tr;
		}
		return null;
	}

	public List<Seat> getAllGheOnTrip(Connection con, int idChuyen) {

		/* List<Seat> list = new ArrayList<Seat>(); */
		List<Seat> list = new ArrayList<>();
		String sql1 = "select * from seat where trip_id = ?";
		PreparedStatement pre = null;

		try {
			pre = con.prepareStatement(sql1);
			pre.setInt(1, idChuyen);
			ResultSet res = pre.executeQuery();
			while (res.next()) {
				Seat seat = new Seat();
				int id = res.getInt("id");
				String numberChair = res.getString("number_chair");
				int status = res.getInt("status");
				seat.setId(id);
				seat.setNumber_chair(numberChair);
				seat.setStatus(status);
				list.add(seat);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

		}

		return list;
	}

	public static ArrayList<String> getListDeparture(Connection con) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT DISTINCT departure FROM Trip";
		PreparedStatement pre = null;

		try {
			pre = con.prepareStatement(sql);
			ResultSet res = pre.executeQuery();
			while (res.next()) {
				String departure = res.getString("departure");
				list.add(departure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static ArrayList<String> getListDestination(Connection con) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT DISTINCT destination FROM Trip";
		PreparedStatement pre = null;

		try {
			pre = con.prepareStatement(sql);
			ResultSet res = pre.executeQuery();
			while (res.next()) {
				String destination = res.getString("destination");
				list.add(destination);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static ArrayList<Trip> getListTrip(Connection con, String departute, String destination,
			String departure_time) {
		ArrayList<Trip> list_trip = new ArrayList<Trip>();
		String sql = "SELECT Trip.*, Garage.fullname, Garage.address, Garage.description  FROM Trip JOIN Garage ON garage_id = Garage.id"
				+ " WHERE CONVERT(date, departure_time) = '" + departure_time + "' and departure = N'" + departute
				+ "' and destination = N'" + destination + "' and Garage.deleted = 0";
		PreparedStatement pre = null;

		try {
			pre = con.prepareStatement(sql);
			ResultSet res = pre.executeQuery();
			while (res.next()) {
				Trip trip = new Trip();
				trip.setId(res.getInt("id"));
				trip.setDeparture(res.getString("departure"));
				trip.setDestination(res.getString("destination"));
				trip.setDeparture_time(res.getString("departure_time"));
				trip.setPrice(res.getInt("price"));
				trip.setNum_seat(res.getInt("num_seat"));
				trip.setTrip_board(res.getString("trip_board"));
				trip.garage.setId(res.getInt("garage_id"));
				trip.garage.setAddress(res.getString("address"));
				trip.garage.setFullname(res.getString("fullname"));
				trip.garage.setDescription(res.getString("description"));
				list_trip.add(trip);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list_trip;
	}

	public List<Trip> listTrips(Connection conn, String userManager) throws SQLException {
		String garage_id = "select garage_id from Manager where phone_number = " + userManager;
		PreparedStatement pstm1 = conn.prepareStatement(garage_id);
		ResultSet rs1 = pstm1.executeQuery();
		int _garageid = 0;

		if (rs1.next()) {
			_garageid = rs1.getInt("garage_id");
		}
		String sql = "select * from Trip where garage_id =" + _garageid;
		PreparedStatement pstm = conn.prepareStatement(sql);
		ResultSet rs = pstm.executeQuery();
		List<Trip> list = new ArrayList<Trip>();
		while (rs.next()) {
			String departure = rs.getString("departure");
			String destination = rs.getString("destination");
			String departure_time = rs.getString("departure_time");
			String trip_board = rs.getString("trip_board");
			int price = rs.getInt("price");
			int id = rs.getInt("id");
			int num_seat = rs.getInt("num_seat");
			Trip trip = new Trip();
			trip.setId(id);
			trip.setDeparture(departure);
			trip.setDestination(destination);
			trip.setDeparture_time(departure_time);
			trip.setTrip_board(trip_board);
			trip.setNum_seat(num_seat);
			trip.setPrice(price);
			trip.setGarageID(_garageid);
			list.add(trip);
		}

		return list;
	}

	public List<Trip> searchListTrips(Connection conn, String trip_board) throws SQLException {
		String sql = "select * from Trip where trip_board = '" + trip_board + "' ";
		PreparedStatement pstm1 = conn.prepareStatement(sql);
		ResultSet rs = pstm1.executeQuery();

		List<Trip> list = new ArrayList<Trip>();
		while (rs.next()) {
			String departure = rs.getString("departure");
			String destination = rs.getString("destination");
			String departure_time = rs.getString("departure_time");
			int garage_id = rs.getInt("garage_id");
			int price = rs.getInt("price");
			int id = rs.getInt("id");
			int num_seat = rs.getInt("num_seat");
			Trip trip = new Trip();
			trip.setId(id);
			trip.setDeparture(departure);
			trip.setDestination(destination);
			trip.setDeparture_time(departure_time);
			trip.setTrip_board(trip_board);
			trip.setNum_seat(num_seat);
			trip.setPrice(price);
			trip.setGarageID(garage_id);
			list.add(trip);
		}
		return list;
	}

	public void addTrip(Connection conn, String departure, String destination, String departure_time, int price,
			int num_seat, String trip_board, String userManager) throws SQLException {
		// kiểm tra xem có tốn tại chuyến xe nào có trùng biển số và thời gian chạy
		// không
		// nếu có thì retunt 1 đã tồn tại, hoặc 0 thì chưa tồn tại và insert
		String checkTrip = "select * from Trip where trip_board = ? and departure_time = ?";
		PreparedStatement pstm3 = conn.prepareStatement(checkTrip);
		pstm3.setString(1, trip_board);
		pstm3.setString(2, departure_time);
		ResultSet rs3 = pstm3.executeQuery();
		int check = 0;
		if (rs3.next()) {
			check = rs3.getInt("id");
		}
		// nếu không tồn tại chuyến xe trùng
		if (check == 0) {
			String garage_id = "select garage_id from Manager where phone_number = " + userManager;
			PreparedStatement pstm1 = conn.prepareStatement(garage_id);
			ResultSet rs1 = pstm1.executeQuery();
			int _garageid = 0;

			if (rs1.next()) {
				_garageid = rs1.getInt("garage_id");
			}
			String sqlAddTrip = "insert into Trip (departure, destination, departure_time,price,num_seat,trip_board,garage_id) values (?,?,?,?,?,?,?)";
			PreparedStatement pre = null;
			try {
				conn.setAutoCommit(false);
				pre = conn.prepareStatement(sqlAddTrip);
				pre.setString(1, departure);
				pre.setString(2, destination);
				pre.setString(3, departure_time);
				pre.setInt(4, price);
				pre.setInt(5, num_seat);
				pre.setString(6, trip_board);
				pre.setInt(7, _garageid);
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
		} else {
			System.out.println("99999999999999999999999999999999");
		}
	}

	public int getIdTrip(Connection conn, String tripBoard) throws SQLException {
		String trip_id = "select id from Trip where trip_board = '" + tripBoard + "' ";
		PreparedStatement pstm2 = conn.prepareStatement(trip_id);
		ResultSet rs2 = pstm2.executeQuery();
		int tripid = 0;
		if (rs2.next()) {
			tripid = rs2.getInt("id");
			System.out.println(tripid);
			return tripid;
		}
		return 0;
	}


	public static Trip getTripFromTripID(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM Trip WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, id);

		ResultSet rs = pstm.executeQuery();
		while (rs.next()) {
			String departure = rs.getString("departure");
			String destination = rs.getString("destination");
			String departure_time = rs.getString("departure_time");
			String trip_board = rs.getString("trip_board");
			int price = rs.getInt("price");
			int num_seat = rs.getInt("num_seat");
			int garageid = rs.getInt("garage_id");
			Trip trip = new Trip();
			trip.setId(id);
			trip.setDeparture(departure);
			trip.setDestination(destination);
			trip.setDeparture_time(departure_time);
			trip.setTrip_board(trip_board);
			trip.setNum_seat(num_seat);
			trip.setPrice(price);
			trip.setGarageID(garageid);
			System.out.println("678678678");
			return trip;
		}
		return null;
	}

	public static void updateTrip(Connection conn, String departure, String destination, String departure_time,
			int price, int num_seat, String trip_board, int id) throws SQLException {
		String sql = "UPDATE Trip SET departure = ?, destination = ?, departure_time = ?, price = ?, num_seat = ?, trip_board = ? WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, departure);
		pstm.setString(2, destination);
		pstm.setString(3, departure_time);
		pstm.setInt(4, price);
		pstm.setInt(5, num_seat);
		pstm.setString(6, trip_board);
		pstm.setInt(7, id);
		System.out.println("888888888888888888888888888888888888888");
		pstm.executeUpdate();
	}

	public static void deleteTrip(Connection conn, int id) throws SQLException {
		String sql = "DELETE FROM Trip WHERE id = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, id);
		pstm.executeUpdate();

		String sql2 = "DELETE FROM Seat WHERE trip_id = ?";
		PreparedStatement pstm2 = conn.prepareStatement(sql2);
		pstm2.setInt(1, id);
		pstm2.executeUpdate();
	}
	
}
