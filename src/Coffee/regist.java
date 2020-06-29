package Coffee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.UUID;
/**
 * Servlet implementation class regist
 */
@WebServlet("/api/usermanage/regist")
public class regist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public regist() {
        super();
        // TODO Auto-generated constructor stub
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://106.13.201.225:3306/coffee?useSSL=false&serverTimezone=GMT","coffee","TklRpGi1");
			Statement stmt = conn.createStatement();
			String userId = UUID.randomUUID().toString();
			String password = request.getParameter("password");
			String telephone = request.getParameter("telephone");
			String email = request.getParameter("email");
			String userName = request.getParameter("userName");
			if(password==null||userName==null) {
				JSONArray jsonarray = new JSONArray();
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("success",false);
				jsonobj.put("msg","参数错误,未完整填写参数");
				jsonarray.add(jsonobj);
				out = response.getWriter();
				out.println(jsonarray);
				stmt.close();
				conn.close();
			}
			else {
				String sql = "insert into user(userId,telephone,email,password,userName) values(?,?,?,?,?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, userId);
				ps.setString(2, telephone);
				ps.setString(3, email);
				ps.setString(4, password);
				ps.setString(5, userName);
				try {
					int rowCount = ps.executeUpdate();
					JSONArray jsonarray = new JSONArray();
					JSONObject jsonobj = new JSONObject();
					if(rowCount>0){
						jsonobj.put("success",true);
						jsonobj.put("msg","注册成功");
						jsonarray.add(jsonobj);
					}
					out = response.getWriter();
					out.println(jsonarray);
					stmt.close();
					conn.close();
				}
				catch(Exception e) {
					JSONArray jsonarray = new JSONArray();
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("success",false);
					jsonobj.put("msg","操作错误,用户可能已经注册");
					jsonarray.add(jsonobj);
					out = response.getWriter();
					out.println(jsonarray);
					stmt.close();
					conn.close();
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
