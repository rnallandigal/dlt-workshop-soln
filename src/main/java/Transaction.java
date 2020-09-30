import com.google.gson.Gson;

import java.util.Date;
import java.nio.ByteBuffer;


public class Transaction {
	private String sender, receiver;
	private int amount;
	private Date timestamp;

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public int getAmount() {
		return amount;
	}

	public Transaction(String sender, String receiver, int amount) {
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.timestamp = new Date();
	}

	public static Transaction transactionFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Transaction.class);
	}
}
