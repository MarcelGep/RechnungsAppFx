package com.gepraegs.rechnungsAppFx.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gepraegs.rechnungsAppFx.*;
import javafx.beans.property.StringProperty;

public class DbController {

	private static final DbController dbcontroller = new DbController();
	private static Connection connection;
	private static final Logger LOGGER = Logger.getLogger( DbController.class.getName() );
	private static final String DB_NAME = "database/database.db";
	private static final String JDBC_PREFIX = "jdbc:sqlite:";
	private static String DB_PATH = "";
	// TODO
	private static final String DB_SQL_FILE = "src/main/resources/database.sql";

	static {
		try {
			Class.forName( "org.sqlite.JDBC" );
		} catch ( ClassNotFoundException e ) {
			System.err.println( "Fehler beim Laden des JDBC-Treibers" );
			e.printStackTrace();
			LOGGER.warning( e.toString() );
		}
	}

	public static DbController getInstance() {
		return dbcontroller;
	}

	public boolean isDbConnected() {
		boolean isDbConnected = false;

		try {
			if ( connection != null ) {
				isDbConnected = !connection.isClosed();
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
			LOGGER.warning( e.toString() );
			isDbConnected = false;
		}

		return isDbConnected;
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection( JDBC_PREFIX + DB_PATH );
	}

	public boolean databaseExist( String dbPath ) {
		File dbFile = new File( dbPath );
		return dbFile.exists();
	}

	public void initDBConnection( String dbPath ) {
		DB_PATH = dbPath;

		try {
			if ( connection != null )
				return;

			LOGGER.info( "Creating Connection to Database..." );
			LOGGER.info( "DB-Path: " + DB_PATH );

			if ( !databaseExist( DB_PATH ) ) {
				LOGGER.info( "Database NOT FOUND!" );
				return;
			}

			connection = getConnection();

			if ( !connection.isClosed() ) {
				LOGGER.info( "...Connection established to DB \"" + DB_NAME + "\"" );
			}
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return;
		}

		Runtime.getRuntime().addShutdownHook( new Thread( () -> {
			try {
				if ( !connection.isClosed() && connection != null ) {
					connection.close();
					if ( connection.isClosed() ) {
						System.out.println( "Connection to Database closed" );
					}
				}
			} catch ( SQLException e ) {
				e.printStackTrace();
				System.out.println( e.toString() );
			}
		} ) );
	}

	public boolean createNewDatabase( String dbPath ) {
		DB_PATH = dbPath;

		StringBuffer sb = new StringBuffer();

		try {
			if ( databaseExist( DB_PATH ) ) {
				new File( DB_PATH ).delete();
			}

			FileReader fr = new FileReader( new File( DB_SQL_FILE ) );
			BufferedReader br = new BufferedReader( fr );

			String line;
			while ( ( line = br.readLine() ) != null ) {
				sb.append( line );
			}
			br.close();

			String[] inst = sb.toString().split( ";" );

			Connection c = getConnection();
			Statement st = c.createStatement();

			for ( int i = 0; i < inst.length; i++ ) {
				if ( !inst[i].trim().equals( "" ) ) {
					st.executeUpdate( inst[i] );
					LOGGER.info( "DB Install: " + inst[i] );
				}
			}

			LOGGER.info( "DB Install: COMPLETED!" );
			return true;
		} catch ( Exception e ) {
			LOGGER.warning( "Error Create DB: " + e.toString() );
			return false;
		}
	}

//	private boolean updateIDs( int deletedId ) {
//		String query = "UPDATE Guests SET ID = ID - 1 WHERE ID > ?";
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setInt( 1, deletedId );
//			ps.executeUpdate();
//
//			LOGGER.info( "Guest ids updated!" );
//			return true;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}
//	}
//
//	private boolean updateSqliteSeq() {
//		int guestCount = readGuestCount();
//		String query = "UPDATE sqlite_sequence SET seq = ? WHERE name = 'Guests'";
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setInt( 1, guestCount );
//			ps.executeUpdate();
//
//			LOGGER.info( "Update slite sequence from Guests to " + guestCount );
//
//			return true;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}
//	}

	public boolean createCustomer(Customer customer ) {
		// updateSqliteSeq();

		String query = "INSERT INTO Customers(company, name1, name2, street, plz, location, country, phone, fax, email, website, discount, accountBalance, informations, handy) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setString( 1, customer.getCompany() );
//			ps.setString( 2, customer.getName1() );
//			ps.setInt( 3, customer.getName2() );
//			ps.setString( 4, customer.getStreet() );
//			ps.setString( 4, customer.getPlz() );
//			ps.setString( 4, customer.getLocation() );
//			ps.setString( 5, customer.getPhone() );
//			ps.setString( 6, customer.getHandy() );
//			ps.setString( 7, customer.getEmail() );
//			ps.setString( 8, customer.getStreet() );
//			ps.setString( 9, customer.getPlz() );
//			ps.setString( 10, customer.getOrt() );
//			ps.setString( 11, customer.getComments() );
//			ps.executeUpdate();
//
//			LOGGER.info( "New guest created!" );

//			return true;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}

		return true;
	}

	public boolean editGuest( Customer guest ) {
//		String query = "UPDATE Guests SET LastName = ?, FirstName = ?, Age = ?, Status = ?, Phone = ?, Handy = ?, "
//			+ "Email = ?, Street = ?, Plz = ?, Ort = ?, Comments = ? WHERE ID = ?";
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setString( 1, guest.getLastName() );
//			ps.setString( 2, guest.getFirstName() );
//			ps.setInt( 3, guest.getAge() );
//			ps.setString( 4, guest.getStatus() );
//			ps.setString( 5, guest.getPhone() );
//			ps.setString( 6, guest.getHandy() );
//			ps.setString( 7, guest.getEmail() );
//			ps.setString( 8, guest.getStreet() );
//			ps.setString( 9, guest.getPlz() );
//			ps.setString( 10, guest.getOrt() );
//			ps.setString( 11, guest.getComments() );
//			ps.setInt( 12, guest.getId() );
//			ps.executeUpdate();
//
//			LOGGER.info( "Guest with ID: " + guest.getId() + " updated to: " + guest.getFirstName() + ", "
//				+ guest.getLastName() + ", " + guest.getAge() + ", " + guest.getStatus() + ", " + guest.getPhone()
//				+ ", " + guest.getHandy() + ", " + guest.getEmail() + ", " + guest.getStreet() + ", " + guest.getPlz()
//				+ ", " + guest.getOrt() + ", " + guest.getComments() );
//
//			return true;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}
		return true;
	}

	public boolean deleteGuest( Customer guest ) {
//		String query = "DELETE FROM Guests WHERE ID = ?";
//		int id = guest.getId();
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setInt( 1, id );
//			ps.executeUpdate();
//
//			LOGGER.info( "Guest " + guest.getFirstName() + " " + guest.getLastName() + " with ID: " + guest.getId()
//				+ " deleted!" );
//
//			return true;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}

		return true;
	}

	public boolean setInvite( Customer guest ) {
//		String query = "UPDATE Guests SET Invite = ? WHERE ID = ?";
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setInt( 1, guest.isInvite() ? 1 : 0 );
//			ps.setInt( 2, guest.getId() );
//			ps.executeUpdate();
//
//			LOGGER.info(
//				"Set invite: " + guest.isInvite() + " for " + guest.getFirstName() + ", " + guest.getLastName() );
//
//			return true;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}
		return true;

	}

	public String readInviteCount() {
		String query = "SELECT COUNT(*) FROM Guests WHERE Invite = 1";

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			String count = "";

			while ( rs.next() ) {
				count = rs.getString( "COUNT(*)" );
			}

			LOGGER.info( "Read invite count of all Guests = " + count );

			return count;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return null;
		}
	}

	public int getGuestId( Customer guest ) {
		int id = -1;
//
//		String query = "SELECT ID FROM Guests WHERE LastName = ? AND FirstName = ? AND Age = ?";
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setString( 1, guest.getLastName() );
//			ps.setString( 2, guest.getFirstName() );
//			ps.setInt( 3, guest.getAge() );
//
//			ResultSet rs = ps.executeQuery();
//
//			while ( rs.next() ) {
//				id = rs.getInt( "ID" );
//			}
//
//			LOGGER.info( "Guest ID = " + id );
//
//			return id;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//		}

		return id;
	}

	public List<Customer> readCustomers() {
		String query = "SELECT * FROM Customers ORDER BY LOWER(\"Company\") ASC, LOWER(\"Kd-Nr.\") ASC";

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			List<Customer> customerData = new ArrayList<>();

			while ( rs.next() ) {
				String kdNr = rs.getString( "Kd-Nr." );
				String company = rs.getString( "Company" );
				String name1 = rs.getString( "Name1" );
				String name2 = rs.getString( "Name2" );
				String street = rs.getString( "Street" );
				String plz = rs.getString( "Plz" );
				String location = rs.getString( "Location" );
				String country = rs.getString( "Country" );
				String phone = rs.getString( "Phone" );
				String handy = rs.getString( "Handy" );
				String fax = rs.getString( "Fax" );
				String email = rs.getString( "Email" );
				String website = rs.getString( "Website" );
				String informations = rs.getString( "Informations" );
				double discount = rs.getDouble( "Discount" );
				double accountBalance = rs.getDouble( "AccountBalance" );

				Customer customer = new Customer(kdNr, company, name1, name2, street, plz, location, country, phone, handy, fax,
						email, website, informations, discount, accountBalance);

				customerData.add( customer );
			}
			LOGGER.info( "Read all customers!" );
			return customerData;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return null;
		}
	}

	public int readNextId( String type ) {
		String query = "SELECT seq FROM sqlite_sequence WHERE name = \"" + type + "\"";
		int newId = -1;

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			while ( rs.next() ) {
				newId = rs.getInt( "seq" );
			}

			newId++ ;

			LOGGER.info( "Read new id from " + type + "= " + newId );

		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
		}

		return newId;
	}

	public int readGuestCount() {
		String query = "SELECT COUNT(*) FROM Guests";

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			int count = -1;

			while ( rs.next() ) {
				count = rs.getInt( "COUNT(*)" );
			}

			LOGGER.info( "Read count of Guests = " + count );

			return count;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return -1;
		}
	}

	public int readAgeCount( int fromAge, int toAge ) {
		String query = "SELECT * FROM Guests WHERE Age >= ? AND Age <= ?";

		int guestCounter = 0;

		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setInt( 1, fromAge );
			ps.setInt( 2, toAge );

			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {
				guestCounter++ ;
			}

			LOGGER.info( "Read age count from " + fromAge + " years to " + toAge + " years: " + guestCounter );

			return guestCounter;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return -1;
		}
	}

//	public boolean createContactData( ContactData contactData, ContactType type ) {
//		String query = "INSERT INTO " + type.getText() + " (name, street, plz, ort, phone, email) VALUES(?,?,?,?,?,?)";
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//			ps.setString( 1, contactData.getName() );
//			ps.setString( 2, contactData.getStreet() );
//			ps.setString( 3, contactData.getPlz() );
//			ps.setString( 4, contactData.getOrt() );
//			ps.setString( 5, contactData.getPhone() );
//			ps.setString( 6, contactData.getEmail() );
//			ps.executeUpdate();
//
//			LOGGER.info( "New " + type.getText() + " created!" );
//
//			return true;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}
//	}
//
//	public ContactData readContactData( ContactType type ) {
//		String query = "SELECT * FROM " + type.getText();
//
//		try {
//			Statement st = connection.createStatement();
//			ResultSet rs = st.executeQuery( query );
//
//			ContactData contactData = new ContactData();
//			contactData.setType( type );
//
//			while ( rs.next() ) {
//				contactData.setName( rs.getString( "Name" ) );
//				contactData.setStreet( rs.getString( "Street" ) );
//				contactData.setPlz( rs.getString( "Plz" ) );
//				contactData.setOrt( rs.getString( "Ort" ) );
//				contactData.setPhone( rs.getString( "Phone" ) );
//				contactData.setEmail( rs.getString( "Email" ) );
//			}
//
//			LOGGER.info( "Read " + type.getText() + " data!" );
//			return contactData;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return null;
//		}
//	}
//
//	public void deleteContactData( ContactType type ) {
//		String query = "DELETE FROM " + type.getText();
//
//		try {
//			PreparedStatement ps = connection.prepareStatement( query );
//
//			// execute the delete statement
//			ps.executeUpdate();
//
//			LOGGER.info( type.getText() + " data deleted!" );
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//		}
//	}
//
//	public boolean checkContactData( ContactType type ) {
//		String query = "SELECT COUNT(*) FROM " + type.getText();
//
//		try {
//			Statement st = connection.createStatement();
//			ResultSet rs = st.executeQuery( query );
//
//			int count = -1;
//
//			while ( rs.next() ) {
//				count = rs.getInt( "COUNT(*)" );
//			}
//
//			boolean locationDataValid = count != 0;
//
//			LOGGER.info( "Check " + type.getText() + " data: " + locationDataValid );
//
//			return locationDataValid;
//		} catch ( SQLException e ) {
//			LOGGER.warning( e.toString() );
//			return false;
//		}
//	}
}
