package edu.unsw.comp9321;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public void test() {
		if (!dbConnStatus)
			return;

		try {
			Statement stmt;
			dbConn.setAutoCommit(false);
			stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT authors FROM publications;" );
			while ( rs.next() ) {
				String authors = rs.getString("authors");
				System.out.println( "Author = " + authors );
			}
			rs.close();
			stmt.close();
		}
		 catch (SQLException e) {
			return;
		}
	}

	public Publication GetRandomPublication() {
		if (!dbConnStatus)
			return null;

		Publication p  = new Publication();

		try {
			Statement stmt;
			dbConn.setAutoCommit(false);
			stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM publications OFFSET floor(random()*(SELECT COUNT(*) FROM publications)) LIMIT 1;" );

			while ( rs.next() ) {
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
			}

			rs.close();
			stmt.close();

			return p;
		}
		catch (SQLException e) {
			return null;
		}
	}

	public Publication get(Integer intId) {
		// TODO Auto-generated method stub
		return null;
	}
}