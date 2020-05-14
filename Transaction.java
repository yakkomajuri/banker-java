import java.util.List;

public class Transaction {

    private String prevHash;
    private String transactionHash;
    private int id;
    private int amount;
    private BankAccount sender;
    private BankAccount receiver;

    /**
     * Constructor for the Transaction class.
     *
     * @param id       the ID of the transaction
     * @param sender   the sending account
     * @param receiver the receiving account
     * @param amount   the amount of money being transferred
     * @param prevHash the hash of the previous transaction
     */
    public Transaction(int id, BankAccount sender, BankAccount receiver, int amount, String prevHash) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.prevHash = prevHash;
        this.transactionHash = generateHash(id, sender.getAccNo(), receiver.getAccNo(), amount, prevHash);
    }

    /**
     * Returns the formatted string of the transaction.
     *
     * @return the formatted String
     */
    public String get() {
        String senderId = Integer.toString(sender.getAccNo());
        String receiverId = Integer.toString(receiver.getAccNo());
        String value = Integer.toString(amount);
        String transactionId = Integer.toString(id);
        return transactionId + ": " + senderId + " -> " + receiverId + " | $" + value + " | " + transactionHash;
    }

    /**
     * Verifies a list of transaction has not been tampered with. Returns true if
     * correct, false if invalid arguments or invalid hash chain.
     *
     * @param transactions the list of transactions
     * @return the result of the verification
     */
    public static boolean verify(List<Transaction> transactions) {
        if (transactions == null) {
            return false;
        }
        if (transactions.size() == 0) {
            return true;
        }
        boolean valid = true;
        String prevHash = null;
        for (int i = 1; i <= transactions.size(); i++) {
            Transaction t = transactions.get(i - 1);
            String checksum = generateHash(i, t.getSender().getAccNo(), t.getReceiver().getAccNo(), t.getAmount(),
                    prevHash);
            String recordedHash = t.getHash();
            prevHash = checksum;
            if (Integer.parseInt(checksum) != Integer.parseInt(recordedHash)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * && Returns the hash code for a transaction.
     *
     * @param id            the ID of the transaction
     * @param senderAccNo   the account number of the sender
     * @param receiverAccNo the account number of the receiver
     * @param amount        the amount of money being transferred
     * @param prevHash      the hash of the previous transaction
     * @return the generated hash
     */
    public static String generateHash(int id, int senderAccNo, int receiverAccNo, int amount, String prevHash) {
        String combined = id + "" + senderAccNo + "" + receiverAccNo + "" + amount + "" + prevHash;
        // System.out.printf("%d %d %d %d %s %s\n", id, senderAccNo, receiverAccNo,
        // amount, prevHash, combined.hashCode());
        return combined.hashCode() + "";
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getHash() {
        return transactionHash;
    }

    public BankAccount getSender() {
        return sender;
    }

    public BankAccount getReceiver() {
        return receiver;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setHash(String hash) {
        this.transactionHash = hash;
    }
}
