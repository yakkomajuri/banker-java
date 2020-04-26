
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Banker {

	private Map<Integer, BankAccount> accounts;
	private List<Transaction> transactions;

	private int nextTransactionNumber;
	private int nextAccountNumber;
	public static final int DEFAULT = 10000;
	private static boolean exited = false;

	public final static String helpString = "EXIT exit from application\n" + "COMMANDS display the command list\n\n"
			+ "LIST ACCOUNTS displays all accounts in system\n"
			+ "LIST TRANSACTIONS displays all transactions in system\n\n"
			+ "DETAILS <accno> displays all details about bank account\n"
			+ "BALANCE <accno> displays the current balance of bank account\n\n"
			+ "HISTORY <accno> displays all transactions involving an account\n"
			+ "OUTGOING <accno> displays all transactions paid by account\n"
			+ "INCOMING <accno> displays all transactions received by account\n\n"
			+ "CREATE <first> <last> [<balance>] creates a bank account\n"
			+ "RENAME <accno> <first> <last> renames a bank account\n\n"
			+ "PAY <sender> <receiver> <amount> transfers money between account\n"
			+ "TRANSACTION <id> displays the transaction details\n"
			+ "CANCEL <id> makes a copy of the transaction with receiver/sender swapped\n\n"
			+ "ARCHIVE <ledgerFile> <accountFile> stores the transaction history as a ledger\n"
			+ "RECOVER <ledgerFile> <accountFile> restores a ledger\n\n"
			+ "MERGE <accno …> transfers all funds from listed accounts into the first account\n\n"
			+ "MAX displays the highest balance from all accounts\n"
			+ "MIN displays the lowest balance from all accounts\n" + "MEAN displays the average balance\n"
			+ "MEDIAN displays the median balance\n" + "TOTAL displays the amount of money stored by bank";

	/**
	 * Constructor for the Banker administrative system.
	 */
	public Banker() {
		this.accounts = new HashMap<Integer, BankAccount>();
		this.transactions = new ArrayList<Transaction>();
		this.nextTransactionNumber = 1;
		this.nextAccountNumber = 100000;
		exited = false;
	}

	/**
	 * Displays the list of commands.
	 */
	public static void commands() {
		System.out.println(Banker.helpString);
		return;
	}

	/**
	 * Displays the closing statement.
	 */
	public static void exit() {
		System.out.println("bye");
		exited = true;
		return;
	}

	/**
	 * Prints out all account numbers within system in numerical order.
	 */
	public void listAccounts() {
		if (accounts.size() == 0) {
			System.out.println("no accounts");
			return;
		}
		// System.out.println(Integer.toString(nextAccountNumber));
		for (int i = 100000; i < nextAccountNumber; i++) {
			System.out.println(i);
		}
		return;
	}

	/**
	 * Prints out all transaction details within system in chronological order.
	 */
	public void listTransactions() {
		if (transactions.size() < 1) {
			System.out.println("no transactions");
			return;
		}
		for (int i = 0; i < transactions.size(); i++) {
			System.out.println(
					Integer.toString(i + 1) + ": " + Integer.toString(transactions.get(i).getSender().getAccNo())
							+ " -> " + Integer.toString(transactions.get(i).getReceiver().getAccNo()) + " | $"
							+ transactions.get(i).getAmount() + " | " + transactions.get(i).getHash());
		}
	}

	/**
	 * Displays the account number, name and balance of an account.
	 *
	 * @param accNo the account number
	 */
	public void details(int accNo) {
		if (accounts.get(accNo) == null) {
			System.out.println("no such account");
			return;
		}
		BankAccount account = accounts.get(accNo);
		System.out.println(Integer.toString(accNo) + " - " + account.getFirst() + " " + account.getLast() + " - $"
				+ Integer.toString(account.getBalance()));
	}

	/**
	 * Displays the current balance for a specified account.
	 *
	 * @param accNo the account number
	 */
	public void balance(int accNo) {
		if (accounts.get(accNo) == null) {
			System.out.println("no such account");
			return;
		}
		BankAccount account = accounts.get(accNo);
		System.out.println("$" + Integer.toString(account.getBalance()));
	}

	/**
	 * Displays the entire transaction history for a specified account.
	 *
	 * @param accNo the account number
	 */
	public void history(int accNo) {
		if (accounts.get(accNo) == null) {
			System.out.println("no such account");
			return;
		}
		if (accounts.get(accNo).getTransactionHistory().size() == 0) {
			System.out.println("no history");
			return;
		}
		List<Transaction> accountTransactions = accounts.get(accNo).getTransactionHistory();
		for (int i = 0; i < accountTransactions.size(); i++) {
			System.out.println(
					Integer.toString(i + 1) + ": " + Integer.toString(accountTransactions.get(i).getSender().getAccNo())
							+ " -> " + Integer.toString(accountTransactions.get(i).getReceiver().getAccNo()) + " | $"
							+ accountTransactions.get(i).getAmount() + " | " + accountTransactions.get(i).getHash());
		}
		return;
	}

	/**
	 * Displays the outgoing transaction history for a specified account.
	 *
	 * @param accNo the account number
	 */
	public void outgoing(int accNo) {
		if (accounts.get(accNo) == null) {
			System.out.println("no such account");
			return;
		}
		int outgoingFound = 0;
		List<Transaction> accountTransactions = accounts.get(accNo).getTransactionHistory();
		for (int i = 0; i < accountTransactions.size(); i++) {
			if (accountTransactions.get(i).getSender().getAccNo() == accNo) {
				outgoingFound++;
				System.out.println(Integer.toString(i + 1) + ": "
						+ Integer.toString(accountTransactions.get(i).getSender().getAccNo()) + " -> "
						+ Integer.toString(accountTransactions.get(i).getReceiver().getAccNo()) + " | $"
						+ accountTransactions.get(i).getAmount() + " | " + accountTransactions.get(i).getHash());
			}
		}
		if (outgoingFound == 0) {
			System.out.println("no outgoing");
		}
		return;
	}

	/**
	 * Displays the incoming transaction history for a specified account.
	 *
	 * @param accNo the account number
	 */
	public void incoming(int accNo) {
		if (accounts.get(accNo) == null) {
			System.out.println("no such account");
			return;
		}
		int incomingFound = 0;
		List<Transaction> accountTransactions = accounts.get(accNo).getTransactionHistory();
		for (int i = 0; i < accountTransactions.size(); i++) {
			if (accountTransactions.get(i).getReceiver().getAccNo() == accNo) {
				incomingFound++;
				System.out.println(Integer.toString(i + 1) + ": "
						+ Integer.toString(accountTransactions.get(i).getSender().getAccNo()) + " -> "
						+ Integer.toString(accountTransactions.get(i).getReceiver().getAccNo()) + " | $"
						+ accountTransactions.get(i).getAmount() + " | " + accountTransactions.get(i).getHash());
			}
		}
		if (incomingFound == 0) {
			System.out.println("no incoming");
		}
		return;
	}

	/**
	 * Creates an account within the system.
	 *
	 * @param first   the first name of the person
	 * @param last    the surname of the person
	 * @param balance the starting balance of the person
	 */
	public void createAccount(String first, String last, int balance) {
		BankAccount newAccount = new BankAccount(nextAccountNumber, first, last, balance);
		accounts.put(nextAccountNumber, newAccount);
		nextAccountNumber++;
		System.out.println("success");
		return;
	}

	/**
	 * Renames a specified account.
	 *
	 * @param accNo the account number
	 * @param first the new first name
	 * @param last  the new surname
	 */
	public void rename(int accNo, String first, String last) {
		if (accounts.get(accNo) == null) {
			System.out.println("no such account");
			return;
		}
		BankAccount account = accounts.get(accNo);
		account.setFirst(first);
		account.setLast(last);
		System.out.println("success");
		return;
	}

	/**
	 * Creates a transaction between two bank accounts.
	 *
	 * @param sender   the account number of the sender
	 * @param receiver the account number of the receiver
	 * @param amount   the amount to be transferred
	 */
	public void pay(int sender, int receiver, int amount) {
		BankAccount s = accounts.get(sender);
		BankAccount r = accounts.get(receiver);
		if (s == null || r == null) {
			System.out.println("no such account");
			return;
		}
		if (amount <= 0) {
			System.out.println("amount must be positive");
			return;
		}
		if (s.getAccNo() == r.getAccNo()) {
			System.out.println("sender cannot be receiver");
			return;
		}
		Transaction newTransaction = new Transaction(nextTransactionNumber, s, r, amount,
				nextTransactionNumber == 1 ? null : transactions.get(nextTransactionNumber - 2).getHash());
		boolean senderOk = s.processTransaction(newTransaction);
		boolean receiverOk = false;
		if (senderOk) {
			receiverOk = r.processTransaction(newTransaction);
		} else {
			return;
		}
		if (senderOk && receiverOk) {
			transactions.add(newTransaction);
			nextTransactionNumber++;
			System.out.println("success");
		}
		return;
	}

	/**
	 * Displays the details for a specified transaction.
	 *
	 * @param id the transaction ID
	 */
	public void transaction(int id) {
		if (id < 1 || id >= nextTransactionNumber) {
			System.out.println("no such transaction");
			return;
		}
		System.out.println(transactions.get(id - 1).get());
		return;
	}

	/**
	 * Creates the reverse transaction to the one specified.
	 *
	 * @param id the transaction id to be cancelled
	 */
	public void cancel(int id) {
		if (id < 1 || id >= nextTransactionNumber) {
			System.out.println("no such transaction");
			return;
		}
		Transaction t = transactions.get(id - 1);
		pay(t.getReceiver().getAccNo(), t.getSender().getAccNo(), t.getAmount());
		return;
	}

	/**
	 * Saves ledger to file.
	 *
	 * @param ledgerFile the name of the ledger file
	 * @param accFile    the name of the accounts file
	 */
	public void archive(String ledgerFile, String accFile) {
		File f1 = new File(ledgerFile);
		File f2 = new File(accFile);
		f1.delete();
		f2.delete();
		boolean ledgerResult = archiveTransactions(ledgerFile);
		boolean accResult = false;
		if (ledgerResult) {
			accResult = archiveAccounts(accFile);
		}
		if (ledgerResult && accResult) {
			System.out.println("success");
		} else {
			f1.delete();
			f2.delete();
		}
		return;
	}

	public boolean archiveTransactions(String file) {
		try {
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = 0; i < transactions.size(); i++) {
				Transaction t = transactions.get(i);
				String id = Integer.toString(t.getId());
				String sender = Integer.toString(t.getSender().getAccNo());
				String receiver = Integer.toString(t.getReceiver().getAccNo());
				String amount = Integer.toString(t.getAmount());
				String line = id + ", " + receiver + ", " + sender + ", " + amount + ", " + t.getHash();
				bufferedWriter.write(line);
				bufferedWriter.write("\n");
			}
			bufferedWriter.close();
			return true;
		} catch (IOException ex) {
			System.out.println("no such file");
			return false;
		}
	}

	public boolean archiveAccounts(String file) {
		try {
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = 100000; i < nextAccountNumber; i++) {
				BankAccount a = accounts.get(i);
				String accNo = Integer.toString(a.getAccNo());
				String balance = Integer.toString(a.getBalance());
				String line = accNo + ", " + a.getFirst() + ", " + a.getLast() + ", " + balance;
				bufferedWriter.write(line);
				bufferedWriter.write("\n");
			}
			bufferedWriter.close();
			return true;
		} catch (IOException ex) {
			System.out.println("no such file");
			return false;
		}
	}

	/**
	 * Restores archived ledger and accounts files to the system.
	 *
	 * @param ledgerFile the name of the ledger file
	 * @param accFile    the name of the accounts file
	 */
	public void recover(String ledgerFile, String accFile) {
		boolean ledgerRestored = false;
		boolean transactionsRestored = true;
		int latestAccount = 0;
		Map<Integer, BankAccount> restoredAccounts = new HashMap<Integer, BankAccount>();
		List<Transaction> restoredLedger = new ArrayList<Transaction>();
		try {
			FileReader fr1 = new FileReader(accFile);
			BufferedReader br1 = new BufferedReader(fr1);
			String line;
			while ((line = br1.readLine()) != null) {
				String[] details = line.split("\\s*,\\s*");
				int balance = Integer.parseInt(details[3]);
				int accNo = Integer.parseInt(details[0]);
				BankAccount newAccount = new BankAccount(accNo, details[1], details[2], balance);
				restoredAccounts.put(accNo, newAccount);
				latestAccount = accNo;
			}
			br1.close();
			ledgerRestored = true;
		} catch (IOException ex) {
			System.out.println("no such file");
			ledgerRestored = false;
		}
		if (ledgerRestored) {
			try {
				FileReader fr2 = new FileReader(ledgerFile);
				BufferedReader br2 = new BufferedReader(fr2);
				String line;
				while ((line = br2.readLine()) != null) {
					String[] details = line.split("\\s*,\\s*");
					int id = Integer.parseInt(details[0]);
					BankAccount sender = restoredAccounts.get(Integer.parseInt(details[2]));
					BankAccount receiver = restoredAccounts.get(Integer.parseInt(details[1]));
					if (sender == null || receiver == null) {
						transactionsRestored = false;
						break;
					}
					int amount = Integer.parseInt(details[3]);
					String prevHash = null;
					if (id != 1) {
						prevHash = restoredLedger.get(id - 2).getHash();
					}
					Transaction t = new Transaction(id, sender, receiver, amount, prevHash);
					t.setHash(details[4]);
					restoredAccounts.get(sender.getAccNo()).recoverTransaction(t);
					restoredAccounts.get(receiver.getAccNo()).recoverTransaction(t);
					restoredLedger.add(t);
				}
				br2.close();
			} catch (IOException ex) {
				System.out.println("no such file");
			}
		}
		boolean verified = Transaction.verify(restoredLedger);
		if (ledgerRestored && transactionsRestored && verified) {
			accounts = restoredAccounts;
			transactions = restoredLedger;
			nextAccountNumber = latestAccount + 1;
			nextTransactionNumber = transactions.size() + 1;
			System.out.println("success");
		} else if (!verified) {
			System.out.println("invalid ledger");
		}
		return;
	}

	/**
	 * Transfers all funds into the destination account.
	 *
	 * @param dest   the account number for the destination account
	 * @param others the account numbers for the accounts to be merged
	 */
	public void merge(int dest, int[] others) {
		BankAccount d = accounts.get(dest);
		if (d == null) {
			System.out.println("no such account");
			return;
		}
		for (int account : others) {
			BankAccount acc = accounts.get(account);
			if (acc == null) {
				System.out.println("no such account");
				return;
			}
			if (acc.getAccNo() == d.getAccNo()) {
				System.out.println("sender cannot be receiver");
				return;
			}
		}
		for (int a : others) {
			BankAccount s = accounts.get(a);
			Transaction newTransaction = new Transaction(nextTransactionNumber, s, d, s.getBalance(),
					nextTransactionNumber == 1 ? null : transactions.get(nextTransactionNumber - 2).getHash());
			boolean senderOk = s.processTransaction(newTransaction);
			boolean receiverOk = false;
			if (senderOk) {
				receiverOk = d.processTransaction(newTransaction);
			} else {
				return;
			}
			if (senderOk && receiverOk) {
				transactions.add(newTransaction);
				nextTransactionNumber++;
			}
		}
		System.out.println("success");
		return;

	}

	/**
	 * Displays the lowest balance in the system.
	 */
	public void min() {
		if (nextAccountNumber == 100000) {
			System.out.println("no accounts");
			return;
		}
		List<BankAccount> accs = new ArrayList<BankAccount>();
		for (int i = 100000; i < nextAccountNumber; i++) {
			accs.add(accounts.get(i));
		}
		System.out.println("$" + Integer.toString(BankAccount.findMin(accs)));
		return;
	}

	/**
	 * Displays the highest balance in the system.
	 */
	public void max() {
		if (nextAccountNumber == 100000) {
			System.out.println("no accounts");
			return;
		}
		List<BankAccount> accs = new ArrayList<BankAccount>();
		for (int i = 100000; i < nextAccountNumber; i++) {
			accs.add(accounts.get(i));
		}
		System.out.println("$" + Integer.toString(BankAccount.findMax(accs)));
		return;
	}

	/**
	 * Displays the average balance in the system (rounded down).
	 */
	public void mean() {
		if (nextAccountNumber == 100000) {
			System.out.println("no accounts");
			return;
		}
		List<BankAccount> accs = new ArrayList<BankAccount>();
		for (int i = 100000; i < nextAccountNumber; i++) {
			accs.add(accounts.get(i));
		}
		System.out.println("$" + Integer.toString(BankAccount.mean(accs)));
		return;
	}

	/**
	 * Displays the median balance in the system.
	 */
	public void median() {
		if (nextAccountNumber == 100000) {
			System.out.println("no accounts");
			return;
		}
		List<BankAccount> accs = new ArrayList<BankAccount>();
		for (int i = 100000; i < nextAccountNumber; i++) {
			accs.add(accounts.get(i));
		}
		System.out.println("$" + Integer.toString(BankAccount.median(accs)));
		return;
	}

	/**
	 * Displays the total balance for all accounts.
	 */
	public void total() {
		if (nextAccountNumber == 100000) {
			System.out.println("no accounts");
			return;
		}
		List<BankAccount> accs = new ArrayList<BankAccount>();
		for (int i = 100000; i < nextAccountNumber; i++) {
			accs.add(accounts.get(i));
		}
		System.out.println("$" + Integer.toString(BankAccount.totalBalance(accs)));
		return;
	}

	public void runSystem() {
		Scanner keyboard = new Scanner(System.in);
		while (!exited) {
			System.out.print("$ ");
			String command = "";
			if (keyboard.hasNextLine()) {
				command = keyboard.nextLine();

			}
			String[] commandArr = command.split("[\\s']");
			String keyword = commandArr[0];
			switch (keyword) {
				case "EXIT":
					exit();
					break;
				case "COMMANDS":
					commands();
					break;
				case "LIST":
					if (commandArr[1].equals("ACCOUNTS")) {
						listAccounts();
					} else if (commandArr[1].equals("TRANSACTIONS")) {
						listTransactions();
					}
					break;
				case "BALANCE":
					balance(Integer.parseInt(commandArr[1]));
					break;
				case "DETAILS":
					details(Integer.parseInt(commandArr[1]));
					break;
				case "HISTORY":
					history(Integer.parseInt(commandArr[1]));
					break;
				case "OUTGOING":
					outgoing(Integer.parseInt(commandArr[1]));
					break;
				case "INCOMING":
					incoming(Integer.parseInt(commandArr[1]));
					break;
				case "CREATE":
					createAccount(commandArr[1], commandArr[2],
							commandArr.length < 4 ? DEFAULT : Integer.parseInt(commandArr[3]));
					break;
				case "RENAME":
					rename(Integer.parseInt(commandArr[1]), commandArr[2], commandArr[3]);
					break;
				case "PAY":
					pay(Integer.parseInt(commandArr[1]), Integer.parseInt(commandArr[2]),
							Integer.parseInt(commandArr[3]));
					break;
				case "TRANSACTION":
					transaction(Integer.parseInt(commandArr[1]));
					break;
				case "CANCEL":
					cancel(Integer.parseInt(commandArr[1]));
					break;
				case "ARCHIVE":
					archive(commandArr[1], commandArr[2]);
					break;
				case "RECOVER":
					recover(commandArr[1], commandArr[2]);
					break;
				case "MERGE":
					int[] others = new int[commandArr.length - 2];
					for (int i = 2; i < commandArr.length; i++) {
						others[i - 2] = Integer.parseInt(commandArr[i]);
					}
					merge(Integer.parseInt(commandArr[1]), others);
					break;
				case "MAX":
					max();
					break;
				case "MIN":
					min();
					break;
				case "MEAN":
					mean();
					break;
				case "MEDIAN":
					median();
					break;
				case "TOTAL":
					total();
					break;
				default:
					System.out.println("Invalid command");
					break;
			}
			System.out.print("\n");
		}
		keyboard.close();
	}

	public static void main(String[] args) {
		Banker bankerOS = new Banker();
		bankerOS.runSystem();
	}

}
