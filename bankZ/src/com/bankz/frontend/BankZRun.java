package com.bankz.frontend;




import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import com.bankz.backend.Logic;
import com.bankz.helper.Supplement;
import com.bankz.persistence.CustomerPersistence;
import com.bankz.persistence.DatabaseTasks;
import com.bankz.persistence.QueryBuilder;
import com.bankz.pojo.Account;
import com.bankz.pojo.Branch;
import com.bankz.pojo.Customer;
import com.bankz.pojo.Employee;
import com.bankz.pojo.Transaction;
import com.bankz.pojo.User;
import com.bankz.utilities.BankException;
import com.bankz.utilities.InvalidInputException;



public class BankZRun {
	private static Scanner scan=new Scanner(System.in);
	private static QueryBuilder queryBuilder=new QueryBuilder();
	private static CustomerPersistence cp=new CustomerPersistence();
	private static Logger logger = Logger.getAnonymousLogger();
	private static Logic logic=new Logic();
	private static DatabaseTasks dbTasks=new DatabaseTasks("jdbc:mysql://localhost:3306/bankZ","root","hari03@mySql");
	public static void main(String... args) {
		
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
		System.out.println("||                                        ||");
		System.out.println("||                                        ||");
		System.out.println("||                 bank Z!                ||");
		System.out.println("||                                        ||");
		System.out.println("||                                        ||");
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
		
		
		try {
			
			
			User user=login();
			switch (user.getType()) {
			case "Customer":
				customer(user);
				break;
			case "Employee":
				employee(user);
				break;
			case "Admin":
				admin(user);
				break;
			default:
				
				break;
			}
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		
		}
	
	private static void customer(User user) {

		System.out.println("////////////////////////////////////////");
		System.out.println("        Welcome "+user.getName()+"      ");
		System.out.println("////////////////////////////////////////");
		int x=1;
		while(x==1) {
		System.out.println("1. View Account details\n"
				+ "2. View Personal info\n"
				+ "3. Deposit amount\n"
				+ "4. Mini statement\n"
				+ "5. Change password\n"
				+ "6. Check Status\n"
				+ "7. Money Transfer\n"
				+ "8. Logout");
		int choice=getInteger("Enter your choice");
		switch(choice) {
		case 1:
			try {
				Account account= logic.getAccount(user.getId());
				System.out.println("Account details:");
				System.out.println("Account Number: "+account.getAccountNum());
				System.out.println("Account Holder Name: "+user.getName());
				System.out.println("Type: "+account.getType());
				System.out.println("Status: "+account.getStatus());
				
			} catch (Exception e) {
				e.printStackTrace();
			}break;
		case 2:
			try {
				Customer customer=logic.getCustomerPersonalInfo(user.getId());
				System.out.println("Personal Info");
				System.out.println("Account holder Id: "+customer.getId());
				System.out.println("Account Holder Name: "+customer.getName());
				System.out.println("DOB: "+Supplement.longToDate(customer.getDob()));
				System.out.println("Email: "+customer.getEmail());
				System.out.println("Address: "+customer.getAddress());
				System.out.println("Contact Number: "+customer.getContactNum());
				System.out.println("Aadhar Number: "+customer.getAadharNum());
				System.out.println("PAN Number: "+customer.getPanNum());
				
			} catch (Exception e) {
				e.printStackTrace();
			}break;
			
		case 3:
			try {
				if(user.getStatus().equals("ACTIVE")) {
					System.out.println("Amount Deposition...");
					int amount=getInteger("Enter the Amount: ");
					logic.deposit(user.getId(), amount);
				}else {
					throw new BankException("Your id is INACTIVE");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}break;
			
		case 4:
			try {
				System.out.println("Mini-statement");
				List<Transaction> transactionsList= logic.getTransactionDetails(user.getId());
				for(Transaction transaction:transactionsList) {
					System.out.println("Transaction Type: "+transaction.getTranscationType());
					System.out.println("Total Amount: "+transaction.getTranscationAmount());
					System.out.println("Account Balance"+transaction.getBalance());
					System.out.println("Account number: "+transaction.getPrimaryAccNum());
					if (transaction.getTranscationType().equals("Withdrawal")||transaction.getTranscationType().equals("Deposit")) {
						System.out.println("Receiver Account Number: "+ transaction.getPrimaryAccNum());
					}else {
						System.out.println("Receier Account number: "+transaction.getSecondaryAccNum());
					}
					System.out.println("Time: "+transaction.getTimeStamp());
					System.out.println("Description: "+transaction.getDescription());
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}break;
			
		case 5:
			try {
				if(user.getStatus().equals("ACTIVE")) {
					System.out.println("Change the Password!");
					String newPassword=getString("Enter the new Password:");
					logic.changePassword(user.getId(), newPassword);
					
				}else {
					throw new BankException("Your id is INACTIVE");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}break;
			
		case 6:
			try {
				System.out.println("Check the Status:");
				System.out.println("Your Account is: "+cp.checkStatus(user.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}break;
			
		case 7:
			try {
				if(user.getStatus().equals("ACTIVE")) {
					System.out.println("Direct Transfer to another Account");
					int amount=getInteger("Enter the Amount: ");
					long receiverAccountNum=(long)getInteger("Enter the Account Number: ");
					String description=getString("Notes On the Transaction");
					logic.moneyTransfer(user.getId(), amount,receiverAccountNum, description);
				}else {
					throw new BankException("Your id is INACTIVE");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}break;
		case 8:
			x=2;
			break;
		}
		}
	}
	
	private static void employee(User user) {
		System.out.println("////////////////////////////////////////");
		System.out.println("      Welcome "+user.getName());
		System.out.println("////////////////////////////////////////");
		int x=1;
		while(x==1) {
			System.out.println("1. View Personal Info\n"							
					+ "2. Change Personal Info\n"								
					+ "3. Add Customer\n"										
					+ "4. View Customer Info\n"										
					+ "5. Remove Customer\n"									
					+ "6. Add Account\n"									
					+ "7. View Account Info\n"										
					+ "8. Remove Account\n"							
					+ "9. Block Account\n"							
					+ "10. View Branch Info\n"						
					+ "11. View Transaction Info\n"									
					+ "12. View Other Employee Details\n"													
					+ "13. Change Password\n"
					+ "14. Check Status\n"
					+ "15. Logout");											
			int choice=getInteger("Enter your choice");							
			
			switch(choice) {
				case 1:
					try {
						
						Employee employee=logic.getEmployeePersonalInfo(user.getId());
						System.out.println("Personal Info");
						System.out.println("EmployeeId: "+employee.getId());
						System.out.println("Employee Name: "+employee.getName());
						System.out.println("DOB: "+Supplement.longToDate(employee.getDob()));
						System.out.println("Email: "+employee.getEmail());
						System.out.println("Address: "+employee.getAddress());
						System.out.println("Contact Number: "+employee.getContactNum());
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 2:
					try {
						System.out.println("Change Personal Info");
						int fieldChoice=getInteger("1. Name\n 2. DOB\n 3.Email\n 4.Address\n 5.Contact Number");
						int userId=user.getId();
						switch(fieldChoice) {
						case 1:
							String newName=getString("The New Name: ");
							logic.modifyInfo(userId,"Name",newName);
							break;
						case 2:
							String dob=getString("The Correct DoB is: ");
							logic.modifyInfo(userId,"DOB",Supplement.dateToLong(dob));
							break;
						case 3:
							String emailId=getString("The Correct emailId: ");
							logic.modifyInfo(userId, "Email", emailId);
							break;
						case 4:
							String address=getString("The Correct Address is: ");
							logic.modifyInfo(userId, "Address", address);
						case 5:
							long contactNum=getLong("The Correct Conatact Number: ");
							logic.modifyInfo(userId, "Contact Number", contactNum);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
						
					
					
				case 3:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							Customer customer=new Customer();
							customer.setId(getInteger("The Id for the Customer: "));
							String password=getString("Your Password: ");
							customer.setPassword(logic.encryptPassword(password));
							customer.setName(getString("Full Name of the Customer: "));
							customer.setEmail(getString("Email id: "));
							customer.setDob(Supplement.dateToLong(getString("The Dob(yyyy-mm-dd): ")));
							customer.setAddress(getString("The Address: "));
							customer.setType("Customer");
							customer.setStatus("ACTIVE");
							customer.setContactNum(getLong("The Contact Num: "));
							customer.setAadharNum(getLong("Aadhar Number: "));
							customer.setPanNum(getString("PAN number: "));
							logic.addCustomer(customer);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 4:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Details of a Customer");
							Customer customer=logic.getCustomerPersonalInfo(getInteger("The Id of the Customer: "));
							System.out.println("Personal Info");
							System.out.println("Account holder Id: "+customer.getId());
							System.out.println("Account Holder Name: "+customer.getName());
							System.out.println("DOB: "+Supplement.longToDate(customer.getDob()));
							System.out.println("Email: "+customer.getEmail());
							System.out.println("Address: "+customer.getAddress());
							System.out.println("Contact Number: "+customer.getContactNum());
							System.out.println("Aadhar Number: "+customer.getAadharNum());
							System.out.println("PAN Number: "+customer.getPanNum());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 5:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Removing Customer: ");
							logic.removeCustomer(getInteger("The employeeId of the Customer: "));
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 6:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Add an Account");
							Account account=new Account();
							account.setAccountNum(getLong("New Account Number: "));
							account.setCustomerId(getInteger("Customer id: "));
							account.setType(getString("Type: "));
							account.setBalance(getInteger("Initial Balance: "));
							account.setStatus("Active");
							account.setBranchId(getInteger("The branch id: "));
							logic.addAccount(account);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 7:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							int employeeId=getInteger("Enter the CustomerId of the Account");
							Account account= logic.getAccount(employeeId);
							System.out.println("Account details:");
							System.out.println("Account Number: "+account.getAccountNum());
							System.out.println("Account Holder Name: "+user.getName());
							System.out.println("Type: "+account.getType());
							System.out.println("Status: "+account.getStatus());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 8:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							long accountNum=getLong("Enter the Account Number:");
							logic.removeAccount(accountNum);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 9:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							long accountNum=(long)getInteger("Enter the Account Number: ");
							logic.blockAccount(accountNum);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 10:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Details of your Branch");
							Branch branch=logic.getBranchDetails(getInteger("The BranchId: "));
							System.out.println("The Branch Id: "+branch.getBranchId());
							System.out.println("Name: "+branch.getName());
							System.out.println("IFSC code: "+branch.getiFSCcode());
							System.out.println("Address: "+branch.getAddress());
							System.out.println("Active Employees: "+branch.getNumOfActiveEmployees());
							System.out.println("Active Customers: "+branch.getNumOfActiveCustomers());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 11:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Transaction Details");
							int customerId=getInteger("The Customer id: ");
							List<Transaction> transactionsList=logic.getTransactionDetails(customerId);
							for(Transaction transaction:transactionsList) {
								System.out.println("Transaction Type: "+transaction.getTranscationType());
								System.out.println("Total Amount: "+transaction.getTranscationAmount());
								System.out.println("Account Balance"+transaction.getBalance());
								System.out.println("Account number: "+transaction.getPrimaryAccNum());
								if (transaction.getTranscationType().equals("Withdrawal")||transaction.getTranscationType().equals("Deposit")) {
									System.out.println("Receiver Account Number: "+ transaction.getPrimaryAccNum());
								}else {
									System.out.println("Receier Account number: "+transaction.getSecondaryAccNum());
								}
								System.out.println("Time: "+transaction.getTimeStamp());
								System.out.println("Description: "+transaction.getDescription());
								
							}
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 12:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Employee Details...");
							int employeeId=getInteger("Enter the employee id: ");
							Employee employee=logic.getEmployeePersonalInfo(employeeId);
							System.out.println("Personal Info");
							System.out.println("EmployeeId: "+employee.getId());
							System.out.println("Employee Name: "+employee.getName());
							System.out.println("DOB: "+Supplement.longToDate(employee.getDob()));
							System.out.println("Email: "+employee.getEmail());
							System.out.println("Address: "+employee.getAddress());
							System.out.println("Contact Number: "+employee.getContactNum());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 13:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Change the Password!");
							logic.changePassword(user.getId(), getString("Enter the new Password:"));
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 14:
					try {
						System.out.println("Check the Status:");
						System.out.println("You are: "+logic.checkStatus(user.getId()));
					} catch (Exception e) {
						e.printStackTrace();
					}break;	
				
				case 15:
					x=2;
					break;
			}
		}
	}
	
	private static void admin(User user) {
		System.out.println("////////////////////////////////////////");
		System.out.println("      Welcome "+user.getName());
		System.out.println("////////////////////////////////////////");
		int x=1;
		while(x==1) {
			System.out.println("1. View Personal Info\n"							
					+ "2. Change Personal Info\n"								
					+ "3. Add Customer\n"										
					+ "4. View Customer Info\n"										
					+ "5. Remove Customer\n"
					+ "6. Add Employee\n"
					+ "7. View Employee Info\n"
					+ "8. Remove Employee\n"									
					+ "9. Add Account\n"									
					+ "10. View Account Info\n"										
					+ "11. Remove Account\n"							
					+ "12. Block Account\n"							
					+ "13. View Branch Info\n"						
					+ "14. View Transaction Info\n"									
					+ "15. View Other Employee Details\n"													
					+ "16. Change Password\n"
					+ "17. Check Status\n"
					+ "18. Logout");											
			int choice=getInteger("Enter your choice");							
			
			switch(choice) {
				case 1:
					try {
						
						Employee employee=logic.getEmployeePersonalInfo(user.getId());
						System.out.println("Personal Info");
						System.out.println("EmployeeId: "+employee.getId());
						System.out.println("Employee Name: "+employee.getName());
						System.out.println("DOB: "+Supplement.longToDate(employee.getDob()));
						System.out.println("Email: "+employee.getEmail());
						System.out.println("Address: "+employee.getAddress());
						System.out.println("Contact Number: "+employee.getContactNum());
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 2:
					try {
						System.out.println("Change Personal Info");
						int fieldChoice=getInteger("1. Name\n 2. DOB\n 3.Email\n 4.Address\n 5.Contact Number");
						int userId=user.getId();
						switch(fieldChoice) {
						case 1:
							String newName=getString("The New Name: ");
							logic.modifyInfo(userId,"Name",newName);
							break;
						case 2:
							String dob=getString("The Correct DoB is(yyyy-mm-dd): ");
							logic.modifyInfo(userId,"DOB",Supplement.dateToLong(dob));
							break;
						case 3:
							String emailId=getString("The Correct emailId: ");
							logic.modifyInfo(userId, "Email", emailId);
							break;
						case 4:
							String address=getString("The Correct Address is: ");
							logic.modifyInfo(userId, "Address", address);
						case 5:
							long contactNum=getLong("The Correct Conatact Number: ");
							logic.modifyInfo(userId, "Contact Number", contactNum);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}break;
						
					
					
				case 3:
					try {
						if (user.getStatus().equals("ACTIVE")) {
							Customer customer=new Customer();
							customer.setId(getInteger("The Id for the Customer: "));
							
							customer.setPassword(logic.encryptPassword(getString("Password: ")));
							customer.setName(getString("Full Name of the Customer: "));
							customer.setEmail(getString("The EmailId: "));
							customer.setDob(Supplement.dateToLong(getString("The Dob(yyyy-mm-dd): ")));
							customer.setAddress(getString("The Address: "));
							customer.setType("Customer");
							customer.setStatus("ACTIVE");
							customer.setContactNum(getLong("The Contact Num: "));
							customer.setAadharNum(getLong("Aadhar Number: "));
							customer.setPanNum(getString("PAN number: "));
							logic.addCustomer(customer);
						}else {
							throw new BankException("Your ID is INACTIVE");
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 4:
					try {
						if (user.getStatus().equals("ACTIVE")) {
							System.out.println("Details of a Customer");
							Customer customer=logic.getCustomerPersonalInfo(getInteger("The Id of the Customer: "));
							System.out.println("Personal Info");
							System.out.println("Account holder Id: "+customer.getId());
							System.out.println("Account Holder Name: "+customer.getName());
							System.out.println("DOB: "+Supplement.longToDate(customer.getDob()));
							System.out.println("Email: "+customer.getEmail());
							System.out.println("Address: "+customer.getAddress());
							System.out.println("Contact Number: "+customer.getContactNum());
							System.out.println("Aadhar Number: "+customer.getAadharNum());
							System.out.println("PAN Number: "+customer.getPanNum());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 5:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Removing Customer: ");
							logic.removeCustomer(getInteger("The employeeId of the Customer: "));
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 6:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Add an Employee..");
							Employee employee=new Employee();
							employee.setId(getInteger("The Employee id: "));
							employee.setName(getString("Name: "));
							employee.setPassword(logic.encryptPassword(getString("Password: ")));
							employee.setEmail(getString("EmailId: "));
							String dob=getString("The Dob(yyyy-mm-dd): ");
							employee.setDob(Supplement.dateToLong(dob));
							employee.setAddress(getString("Address: "));
							employee.setType("Employee");
							employee.setStatus("ACTIVE");
							employee.setContactNum(getLong("Contact Number: "));
							employee.setDepartment(getString("Department: "));
							employee.setBranch(getString("Branch: "));
							logic.addEmployee(employee);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					}catch (Exception e) {
						e.printStackTrace();					
					}break;
			
				case 7:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Employee Details...");
							int employeeId=getInteger("Enter the employee id: ");
							Employee employee=logic.getEmployeePersonalInfo(employeeId);
							System.out.println("Personal Info");
							System.out.println("EmployeeId: "+employee.getId());
							System.out.println("Employee Name: "+employee.getName());
							System.out.println("DOB: "+Supplement.longToDate(employee.getDob()));
							System.out.println("Email: "+employee.getEmail());
							System.out.println("Address: "+employee.getAddress());
							System.out.println("Contact Number: "+employee.getContactNum());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 8:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Removing Employee: ");
							logic.removeEmployee(getInteger("The Employee id: "));
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 9:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Add an Account");
							Account account=new Account();
							account.setAccountNum(getLong("New Account Number: "));
							account.setCustomerId(getInteger("Customer id: "));
							account.setType(getString("Type: "));
							account.setBalance(getInteger("Initial Balance: "));
							account.setStatus("Active");
							account.setBranchId(getInteger("The branch id: "));
							logic.addAccount(account);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 10:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("View Account Info");
							int employeeId=getInteger("Enter the CustomerId of the Account");
							Account account= logic.getAccount(employeeId);
							System.out.println("Account details:");
							System.out.println("Account Number: "+account.getAccountNum());
							System.out.println("Account Holder Name: "+user.getName());
							System.out.println("Type: "+account.getType());
							System.out.println("Status: "+account.getStatus());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 11:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							long accountNum=(long)getInteger("Enter the Account Number:");
							logic.removeAccount(accountNum);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 12:
					
					try {
						if(user.getStatus().equals("ACTIVE")) {
							long accountNum=(long)getInteger("Enter the Account Number: ");
							logic.blockAccount(accountNum);
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 13:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Details of your Branch");
							Branch branch=logic.getBranchDetails(getInteger("The BranchId: "));
							System.out.println("The Branch Id: "+branch.getBranchId());
							System.out.println("Name: "+branch.getName());
							System.out.println("IFSC code: "+branch.getiFSCcode());
							System.out.println("Address: "+branch.getAddress());
							System.out.println("Active Employees: "+branch.getNumOfActiveEmployees());
							System.out.println("Active Customers: "+branch.getNumOfActiveCustomers());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 14:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Transaction Details");
							int customerId=getInteger("The Customer id: ");
							List<Transaction> transactionsList=logic.getTransactionDetails(customerId);
							for(Transaction transaction:transactionsList) {
								System.out.println("Transaction Type: "+transaction.getTranscationType());
								System.out.println("Total Amount: "+transaction.getTranscationAmount());
								System.out.println("Account Balance"+transaction.getBalance());
								System.out.println("Sender Account number: "+transaction.getPrimaryAccNum());
								if (transaction.getTranscationType().equals("Withdrawal")||transaction.getTranscationType().equals("Deposit")) {
									System.out.println("Receiver Account Number: "+ transaction.getPrimaryAccNum());
								}else {
									System.out.println("Receier Account number: "+transaction.getSecondaryAccNum());
								}
								System.out.println("Time: "+transaction.getTimeStamp());
								System.out.println("Description: "+transaction.getDescription());
								
							}
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 15:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Employee Details...");
							int employeeId=getInteger("Enter the employee id: ");
							Employee employee=logic.getEmployeePersonalInfo(employeeId);
							System.out.println("Personal Info");
							System.out.println("EmployeeId: "+employee.getId());
							System.out.println("Employee Name: "+employee.getName());
							System.out.println("DOB: "+Supplement.longToDate(employee.getDob()));
							System.out.println("Email: "+employee.getEmail());
							System.out.println("Address: "+employee.getAddress());
							System.out.println("Contact Number: "+employee.getContactNum());
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 16:
					try {
						if(user.getStatus().equals("ACTIVE")) {
							System.out.println("Change the Password!");
							logic.changePassword(user.getId(), getString("Enter the new Password:"));
						}else {
							throw new BankException("Your id is INACTIVE");
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}break;
					
				case 17:
					try {
						System.out.println("Check the Status:");
						System.out.println("You are: "+logic.checkStatus(user.getId()));
					} catch (Exception e) {
						e.printStackTrace();
					}break;	
				
				case 18:
					x=2;
					break;
			}
		}
	}
	
	
	private static User login() throws NoSuchAlgorithmException, InvalidInputException, SQLException {
		int userId=getInteger("Enter the UserId:");
	
		System.out.println("Enter your Password:");
		System.out.print("\033[8m");
		String password=scan.nextLine();
		System.out.println("\033[0m");
		return logic.login(userId, password);
		
	}
	
	private static String getString(String str) {
		System.out.println(str);
		return scan.nextLine();
	}
	 
	private static int getInteger(String str) {
		System.out.println(str);
		int returnInteger= scan.nextInt();
		scan.nextLine();
		return returnInteger;
	}
	
	private static long getLong(String str) {
		System.out.println(str);
		long returnLong=scan.nextLong();
		scan.nextLine();
		return returnLong;
	}
	
	
}
