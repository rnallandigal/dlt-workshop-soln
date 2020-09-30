import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BlockTest {
	private Transaction tx;
	private Transaction newtx;
	private Block block;

	@Before
	public void setUp() throws Exception {
		newtx = new Transaction("Jeeva", "Vinoth", 5);
		tx = new Transaction("Satheesh", "Jeeva", 10);
		block = new Block();
	}

	@Test
	public void add_transaction() {
		assertEquals(block.getTransactionCount(), 0);
		block.add_transaction(tx);
		assertEquals(block.getTransactionCount(), 1);
		assertFalse(block.validate());
		assertThat(block.getHash(), is(emptyString()));
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void finalizeBlock() throws IllegalArgumentException  {
		assertFalse(block.validate());
		block.finalizeBlock();
		assertThat(block.getHash(), is(not(emptyString())));
		assertThat(block.getPrev_hash(), is(emptyString()));
		assertEquals(block.getHeight(),1);
		assertTrue(block.validate());
	}

	@Test(expected = IllegalArgumentException.class)
	public void finalizeBlockFail() throws IllegalArgumentException {
		assertFalse(block.validate());
		block.finalizeBlock();
		block.finalizeBlock();
	}

	@Test
	public void validate() {
		assertFalse(block.validate());
		assertThat(block.getHash(), is(emptyString()));
		block.finalizeBlock();
		String old_hash = block.getHash();
		assertTrue(block.validate());
		block.add_transaction(newtx);
		try {
			block.finalizeBlock();
			fail("Exception: Block is finalized twice");
		} catch(IllegalArgumentException e) {}
		String new_hash = block.getHash();
		assertEquals(old_hash, new_hash);
		assertFalse(block.validate());
	}
}
