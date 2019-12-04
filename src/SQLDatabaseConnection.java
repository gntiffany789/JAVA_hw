

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class SQLDatabaseConnection {
	
	
	
	private static final String DEFAULT_QUERY =  "select productid,productname,categoryid,unitprice from products";
	private static final String DEFAULT_RESULT = "RESULT";
	private static String query = "";
	private static String result = "";
	private static ArrayList<String> allResult;
	private static String title = "";
	

	public static void main(String[] args) {
	
		 
				allResult = new ArrayList<String>();
		
				
				final JTextArea queryArea = new JTextArea(DEFAULT_QUERY,3,100);
				queryArea.setWrapStyleWord(true);
				queryArea.setLineWrap(true);
				
				
				final JTextArea resultArea = new JTextArea(DEFAULT_RESULT,11,100);
				resultArea.setWrapStyleWord(true);
				resultArea.setLineWrap(true);
				resultArea.setEditable(false);
				
				
				JScrollPane scrollPane = new JScrollPane(queryArea
						, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
						, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
				
				
				
				JButton submitButton = new JButton("Submit Query");
				submitButton.addActionListener(
						new ActionListener() {
				            public void actionPerformed(ActionEvent e) {
				            	
				            	allResult.clear();
				            	resultArea.setText("");
				            	title = "";
				            	result = "";
				            	query = "";
				            	query = queryArea.getText();
				            	
				            	connectSQL(query);
				            	
				            	
				            	for (String i:allResult) {
									
									result = result + i ;
								}
				            	
				            	resultArea.setText(title+"\n"+result);
				            }
				        }	
				);
				
				
				
				Box boxNorth = Box.createHorizontalBox();
				boxNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
				boxNorth.add(scrollPane);
				boxNorth.add(submitButton);
				


				
			
				JScrollPane scrollPane2 = new JScrollPane(resultArea
						, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
						, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			
				Box boxSouth = Box.createHorizontalBox();
				boxSouth.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
				boxSouth.add(scrollPane2);
				

				
				JFrame window = new JFrame("Displaying Query Results");
				window.setLocationRelativeTo(null);
				window.add(boxNorth, BorderLayout.NORTH);
				window.add(boxSouth, BorderLayout.SOUTH);
				
				window.setSize(600, 300);
				window.setVisible(true);
				
				
	}
	
	

	public static void connectSQL(String s1) {
		ResultSet resultSet = null;
		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
		    Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;"
		    								+ "databaseName=Northwind;user=sa;password=as;");
		             
		    DatabaseMetaData metadata = conn.getMetaData();   
		    System.out.println("Database Name: "+ metadata.getDatabaseProductName());
		    
		    
		    Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		    String selectSql = s1;
		    resultSet = statement.executeQuery(selectSql);
		    
		    
		    ResultSetMetaData rsmd = resultSet.getMetaData();
		    System.out.println(rsmd.getColumnCount());
		    
		    int count = rsmd.getColumnCount();
		    for (int i = 1; i <= count; i++) {
		    	title = title +"  -  "+rsmd.getColumnName(i)+"  -  ";
		    }
		    while(resultSet.next()) {
		    	
		    	for (int i = 1; i <= count; i++) {
			    	allResult.add("  [  "+resultSet.getString(i)+"  ]  ");
			    	if (i==rsmd.getColumnCount()) {
			    		allResult.add("\n");
			    	}
			    	
				}
		    	

		    }
		    
		    
		   
		    
		   

		    
		             
		} catch (Exception e) {
		   e.printStackTrace();
		   result = "Database Error:\n"+e.toString();
		} 
	}

}
