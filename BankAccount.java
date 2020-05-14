import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BankAccount {

	public static final int DEFAULT = 10000;
	private int accNo;
	private String first;
	private String last;
	private int balance;
	private List<Transaction> transactionHistory;

	/**
	 * Constructor for the BankAccount object
	 *
	 * @param accNo   the account number
	 * @param first   the first name of the person
	 * @param last    the surname of the person
	 * @param balance the starting balance of the account
	 */
	public BankAccount(int accNo, String first, String last, int balance) {
		this.setAccNo(accNo);
		this.setFirst(first);
		this.setLast(last);
		this.setBalance(balance);
		this.setTransactionHistory(new ArrayList<Transaction>());
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getAccNo() {
		return accNo;
	}

	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}

	public List<Transaction> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(List<Transaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

	/**
	 * Returns the formatted string of the account.
	 *
	 * @return the formatted String
	 */
	public String details() {
		return Integer.toString(accNo) + " - " + first + " " + last + " - $" + balance;
	}

	/**
	 * Returns list of all transactions involving account. Returns null if no
	 * history
	 *
	 * @return the transaction history
	 */
	public List<Transaction> history() {
		if (getTransactionHistory().size() != 0) {
			return getTransactionHistory();
		}
		return null;
	}

	/**
	 * Processes transaction by deducting/adding amount. Returns true if successful,
	 * false if insufficient funds.
	 *
	 * @param transaction the transaction to be processed
	 * @return the result of the transaction
	 */
	public boolean processTransaction(Transaction transaction) {
		if (transaction.getSender().getAccNo() == accNo) {
			if (balance >= transaction.getAmount()) {
				balance = balance - transaction.getAmount();
				transactionHistory.add(transaction);
				return true;
			} else {
				System.out.println("insufficient funds");
				return false;
			}
		} else if (transaction.getReceiver().getAccNo() == accNo) {
			balance += transaction.getAmount();
			transactionHistory.add(transaction);
			return true;
		}
		return false;
	}

	public boolean recoverTransaction(Transaction transaction) {
		transactionHistory.add(transaction);
		return true;
	}

	/**
	 * Returns list of transactions where account is sender. Returns null if none.
	 *
	 * @return the list of transactions
	 */
	public List<Transaction> outgoing() {
		List<Transaction> outgoingTransactions = new ArrayList<Transaction>();
		for (int i = 0; i < transactionHistory.size(); i++) {
			if (transactionHistory.get(i).getSender().getAccNo() == accNo) {
				outgoingTransactions.add(transactionHistory.get(i));
			}
		}
		if (outgoingTransactions.size() == 0) {
			return null;
		}
		return outgoingTransactions;
	}

	/**
	 * Returns list of transactions where account is receiver. Returns null if none.
	 *
	 * @return the list of transactions
	 */
	public List<Transaction> incoming() {
		List<Transaction> incomingTransactions = new ArrayList<Transaction>();
		for (int i = 0; i < transactionHistory.size(); i++) {
			if (transactionHistory.get(i).getReceiver().getAccNo() == accNo) {
				incomingTransactions.add(transactionHistory.get(i));
			}
		}
		if (incomingTransactions.size() == 0) {
			return null;
		}
		return incomingTransactions;
	}

	/**
	 * Renames the account with new first and last name.
	 * 
	 * @param first the new first name
	 * @param last  the new surname
	 */
	public void rename(String first, String last) {
		if (first == null || last == null) {
			System.out.println("failure");
			return;
		}
		this.first = first;
		this.last = last;
	}

	/**
	 * Returns the largest balance in list of accounts. Returns -1 if empty or null.
	 *
	 * @param accounts the list of accounts
	 * @return the largest balance
	 */
	public static int findMax(List<BankAccount> accounts) {
		if (accounts == null || accounts.size() == 0) {
			System.out.println("no accounts");
			return -1;
		}
		int max = 0;
		for (int i = 0; i < accounts.size(); i++) {
			if (accounts.get(i).getBalance() > max) {
				max = accounts.get(i).getBalance();
			}
		}
		return max;
	}

	/**
	 * Returns the smallest balance in list of accounts. Returns -1 if empty or
	 * null.
	 *
	 * @param accounts the list of accounts
	 * @return the smallest balance
	 */
	public static int findMin(List<BankAccount> accounts) {
		if (accounts == null || accounts.size() == 0) {
			System.out.println("no accounts");
			return -1;
		}
		List<Integer> sortedBalances = new ArrayList<Integer>();
		for (int i = 0; i < accounts.size(); i++) {
			sortedBalances.add(accounts.get(i).getBalance());
		}
		Collections.sort(sortedBalances);
		return sortedBalances.get(0);
	}

	/**
	 * Returns the average balance in list of accounts (rounded down). Returns -1 if
	 * empty or null.
	 *
	 * @param accounts the list of accounts
	 * @return the average balance
	 */
	public static int mean(List<BankAccount> accounts) {
		if (accounts == null || accounts.size() == 0) {
			System.out.println("no accounts");
			return -1;
		}
		int total = totalBalance(accounts);
		return total / accounts.size();
	}

	/**
	 * Returns the median balance in list of accounts. Returns -1 if empty or null.
	 *
	 * @param accounts the list of accounts
	 * @return the median balance
	 */
	public static int median(List<BankAccount> accounts) {
		if (accounts == null || accounts.size() == 0) {
			System.out.println("no accounts");
			return -1;
		}
		List<Integer> sortedBalances = new ArrayList<Integer>();
		for (int i = 0; i < accounts.size(); i++) {
			sortedBalances.add(accounts.get(i).getBalance());
		}
		Collections.sort(sortedBalances);
		int middle = (sortedBalances.size() - 1) / 2;
		if (sortedBalances.size() == 2) {
			return (sortedBalances.get(0) + sortedBalances.get(1)) / 2;
		}
		return sortedBalances.get(middle);
	}

	/**
	 * Returns the total balance in list of accounts. Returns -1 if empty or null.
	 *
	 * @param accounts the list of accounts
	 * @return the total balance
	 */
	public static int totalBalance(List<BankAccount> accounts) {
		if (accounts == null || accounts.size() == 0) {
			System.out.println("no accounts");
			return -1;
		}
		int total = 0;
		for (int i = 0; i < accounts.size(); i++) {
			total += accounts.get(i).getBalance();
		}
		return total;
	}

}
