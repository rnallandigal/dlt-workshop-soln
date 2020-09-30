import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Blockchain {
	private List blockchain = new ArrayList<Block>();

	public void add_blocks(Block blk) {
		this.blockchain.add(blk);
	}

	public Block get_latest_block() {
		return (Block) this.blockchain.get(blockchain.size()-1);
	}

	/*
	 * TODO: Implement this
	 * 1. Iterate over all the blocks and validate().
	 * 2. Check if the prev_hash of current block is the same as the
	 *    hash of the previous block.
	 */
	public boolean validateChain() {
		if(blockchain.size() == 0) return true;
		if(((Block)(blockchain.get(0))).validate() == false) {
			return false;
		}

		Iterator<Block> blockit = blockchain.iterator();
		String prev_hash = blockit.next().getHash();

		while(blockit.hasNext()) {
			Block block = blockit.next();
			if(!block.validate() || prev_hash != block.getPrev_hash()) {
				return false;
			} else {
				prev_hash = block.getHash();
			}
		}
		return true;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Blockchain blockchainFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Blockchain.class);
	}
	public static Blockchain blockchainFromJson(BufferedReader json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Blockchain.class);
	}

	public void saveAsJson() {
		try {
			FileWriter writer = new FileWriter("blockchain.json");
			writer.write(this.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Blockchain loadFromJson() {
		try {
			BufferedReader br = new BufferedReader(
					new FileReader("blockchain.json"));
			return blockchainFromJson(br);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
