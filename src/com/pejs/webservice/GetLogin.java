package com.pejs.webservice;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("/GetLogin")
public class GetLogin {
	@GET
	@Path("{id}/{timestamp}")
	@Produces(MediaType.TEXT_HTML)
	public String getMessagePathParam(@PathParam("id") int userid,
			@PathParam("timestamp") long timestamp) throws AddressException,
			UnsupportedEncodingException, MessagingException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String email = null;
		String firstName = null;
		int uid = 0;
		int pincode = 0;
		long currentTimestamp = System.currentTimeMillis();
		long difference = Math.abs(currentTimestamp - timestamp);
		JSONObject json = new JSONObject();
		System.out.println(difference);

		if (difference > 15 * 60 * 1000) {
			System.out.println("timestamp is greater than 10 minutes old");
			return "HTML Link is expired";
		}

		try {
			// STEP 2: Register JDBC driver

			Class.forName("com.mysql.jdbc.Driver");
			// STEP 3: Open a connection
			System.out.print("\nConnecting to database...");
			conn = DriverManager.getConnection(Setup.DB_URL, Setup.USER,
					Setup.PASS);
			System.out.println(" SUCCESS!\n");
			Statement st = conn.createStatement();
			String query = "SELECT * FROM pusers where id = '" + userid + "'";
			rs = st.executeQuery(query);
			while (rs.next()) {
				uid = rs.getInt("id");
				firstName = rs.getString("name");
				email = rs.getString("email");
				pincode = rs.getInt("pincode");

				// print the results
				System.out.format("%d,%s, %s, %d\n", uid, firstName, email,
						pincode);
			}
			st.close();

			json.put("Name", firstName);
			json.put("email", email);
			json.put("Pincode", pincode);
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
		String message = json.toString();
		return message;
	}
}
