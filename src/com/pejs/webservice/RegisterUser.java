package com.pejs.webservice;

import java.sql.DriverManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;

@Path("/reguser")
public class RegisterUser {

	@GET
	@Path("{name}/{email}/{pincode}")
	@Produces(MediaType.TEXT_HTML)
	public String getMessagePathParam(@PathParam("name") String name,
			@PathParam("email") String email, @PathParam("pincode") int pincode) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int id = 0;

		try {
			// STEP 2: Register JDBC driver
			Class.forName(Setup.JDBC_DRIVER);
			// STEP 3: Open a connection
			System.out.print("\nConnecting to database...");
			conn = DriverManager.getConnection(Setup.DB_URL, Setup.USER,
					Setup.PASS);
			System.out.println(" SUCCESS!\n");
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO pusers(NAME,EMAIL,PINCODE)"
					+ "VALUES ('" + name + "', '" + email + "','" + pincode
					+ "')", Statement.RETURN_GENERATED_KEYS);
			System.out.println("Id inserted is " + id);
			rs = st.getGeneratedKeys();

			if (rs.next()) {
				id = rs.getInt(1);
			} else {

				// throw an exception from here
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		String message = "Registration is successfull";
		return message;
	}
}
