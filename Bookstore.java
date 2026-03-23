import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Bookstore 
{
	public static void main(String[] args) 
	{
		Connection conn = null;
		try 
		{
			// Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Connect to a database
			conn = DriverManager.getConnection("jdbc:sqlite:Bookstore.db");

			// To check the number of rows added to each table later on
			int bookRows = 0;
			int memberRows = 0;
			int boughtRows = 0;

			// Arrays with the attributes to create an instance of the Book table
			String[] ids = {"111111", "111112", "111113", "111114", "111115", "111116", "111117", "111118"};
			String[] titles = {"Little Women", "Anne of Green Gables", "Mistborn", "Klara and the Sun", "A Man Called Ove", "Nightshade Academy", "Cinder", "The Hunger Games"};
			String[] authors = {"Louisa May Alcott", "L. M. Montgomery", "Brandon Sanderson", "Kazuo Ishiguro", "Fredrik Backman", "Mel Torrefranca", "Marissa Meyer", "Suzanne Collins"};
			String[] genres = {"Coming-of-Age", "Coming-of-Age", "Fantasy", "Literary Fiction", "Literary Fiction", "Fantasy", "Fantasy", "Dystopian"};
			double[] prices = {12.00, 12.00, 14.99, 25.75, 18.99, 25.99, 21.00, 18.39};
			int[] copies = {10, 15, 8, 5, 6, 4, 9, 8};
			
			// Arrays with the attributes to create an instance of the Member table
			String[] memberIds = {"12345", "23456"};
			String[] names = {"Jane Doe", "John Doe"};
			String[] emails = {"jane.doe@gmail.com", "johndoe@gmail.com"};
			
			// Arrays with the attributes to create an instance of the Bought table
			String[] bMemberIds = {"12345", "12345", "23456", "23456"};
			String[] bIds = {"111111", "111112", "111113", "111111"};
			String[] dates = {"03/11/2026", "03/11/2026", "04/12/2026", "05/01/2026"};

			// Create a statement
			Statement stmt = conn.createStatement();
			
			// Design the database schema
			String sql = "create table if not exists book "
				+ "(ID			varchar(6), "
				+ " title			varchar(50) not null, "
				+ " author			varchar(20), "
				+ " genre			varchar(20), "
				+ " price			numeric(3,2) check (price >= 0), "
				+ " copies			numeric(4,0) check (copies >= 0), "
				+ " primary key (ID)"
				+ ")";
			
			String sql1 = "create table if not exists member "
				+ "(member_id		varchar(5), "
				+ " name			varchar(20), "
				+ " email			varchar(40), "
				+ " primary key (member_id)"
				+ ")";
				
			String sql2 = "create table if not exists bought "
				+ "(member_id		varchar(5), "
				+ " ID				varchar(6), "
				+ " date			varchar(10), "
				+ " primary key (ID, member_id), "
				+ " foreign key (member_id)	references member (member_id) on delete cascade, "
				+ " foreign key (ID) references book (ID) on delete cascade"
				+ ")";
				
			// Execute three CREATE queries
			stmt.executeUpdate(sql);
			System.out.println("Table 'Book' created successfully. ");
			
			stmt.executeUpdate(sql1);
			System.out.println("Table 'Member' created successfully. ");
			
			stmt.executeUpdate(sql2);
			System.out.println("Table 'Bought' created successfully. ");


			// Insert rows into Book table
			String sql3 = "insert or replace into book (ID, title, author, genre, price, copies) values(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(sql3);
				
			// Set parameters
			for (int i = 0; i < 8; i++)
			{
				pstmt.setString(1, ids[i]);
				pstmt.setString(2, titles[i]);
				pstmt.setString(3, authors[i]);
				pstmt.setString(4, genres[i]);
				pstmt.setDouble(5, prices[i]);
				pstmt.setInt(6, copies[i]);
				
				// Execute the INSERT queries
				bookRows += pstmt.executeUpdate();
			}
			// Check
			System.out.println(bookRows + " row(s) inserted into Book table. ");
			
			
			// Insert rows into Member table
			String sql4 = "insert or replace into Member (member_id, name, email) values(?, ?, ?)";
			
			PreparedStatement pstmt2 = conn.prepareStatement(sql4);
				
			// Set parameters
			for (int i = 0; i < 2; i++)
			{
				pstmt2.setString(1, memberIds[i]);
				pstmt2.setString(2, names[i]);
				pstmt2.setString(3, emails[i]);
				
				// Execute the INSERT queries
				memberRows += pstmt2.executeUpdate();
			}
			// Check
			System.out.println(memberRows + " row(s) inserted into Member table. ");
			
			
			// Insert rows into Bought table
			String sql5 = "insert or replace into bought (member_id, ID, date) values(?, ?, ?)";
			
			PreparedStatement pstmt3 = conn.prepareStatement(sql5);
				
			// Set parameters
			for (int i = 0; i < 4; i++)
			{
				pstmt3.setString(1, bMemberIds[i]);
				pstmt3.setString(2, bIds[i]);
				pstmt3.setString(3, dates[i]);
				
				// Execute the INSERT queries
				boughtRows += pstmt3.executeUpdate();
			}
			// Check
			System.out.println(boughtRows + " row(s) inserted into Bought table. ");
				
			
			// Execute a SELECT query
			ResultSet rs = stmt.executeQuery("select * from book");
			
			boolean bookHasRows = false;
			
			// Loop through the result set and print the results
			while (rs.next()) 
			{
				bookHasRows = true;
				System.out.println(
					"ID: " + rs.getString("ID") +
					", Title: " + rs.getString("title") +
					", Author: " + rs.getString("author") +
					", Genre: " + rs.getString("genre") +
					", Price: $" + rs.getDouble("price") +
					", Copies: " + rs.getInt("copies")
				);
			}
			
			if (!bookHasRows)
			{
				System.out.println("No rows found in book table.");
			}
			
			
			// Execute a SELECT query
			ResultSet rs1 = stmt.executeQuery("select * from member");
			
			boolean memberHasRows = false;
			
			// Loop through the result set and print the results
			while (rs1.next()) 
			{
				memberHasRows = true;
				System.out.println(
					"Member ID: " + rs1.getString("member_id") +
					", Name: " + rs1.getString("name") +
					", Email: " + rs1.getString("email")
				);
			}
			
			if (!memberHasRows)
			{
				System.out.println("No rows found in member table.");
			}
			
			
			// Execute a SELECT query
			ResultSet rs2 = stmt.executeQuery("select * from bought");
			
			boolean boughtHasRows = false;
			
			// Loop through the result set and print the results
			while (rs2.next()) 
			{
				boughtHasRows = true;
				System.out.println(
					"Member ID: " + rs2.getString("member_id") +
					", ID: " + rs2.getString("ID") +
					", Date: " + rs2.getString("date")
				);
			}
			
			if (!boughtHasRows)
			{
				System.out.println("No rows found in bought table.");
			}
			
			
			// Update statement
			String sql6 = "update book set copies = copies - (select count(*) from bought where bought.ID = book.id) where ID in (select id from bought)";
			
			//int newCopies = 9;
			//String idToUpdate = "111111";
			
			PreparedStatement pstmt4 = conn.prepareStatement(sql6);
			
			//pstmt4.setInt(1, newCopies);
			//pstmt4.setString(2, idToUpdate);
			
			int rowsAffected = pstmt4.executeUpdate();
			System.out.println(rowsAffected + " row(s) updated. ");
			
			
			// Execute a SELECT query
			ResultSet rs3 = stmt.executeQuery("select * from book");
			
			//boolean bookHasRows = false;
			
			// Loop through the result set and print the results
			while (rs3.next()) 
			{
				bookHasRows = true;
				System.out.println(
					"ID: " + rs3.getString("ID") +
					", Title: " + rs3.getString("title") +
					", Author: " + rs3.getString("author") +
					", Genre: " + rs3.getString("genre") +
					", Price: $" + rs3.getDouble("price") +
					", Copies: " + rs3.getInt("copies")
				);
			}
			
			if (!bookHasRows)
			{
				System.out.println("No rows found in book table.");
			}
			
			
			String sql7 = "delete from bought where member_id = ? and ID = ?";
			
			pstmt = conn.prepareStatement(sql7);
			
			pstmt.setString(1, memberIds[1]);
			pstmt.setString(2, ids[0]);
			
			rowsAffected = pstmt.executeUpdate();
			System.out.println(rowsAffected + " row(s) deleted. ");
			
			
			// Execute a SELECT query
			ResultSet rs4 = stmt.executeQuery("select * from bought");
			
			//boolean boughtHasRows = false;
			
			// Loop through the result set and print the results
			while (rs4.next()) 
			{
				boughtHasRows = true;
				System.out.println(
					"Member ID: " + rs4.getString("member_id") +
					", ID: " + rs4.getString("ID") +
					", Date: " + rs4.getString("date")
				);
			}
			
			if (!boughtHasRows)
			{
				System.out.println("No rows found in bought table.");
			}
			
		} 
		catch (SQLException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (conn != null) 
				{
				conn.close();
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
}