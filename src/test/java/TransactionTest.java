import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionTest {
	Transaction tx;

	@Before
	public void setUp() throws Exception {
		tx = new Transaction("Satheesh", "Jeeva", 10);
	}

	@Test
	public void testNewTransaction() {
		assertEquals(tx.getAmount(), 10);
		assertEquals(tx.getSender(), "Satheesh");
		assertEquals(tx.getReceiver(), "Jeeva");
	}
}
