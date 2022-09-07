package banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class bankManagement { // these class provides all
							// bank method

	private static final int NULL = 0;

	static Connection con = connection.getConnection();
	static String sql = "";
	public static boolean
	createAccount(String name,int passCode) // create account function
	{
		try {
			// validation
			if (name == "" || passCode == NULL) {
				System.out.println("All Field Required!");
				return false;
			}
			// query
			Statement st = con.createStatement();
			sql = "INSERT INTO customer(cname,balance,pass_code) values('"+ name + "',1000," + passCode + ")";

			// Execution
			if (st.executeUpdate(sql) == 1) {
				System.out.println(name + ", Now You Login!");
				return true;
			}
			// return
		}
		catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Username Not Available!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean loginAccount(String name, int passCode) // login method
	{
		try {
			// validation
			if (name == "" || passCode == NULL) {
				System.out.println("All Field Required!");
				return false;
			}
			
			sql = "select * from customer where cname='"
				+ name + "' and pass_code=" + passCode;
			PreparedStatement st= con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

			if (rs.next()) {
				

				int ch = 5;
				int amt = 0;
				int acNo = rs.getInt("ac_no");
				//int balance= rs.getInt("balance");
				
				
				while (true) {
					try {
						System.out.println("Hello, "+ rs.getString("cname"));
						
						System.out.println("2)View Balance");
						System.out.println("3)Withdraw");
						System.out.println("4)Deposit");
						System.out.println("5)LogOut");

						System.out.print("Enter Choice:");
						ch = Integer.parseInt(sc.readLine());
						if (ch == 1) {
							
							System.out.print("Enter Amount:");
							amt = Integer.parseInt(sc.readLine());

							
						}
						else if (ch == 2) {

							bankManagement.getBalance(acNo);
						}
						else if (ch == 3) {

							System.out.print("Enter Amount:");
							amt = Integer.parseInt(sc.readLine());
							System.out.print("Enter Account Number:");
							acNo = Integer.parseInt(sc.readLine());
							bankManagement.Withdraw(acNo,amt);
							
							
						}
						else if (ch == 4) {

							System.out.print("Enter Amount:");
							amt = Integer.parseInt(sc.readLine());
							System.out.print("Enter Account Number:");
							acNo = Integer.parseInt(sc.readLine());
							bankManagement.Deposit(acNo,amt);
							
						}
						
						else if (ch == 5) {
							break;
						}
						else {
							System.out.println("Err : Enter Valid input!\n");
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else {
				return false;
			}
			
			return true;
		}
		catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Username Not Available!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void getBalance(int acNo) // fetch balance method
	{
		try {

			
			sql = "select * from customer where ac_no="+ acNo;
			PreparedStatement st= con.prepareStatement(sql);

			ResultSet rs = st.executeQuery(sql);
			System.out.println(
				"-----------------------------------------------------------");
			System.out.printf("%12s %10s %10s\n","Account No", "Name","Balance");

			

			while (rs.next()) {
				System.out.printf("%12d %10s %10d.00\n",
								rs.getInt("ac_no"),
								rs.getString("cname"),
								rs.getInt("balance"));
			}
			System.out.println(
				"-----------------------------------------------------------\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//Withdraw
	public static boolean Withdraw(int acNo,int amt) throws SQLException
	{
		if (acNo== NULL || amt == NULL) {
            System.out.println("All Fields Required!");
            return false;
		}
		
            
            try {
                con.setAutoCommit(false);
                sql = "select * from customer where ac_no="+ acNo;
                PreparedStatement ps= con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
     
                if (rs.next()) {
                    if (rs.getInt("balance") < amt) {
                        System.out.println(
                            "Insufficient Balance!");
                        return false;
                    }
                }
     
                Statement st = con.createStatement();
     
                // debit
                con.setSavepoint();
     
                sql = "update customer set balance=balance-"+ amt + " where ac_no=" + acNo;
                if (st.executeUpdate(sql) == 1) {
                    System.out.println("Amount Withdrawn!");
                }
     
                
     
                con.commit();
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                con.rollback();
            }
            // return
            return false;
        
       }
	public static boolean Deposit(int acNo,int amt) throws SQLException
	{
		if (acNo== NULL || amt == NULL) {
            System.out.println("All Fields Required!");
            return false;
		}
		
            
            try {
                con.setAutoCommit(false);
                /*sql = "select * from customer where ac_no="+ acNo;
                PreparedStatement ps= con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    if (rs.getInt("balance") < amt) {
                        System.out.println("Insufficient Balance!");
                        return false;
                    }
                }*/
               
                Statement st = con.createStatement();
                con.setSavepoint();
     
                sql = "update customer set balance=balance+"+ amt + " where ac_no=" + acNo;
                if (st.executeUpdate(sql) == 1) {
                    System.out.println("Amount Deposited!");
                }
     
                
     
                con.commit();
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                con.rollback();
            }
            // return
            return false;
        
       }
	}


