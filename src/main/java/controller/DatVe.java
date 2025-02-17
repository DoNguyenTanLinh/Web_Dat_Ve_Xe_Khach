package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import utils.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import DAO.ChuyenDAO;
import DAO.CustomerDAO;
import DAO.ScheduleDAO;
import DAO.SeatDAO;
import model.Customer;
import model.Schedule;
import model.Seat;

/**
 * Servlet implementation class TimGhe
 */
@WebServlet("/dat_ve")
public class DatVe extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DatVe() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		Connection con = MyUtils.getStoredConnection(request);
		ChuyenDAO c = new ChuyenDAO();
	
		boolean hasError = false;
		String errorString = null;
		//String idChuyen = (String) session.getAttribute("idChuyen");
		String idChuyen= request.getParameter("inputIdChuyenXe");
		String idGhe1 = request.getParameter("seatID1");
		String idGhe2= request.getParameter("seatID2");
		String idGhe3= request.getParameter("seatID3");
		String diemDon= request.getParameter("checkbox_"+idChuyen);
		String diemTra= request.getParameter("checkbox"+idChuyen);
		session.setAttribute("idGhe1", idGhe1);
		session.setAttribute("idGhe2", idGhe2);
		session.setAttribute("idGhe3", idGhe3);
		String tenKh = request.getParameter("nameUser"+idChuyen);
		String phoneUser = request.getParameter("phoneUser"+idChuyen);
		String email = request.getParameter("emailUser"+idChuyen);
		String pass = MyUtils.generatePassword(8);
//		System.out.println("idChuyen" + idChuyen);
//		System.out.println("idGhe1" +idGhe1);
//		System.out.println("idGhe2" +idGhe2);
//		System.out.println("idGhe3" +idGhe3);
//		System.out.println("diemDon"+diemDon);
//		System.out.println("diemTra"+diemTra);
//		System.out.println("nameUser"+tenKh);
//		System.out.println("phoneUser"+phoneUser);
//		System.out.println("emailUser"+email);
//		System.out.println(pass);
		
		
		Customer user = null;
		try {
			if(idGhe1 !=null && idChuyen != null && diemDon != null && diemTra != null && tenKh !=null && phoneUser !=null && email != null) {
				ScheduleDAO sc = new ScheduleDAO();
				String _diemDon = sc.getDiemDon(con,diemDon).toString();
				String _diemTra =sc.getDiemDon(con,diemTra).toString();
				System.out.println("_diemDon"+_diemDon);
				System.out.println("_diemTra"+_diemTra);
				String noiDung = "Chuyến số: "  + "Ghế số: " + idGhe1 + "Địa điểm đón: " + _diemDon + "Địa điểm trả: " + _diemTra
				+ "\nTài khoản để đăng nhập để kiểm tra vé là: Tên đăng nhập: " + phoneUser +" " + "Mật khẩu: "+ pass;

				c.addKhachHang(con, tenKh, phoneUser, email, pass);
				session.setAttribute("tenKh", tenKh);
				session.setAttribute("phoneUser",phoneUser);
				session.setAttribute("email", email);
				session.setAttribute("_diemDon", _diemDon);
				session.setAttribute("_diemTra",_diemTra);

				try {
					// Tìm user trong DB.
					user = CustomerDAO.findUser(con, phoneUser);
					int idUser = user.getId();
					if (idChuyen != null && idGhe1 != null && String.valueOf(idUser) != null) {
//						int id = Integer.parseInt(idGhe);
						int idchuyen = Integer.parseInt(idChuyen);
						int idDiemDon = Integer.parseInt(diemDon);
						int idDiemTra = Integer.parseInt(diemTra);
						try {
							//lay seat de them vao ticket
							SeatDAO seatDAO = new SeatDAO();
							Seat seat = new Seat();
							//kiem tra tung seat hop le thi se them chuyen
							if(idGhe1!="") {
								seat = seatDAO.getIdSeat(con, idGhe1);
								int idSeat = seat.getId();		
								if(String.valueOf(idSeat) != null) {
									c.addChuyen(con, idchuyen, idSeat,idDiemDon, idUser);
								}
							}
							if(idGhe2!="") {
								seat = seatDAO.getIdSeat(con, idGhe2);
								int idSeat2 = seat.getId();
								if(String.valueOf(idSeat2) != null) {
									c.addChuyen(con, idchuyen, idSeat2,idDiemDon, idUser);
								}
							}
							
							if(idGhe3!="") {
								seat = seatDAO.getIdSeat(con, idGhe3);
								int idSeat3 = seat.getId();
								if(String.valueOf(idSeat3) != null) {
									c.addChuyen(con, idchuyen, idSeat3,idDiemDon, idUser);
								}
							}
								
								SendEmail.getInstant().guiMail(email, noiDung);
								String mes = "Email của quý khách đã gửi thành công!";
								request.setAttribute("mes", mes);
								response.sendRedirect(request.getContextPath() + Router.THONG_BAO);
								
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							response.sendRedirect(request.getContextPath() + Router.PAGE_BUY_TICKET);
						}
						
						
					}else{
						errorString = "Có lỗi";
						response.sendRedirect(request.getContextPath() + Router.PAGE_BUY_TICKET);
						
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
					hasError = true;
					errorString = e.getMessage();
				}
			}
			else {
				errorString = "Vui lòng nhập thông tin đầy đủ";
				session.setAttribute("errorString", errorString);
				response.sendRedirect(request.getContextPath()  + Router.PAGE_BUY_TICKET);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			hasError = true;
			errorString = e.getMessage();
		}		
	}

}
