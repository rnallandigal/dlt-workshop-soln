import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Block {
	private List transactions = new ArrayList<Transaction>();
	private String prev_hash, hash;
	private int height, transactionCount;
	private Date when;

	public String getPrev_hash() {
		return prev_hash;
	}

	public String getHash() {
		return hash;
	}

	public int getHeight() {
		return height;
	}

	public int getTransactionCount() {
		return transactionCount;
	}

	public Block() {
		this.transactionCount = 0;
		this.when = new Date();
		this.height = 1;
		this.hash = "";
		this.prev_hash = "";
	}

	public Block(Block previousBlock) {
		this();
		this.prev_hash = previousBlock.hash;
		this.height = previousBlock.height + 1;
	}

	public void add_transaction(Transaction tx) {
		this.transactions.add(tx);
		this.transactionCount++;
	}

	public void finalizeBlock() {
		if (this.hash.isEmpty()) {
			this.hash = hashBlock();
		} else {
			throw new IllegalArgumentException("Block is already finalized");
		}
	}

	/*
	 * TODO: Implement this.
	 * 1. Iterate over all transactions.
	 * 2. Calculate the hash as curr_hash = hash(curr_hash + curr_txn)	
	 */
	private String hashTransactions() {
		String currentHash = "";
		for (Iterator txn = transactions.iterator(); txn.hasNext(); ) {
			Transaction curr_txn = (Transaction) txn.next();
			currentHash = HashHelper.hashMessage((currentHash + curr_txn.toString()).getBytes());
		}
		return currentHash;
	}

	/*
	 * TODO: Implement this.
	 * 1. Get the transaction hash.
	 * 2. Assemble the blockheader:
	 *    blockheader = {
	 *        payload_hash,
	 *        timestamp,
	 *        prev_hash,
	 *        total_transactions
	 *    }
	 *    and encode it into a string.
	 * 3. Calculate the blockhash = hash(blockheader).
	 */
	private String hashBlock() {
		class BlockHeader {
			private String payloadHash;
			private String timestamp;
			private String prevHash;
			private int totalTransactions;

			public BlockHeader(String payload, String time, String prev, int txs) {
				this.payloadHash = payload;
				this.timestamp = time;
				this.prevHash = prev;
				this.totalTransactions = txs;
			}

			public String toString() {
				return new Gson().toJson(this);
			}
		}
		return HashHelper.hashMessage(
			(this.hashTransactions() + new BlockHeader(
				this.hashTransactions(),
				this.getPrev_hash(),
				this.when.toString(),
				this.getTransactionCount()
			).toString()).getBytes()
		);
	}

	/*
	 * TODO: Implement this.
	 * Check if the hash of the block is correct.
	 */
	public boolean validate() {
		return this.hashBlock().equalsIgnoreCase(this.hash);
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Block blockFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Block.class);
	}

}
