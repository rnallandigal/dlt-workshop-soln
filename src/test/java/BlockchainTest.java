import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.lang.reflect.*;
import java.io.*;

import static org.junit.Assert.*;

public class BlockchainTest {
	private Blockchain blockchain;
	private Transaction[] transactions;
	private Block[] chain1;
	private Block[] chain2;
	
	@Before
	public void setUp() throws Exception {
		blockchain = new Blockchain();

		transactions = new Transaction[] {
			new Transaction("a", "b", 20),
			new Transaction("b", "c", 10),
			new Transaction("c", "d", 5),
			new Transaction("d", "a", 4),
			new Transaction("a", "e", 7)
		};

		Block genesisBlock = new Block();
		genesisBlock.add_transaction(transactions[0]);
		genesisBlock.add_transaction(transactions[1]);
		genesisBlock.finalizeBlock();

		Block c1b2 = new Block(genesisBlock);
		c1b2.add_transaction(transactions[2]);
		c1b2.add_transaction(transactions[3]);
		c1b2.finalizeBlock();

		Block c2b2 = new Block(genesisBlock);
		c2b2.add_transaction(transactions[4]);
		c2b2.finalizeBlock();

		Block c1b3 = new Block(c1b2);
		c1b3.add_transaction(transactions[0]);
		c1b3.add_transaction(transactions[2]);
		c1b3.add_transaction(transactions[4]);
		c1b3.finalizeBlock();

		Block c2b3 = new Block(c2b2);
		c2b3.add_transaction(transactions[1]);
		c2b3.add_transaction(transactions[3]);
		c2b3.finalizeBlock();

		chain1 = new Block[] { genesisBlock, c1b2, c1b3 };
		chain2 = new Block[] { genesisBlock, c2b2, c2b3 };
	}

	@Test
	public void constructBlockchain() {
		blockchain.add_blocks(chain1[0]);
		blockchain.add_blocks(chain1[1]);
		blockchain.add_blocks(chain1[2]);
	}

	@Test
	public void validateValidChain() {
		// Chain1
		assertTrue(blockchain.validateChain());
		blockchain.add_blocks(chain1[0]);
		assertTrue(blockchain.validateChain());
		blockchain.add_blocks(chain1[1]);
		assertTrue(blockchain.validateChain());
		blockchain.add_blocks(chain1[2]);
		assertTrue(blockchain.validateChain());

		// Chain2
		blockchain = new Blockchain();
		assertTrue(blockchain.validateChain());
		blockchain.add_blocks(chain2[0]);
		assertTrue(blockchain.validateChain());
		blockchain.add_blocks(chain2[1]);
		assertTrue(blockchain.validateChain());
		blockchain.add_blocks(chain2[2]);
		assertTrue(blockchain.validateChain());
	}

	@Test
	public void validateUnfinalizedChain() {
		Block b1 = new Block();
		b1.add_transaction(transactions[1]);
		blockchain.add_blocks(b1);
		assertFalse(blockchain.validateChain());
	}

	@Test
	public void validateInvalidChain() {
		chain1[0].add_transaction(transactions[4]);
		blockchain.add_blocks(chain1[0]);
		assertFalse(blockchain.validateChain());
		chain1[1].add_transaction(transactions[2]);
		blockchain.add_blocks(chain1[1]);
		assertFalse(blockchain.validateChain());
	}

	@Test
	public void validateMaliciousChain() throws Exception {
		blockchain.add_blocks(chain1[0]);
		blockchain.add_blocks(chain1[1]);
		blockchain.add_blocks(chain1[2]);

		assertTrue(blockchain.validateChain());

		Field blist_f = blockchain.getClass().getDeclaredField("blockchain");
		blist_f.setAccessible(true);
		ArrayList<Block> blist = (ArrayList<Block>)(blist_f.get(blockchain));

		Field txslist_f = ((Block)blist.get(0)).getClass().getDeclaredField("transactions");
		txslist_f.setAccessible(true);
		ArrayList<Transaction> txslist = (ArrayList<Transaction>)(txslist_f.get(blist.get(0)));

		Field amount_f = ((Transaction)txslist.get(0)).getClass().getDeclaredField("amount");
		amount_f.setAccessible(true);
		amount_f.setInt(txslist.get(0), 20000);

		assertFalse(blockchain.validateChain());
	}
}
