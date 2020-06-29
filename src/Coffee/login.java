package Coffee;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class login
 */
@WebServlet("/api/usermanage/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Map<String,String>userTokenMap = new HashMap<String, String>();
		this.getServletContext().setAttribute("userTokenMap", userTokenMap);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
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
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			if(userName==null||password==null) {
				JSONArray jsonarray = new JSONArray();
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("success",false);
				jsonobj.put("msg","用户名或密码为空");
				jsonarray.add(jsonobj);
				out = response.getWriter();
				out.println(jsonarray);
				stmt.close();
				conn.close();
			}
			else {
				String sql = "select * from user where userName=? and password=?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, userName);
				ps.setString(2, password);
				ResultSet rs = ps.executeQuery();
				JSONArray jsonarray = new JSONArray();
				JSONObject jsonobj = new JSONObject();
				if(rs.next()){
					jsonobj.put("success",true);
					String token = UUID.randomUUID().toString().replace("-", "");
					jsonobj.put("token",token);
					jsonarray.add(jsonobj);
					Map<String,String> temp = (Map<String,String>)this.getServletContext().getAttribute("userTokenMap");
					temp.put(rs.getString("userId"), token);
					this.getServletContext().setAttribute("userTokenMap",temp);
					for(String key : temp.keySet()){
					    String value = temp.get(key);
					    System.out.println(key+":"+value);
					}
				}
				else {
					jsonobj.put("success",false);
					jsonobj.put("msg","登录失败");
					jsonarray.add(jsonobj);
				}
				out = response.getWriter();
				out.println(jsonarray);
				rs.close();
				stmt.close();
				conn.close();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
