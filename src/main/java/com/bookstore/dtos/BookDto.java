package com.bookstore.dtos;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.models.Book;
import com.ibm.db2.jcc.am.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookDto {
	private String databaseURL;
	private String username;
	private String password;
	private Connection jdbcConnection;
	
	
	public BookDto(String databaseURL, String username, String password) {
		super();
		this.databaseURL = databaseURL;
		this.username = username;
		this.password = password;
	}
	
	protected void connect() throws SQLException{
		if(jdbcConnection == null || jdbcConnection.isClosed()) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			throw new SQLException(e);
		}
		 jdbcConnection = (Connection) DriverManager.getConnection(databaseURL, username, password);
		}
	}
	
	protected void deconnect() throws SQLException {
		if(jdbcConnection != null  || !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	public boolean insertBook(Book book) throws SQLException {
		String sql = "INSERT INTO (title, author, price) VALUES(?,?,?)";
		connect();
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, book.getTitle());
		statement.setString(2, book.getAuthor());
		statement.setDouble(1, book.getPrice());
		
		boolean isInserted = statement.executeUpdate() > 0 ; 
		statement.close();
		deconnect();
		return isInserted;
	}
	
	
	public List<Book> listBooks() throws SQLException{
		String sql = "Select * from book";
		List<Book> books = new ArrayList<>();
		
		connect();
		Statement st = jdbcConnection.createStatement();
		ResultSet resultSet = st.executeQuery(sql);
		while(resultSet.next()) {
			Book book = new Book(
					resultSet.getInt("id"),
					resultSet.getString("title"),
					resultSet.getString("author"),
					resultSet.getDouble("price")
					);
			
			books.add(book);
		}
		
		resultSet.close();
		st.close();
		deconnect();
		
		return books;
	}
	
	public boolean updateBook(Book newBookInfo) throws SQLException{
		String sql = "UPDATE book set title=?, author=?, price=? where id=?"
	}
	
	
	
	
	
	
	

}
