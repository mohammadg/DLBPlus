package edu.unsw.comp9321;

import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Database helper class which abstracts database calls.
 */

public class DBHelper {
	private static final String HOST = "localhost";
	private static final Integer PORT = 5432;
	private static final String dbName = "dlbplus";
	private static final String dbUser = "postgres";
	private static final String dbPass = "password";

	private Connection dbConn = null;
	private boolean dbConnStatus = false;

	/**
	 * Initiate connection to DB
	 */
	public boolean init() {
		if (dbConnStatus) //if already connected
			return true;

		try {
			Class.forName("org.postgresql.Driver");
			dbConn = DriverManager
							.getConnection("jdbc:postgresql://" + HOST + ":" + PORT + "/" + dbName,
											dbUser, dbPass);
			dbConnStatus = true;

			//Get total number of publications
		} catch (Exception e) {
			dbConnStatus = false;
		}

		return dbConnStatus;
	}

	/**
	 * Get a random publication
	 *
	 * @return Random publication or null if no publications exist (0 publications in db)
	 */
	public Publication GetRandomPublication() {
		if (!dbConnStatus)
			return null;

		try {
			Statement stmt;
			dbConn.setAutoCommit(false);
			stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM publications OFFSET floor(random()*(SELECT COUNT(*) FROM publications)) LIMIT 1;" );

			return processResultSet(rs);
		}
		catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Get publication using ID
	 *
	 * @param pubID Publication identifier
	 * @return publication with matching publication identifier
	 */
	public Publication GetPublication(int pubID) {
		if (!dbConnStatus)
			return null;

		try {
			Statement stmt;
			dbConn.setAutoCommit(false);
			stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM publications where id = " + pubID + ";" );

			return processResultSet(rs);
		}
		catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Given a result set, parse next row of values into Publication
	 *
	 * @param rs result set for a query on the publications table
	 * @return null if no match found/rows exhausted, publication of next row otherwise
	 */
	private Publication processResultSet(ResultSet rs) {
		Publication p = new Publication();

		try {
			if (rs.next()) {
				Integer id = rs.getInt("id");
				String type = rs.getString("type");
				List<String> authors = Arrays.asList(rs.getString("authors").split("\\|"));
				List<String> editors = Arrays.asList(rs.getString("editors").split("\\|"));
				String title = rs.getString("title");
				String pages = rs.getString("pages");
				Integer year = rs.getInt("year");
				String address = rs.getString("address");
				String volume = rs.getString("volume");
				String number = rs.getString("number");
				String month = rs.getString("month");
				List<String> urls = Arrays.asList(rs.getString("urls").split("\\|"));
				List<String> ees = Arrays.asList(rs.getString("ees").split("\\|"));
				String cdrom = rs.getString("cdrom");
				List<String> cites = Arrays.asList(rs.getString("cites").split("\\|"));
				String publisher = rs.getString("publisher");
				String note = rs.getString("note");
				String crossref = rs.getString("crossref");
				List<String> isbns = Arrays.asList(rs.getString("isbns").split("\\|"));
				String series = rs.getString("series");
				List<String> venues = Arrays.asList(rs.getString("venues").split("\\|"));  //school, booktitle and journal
				String chapter = rs.getString("chapter");
				Double recprice = rs.getDouble("recPrice");
				String rating = rs.getString("rating");

				//Set publication fields
				p.setId(id);
				p.setType(type);

				for (String author : authors)
					p.setAuthor(author);

				for (String editor : editors)
					p.setEditor(editor);

				p.setTitle(title);
				p.setPages(pages);
				p.setYear(year);
				p.setAddress(address);
				p.setVolume(volume);
				p.setNumber(number);
				p.setMonth(month);

				for (String url : urls)
					p.setUrl(url);

				for (String ee : ees)
					p.setEe(ee);

				p.setCdrom(cdrom);

				for (String cite : cites)
					p.setCite(cite);

				p.setPublisher(publisher);
				p.setNote(note);
				p.setCrossref(crossref);

				for (String isbn : isbns)
					p.setIsbn(isbn);

				p.setSeries(series);

				for (String venue : venues)
					p.setVenue(venue);

				p.setChapter(chapter);
				p.setRecprice(recprice);
				p.setRating(rating);
			} else { //No result found
				p = null;
			}
		} catch (SQLException e) {
			return null;
		}

		return p;
	}


	public User CreateUser(String username, String plainTextPassword, String fname, String lname, String email,
												 String address, java.util.Date dob, String creditcard, String dp) {
		if (!dbConnStatus)
			return null;

		try {
			if (!doesUserExist(username)) {
				System.out.println("User does not exist, creating new user");


				//Generate random salt and hashed password
				final Random r = new SecureRandom();
				byte[] salt = new byte[32];
				r.nextBytes(salt);
				String encodedSalt = Base64.encode(salt);
				String passwordHash = DigestUtils.sha1Hex(encodedSalt + plainTextPassword);

				Statement stmt;
				dbConn.setAutoCommit(false);
				stmt = dbConn.createStatement();
				String q = "INSERT INTO users (username, salt, password, fname, lname, email, address, dob, creditcard, cartid, dp, acctstatus)" +
								"VALUES ('" + username + "', '" + encodedSalt + "', '" + passwordHash + "', '" + fname + "', '" + lname + "', '" + email + "', '" + address + "', '" + dob.toString() + "', '" + creditcard + "', '" + dp + "', true);";
				System.out.println(q);
				stmt.executeUpdate(q);
				dbConn.commit();
				return null; //todo: change to return user GetUser
			} else { //User already exists
				System.out.println("User already exists");
				return null;
			}
		}
		catch (SQLException e) {
			return null;
		}
	}

	private boolean doesUserExist(String username) {
		if (!dbConnStatus)
			return true; //this should never be returned

		try {
			Statement stmt;
			dbConn.setAutoCommit(false);
			stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users where username = '" + username + "';" );

			if (rs.next()) {
				int count = rs.getInt("count");
				if (count == 0)
					return false;
				else
					return true;
			}

			return true; //this should never be returned
		}
		catch (SQLException e) {
			return true; //this should never be returned
		}

	}
}