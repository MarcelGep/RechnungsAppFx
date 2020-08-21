package com.gepraegs.rechnungsAppFx.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gepraegs.rechnungsAppFx.*;

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

	private boolean updateSqliteSeq()
	{
		int customerCount = readCustomerCount();
		String query = "UPDATE sqlite_sequence SET seq = ? WHERE name = 'Customers'";

		try{
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, customerCount);
			ps.executeUpdate();

			LOGGER.info("Update slite sequence from Customers to " + customerCount);

			return true;
		} catch (SQLException e){
			LOGGER.warning(e.toString());
			return false;
		}
	}

	public boolean createCustomer(Customer customer ) {
		String query = "INSERT INTO Customers(Company, Name1, Name2, Street, Plz, Location, Country, Phone, Fax, Email, Website, Discount, PayedCosts, Handy, OpenCosts, Informations) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setString( 1, customer.getCompany().getValue());
			ps.setString( 2, customer.getName1().getValue());
			ps.setString( 3, customer.getName2().getValue());
			ps.setString( 4, customer.getStreet().getValue());
			ps.setString( 5, customer.getPlz().getValue());
			ps.setString( 6, customer.getLocation().getValue());
			ps.setString( 7, customer.getCountry().getValue());
			ps.setString( 8, customer.getPhone().getValue());
			ps.setString( 9, customer.getFax().getValue());
			ps.setString( 10, customer.getEmail().getValue());
			ps.setString( 11, customer.getWebsite().getValue());
			ps.setDouble( 12, customer.getDiscount());
			ps.setDouble( 13, customer.getPayedCosts());
			ps.setString( 14, customer.getHandy().getValue());
			ps.setDouble( 15, customer.getOpenCosts());
			ps.setString( 16, customer.getInformations().getValue());
			ps.executeUpdate();

			LOGGER.info( "New customer created!" );

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
		}
		return false;
	}

	public boolean editCustomer(Customer customer ) {
		String query = "UPDATE Customers SET Company = ?, Name1 = ?, Name2 = ?, Street = ?, Plz = ?, Location = ?," +
				       "Country = ?, Phone = ?, Fax = ?, Email = ?, Website = ?, Discount = ?, PayedCosts = ?, OpenCosts = ?, Handy = ?, Informations = ? WHERE KdNr = ?";

		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setString( 1, customer.getCompany().getValue());
			ps.setString( 2, customer.getName1().getValue());
			ps.setString( 3, customer.getName2().getValue());
			ps.setString( 4, customer.getStreet().getValue());
			ps.setString( 5, customer.getPlz().getValue());
			ps.setString( 6, customer.getLocation().getValue());
			ps.setString( 7, customer.getCountry().getValue());
			ps.setString( 8, customer.getPhone().getValue());
			ps.setString( 9, customer.getFax().getValue());
			ps.setString( 10, customer.getEmail().getValue());
			ps.setString( 11, customer.getWebsite().getValue());
			ps.setDouble( 12, customer.getDiscount());
			ps.setDouble( 13, customer.getPayedCosts());
			ps.setDouble( 15, customer.getOpenCosts());
			ps.setString( 14, customer.getHandy().getValue());
			ps.setString( 16, customer.getInformations().getValue());
			ps.setString(17, customer.getKdNr().getValue());
			ps.executeUpdate();

			LOGGER.info("Customer with KdNr: " + customer.getKdNr().getValue() + " updated!");

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
		}
		return false;
	}

	public boolean deleteCustomer(Customer customer ) {
		try {
			String query = "DELETE FROM Customers WHERE KdNr = ?";
			int kdNr = Integer.parseInt(customer.getKdNr().getValue());

			PreparedStatement ps = connection.prepareStatement( query );
			ps.setInt(1, kdNr);
			ps.executeUpdate();

			LOGGER.info( "Customer \"" + customer.getCompany().getValue() +
							 "\" with KdNr \"" + customer.getKdNr().getValue() +
					  	     "\" was deleted!" );

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return false;
		}
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

	public Customer readCustomer(int kdNr) {
		String query = "SELECT * FROM Customers WHERE KdNr = ?";
		Customer customer = null;
		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setInt( 1, kdNr );

			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {
				String company = rs.getString("Company");
				String name1 = rs.getString("Name1");
				String name2 = rs.getString("Name2");
				String street = rs.getString("Street");
				String plz = rs.getString("Plz");
				String location = rs.getString("Location");
				String country = rs.getString("Country");
				String phone = rs.getString("Phone");
				String handy = rs.getString("Handy");
				String fax = rs.getString("Fax");
				String email = rs.getString("Email");
				String website = rs.getString("Website");
				String informations = rs.getString("Informations");
				int discount = rs.getInt("Discount");
				double openCosts = rs.getDouble("OpenCosts");
				double payedCosts = rs.getDouble("PayedCosts");

				customer = new Customer(String.valueOf(kdNr), company, name1, name2, street, plz, location, country, phone, handy, fax,
										email, website, discount);

				customer.setInformations(informations);
				customer.setOpenCosts(openCosts);
				customer.setPayedCosts(payedCosts);
			}

			LOGGER.info( "Read customer with KdNr: " + kdNr );
			return customer;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return null;
		}
	}

	public List<Customer> readCustomers() {
		String query = "SELECT * FROM Customers";

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			List<Customer> customerData = new ArrayList<>();

			while ( rs.next() ) {
				String kdNr = rs.getString( "KdNr" );
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
				int discount = rs.getInt( "Discount" );
				double openCosts = rs.getDouble( "OpenCosts" );
				double payedCosts = rs.getDouble( "PayedCosts" );

				Customer customer = new Customer(kdNr, company, name1, name2, street, plz, location, country, phone, handy, fax,
						email, website, discount);

				customer.setInformations(informations);
				customer.setOpenCosts(openCosts);
				customer.setPayedCosts(payedCosts);

				customerData.add( customer );
			}
			LOGGER.info( "Read all customers!" );
			return customerData;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return null;
		}
	}

	public boolean deleteProduct(Product product) {
		try {
			String query = "DELETE FROM Products WHERE ArtNr = ?";
			int artNr = Integer.parseInt(product.getArtNr());

			PreparedStatement ps = connection.prepareStatement( query );
			ps.setInt(1, artNr);
			ps.executeUpdate();

			LOGGER.info( "Product \"" + product.getName() +
					"\" with KdNr \"" + product.getArtNr() +
					"\" was deleted!" );

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return false;
		}
	}

	public List<Product> readProducts() {
		String query = "SELECT * FROM Products";

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			List<Product> productData = new ArrayList<>();

			while ( rs.next() ) {
				String artNr = rs.getString( "ArtNr" );
				String name = rs.getString( "Name" );
				String unit = rs.getString( "Unit" );
				double price = rs.getDouble( "Price" );
				double ust = rs.getDouble( "Ust" );

				Product product = new Product(artNr, name, unit, ust, price);

				productData.add( product );
			}
			LOGGER.info( "Read all products!" );
			return productData;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return null;
		}
	}

	public boolean createProduct(Product product) {
		String query = "INSERT INTO Products(Name, Unit, Price, Ust) VALUES(?,?,?,?)";

		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setString( 1, product.getName());
			ps.setString( 2, product.getUnit());
			ps.setDouble( 3, product.getPriceExcl());
			ps.setDouble( 4, product.getUst());

			ps.executeUpdate();

			LOGGER.info( "New customer created!" );

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
		}
		return false;
	}

	public boolean editProduct( Product product ) {
		String query = "UPDATE Products SET Name = ?, Unit = ?, Price = ?, Ust = ? WHERE ArtNr = ?";

		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setString( 1, product.getName());
			ps.setString( 2, product.getUnit());
			ps.setDouble( 3, product.getPriceExcl());
			ps.setDouble( 4, product.getUst());
			ps.setString(5, product.getArtNr());
			ps.executeUpdate();

			LOGGER.info("Product with ArtNr: " + product.getArtNr() + " updated!");

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
		}
		return false;
	}

	public int readNextId()
	{
		String query = "SELECT * FROM sqlite_sequence";
		int newId = -1;

		try{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while(rs.next())
			{
				newId = rs.getInt("seq");
			}

			newId++;

			LOGGER.info("Read new id = " + newId);

		} catch (SQLException e){
			LOGGER.warning(e.toString());
		}

		return newId;
	}

	public List<Invoice> readInvoices() {
		String query = "SELECT * FROM Invoices";

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			List<Invoice> invoiceData = new ArrayList<>();

			while ( rs.next() ) {
				String reNr = rs.getString( "ReNr" );
				int kdNr = rs.getInt( "KdNr" );
				String createdDate = rs.getString( "CreatedDate" );
				String dueDate = rs.getString( "DueDate" );
				String payedDate = rs.getString( "PayedDate" );
				String skonto = rs.getString( "Skonto" );
				boolean state = rs.getInt( "State" ) == 1 ? true : false;
				double totalPrice = rs.getDouble( "TotalPrice" );
				double ust = rs.getDouble( "USt" );

				Invoice invoice = new Invoice(reNr, readCustomer(kdNr), createdDate, dueDate, payedDate, state, totalPrice,  ust);

				invoiceData.add(invoice);
			}
			LOGGER.info( "Read all invoices!" );
			return invoiceData;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return null;
		}
	}

	public boolean createInvoice(Invoice invoice) {
		String query = "INSERT INTO Invoices(ReNr, KdNr, CreatedDate, DueDate, PayedDate, State, USt, TotalPrice) VALUES(?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setString( 1, invoice.getReNr());
			ps.setString( 2, invoice.getCustomer().getKdNr().getValue());
			ps.setString( 3, invoice.getCreateDate());
			ps.setString( 4, invoice.getDueDate());
			ps.setString( 5, invoice.getPayedDate());
			ps.setBoolean( 6, invoice.isState());
			ps.setDouble( 7, invoice.getUst());
			ps.setDouble( 8, invoice.getTotalPrice());

			ps.executeUpdate();

			LOGGER.info( "New invoice created!" );

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
		}
		return false;
	}

	public boolean editInvoice(Invoice invoice) {
		String query = "UPDATE Invoices SET KdNr = ?, CreatedDate = ?, DueDate = ?, PayedDate = ?, State = ?, USt = ?, TotalPrice = ? WHERE ReNr = ?";

		try {
			PreparedStatement ps = connection.prepareStatement( query );
			ps.setString( 1, invoice.getCustomer().getKdNr().getValue());
			ps.setString( 2, invoice.getCreateDate());
			ps.setString( 3, invoice.getDueDate());
			ps.setString( 3, invoice.getPayedDate());
			ps.setBoolean( 3, invoice.isState());
			ps.setDouble( 4, invoice.getUst());
			ps.setDouble( 4, invoice.getTotalPrice());
			ps.setString( 4, invoice.getReNr());
			ps.executeUpdate();

			LOGGER.info("Invoice with ReNr: " + invoice.getReNr() + " updated!");

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
		}
		return false;
	}

	public boolean deleteInvoice(Invoice invoice) {
		try {
			String query = "DELETE FROM Invoices WHERE ReNr = ?";
			int reNr = Integer.parseInt(invoice.getReNr());

			PreparedStatement ps = connection.prepareStatement( query );
			ps.setInt(1, reNr);
			ps.executeUpdate();

			LOGGER.info( "Invoice with ReNr \"" + invoice.getReNr() + "\" was deleted!" );

			return true;
		} catch ( SQLException e ) {
			LOGGER.warning( e.toString() );
			return false;
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

	public int readCustomerCount() {
		String query = "SELECT COUNT(*) FROM Customers";

		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery( query );

			int count = -1;

			while ( rs.next() ) {
				count = rs.getInt( "COUNT(*)" );
			}

			LOGGER.info( "Read count of Customers = " + count );

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
