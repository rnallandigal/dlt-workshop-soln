import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HashHelperTest {
	private String message1, message2;

	@Before
	public void setUp() throws Exception {
		message1 = "This is a string";
		message2 = "This is a String";
	}

	@Test
	public void testHash() {
		assertEquals(HashHelper.hashMessage(message1.getBytes()),
				HashHelper.hashMessage(message1.getBytes()));
	}
	@Test
	public void testHashNotEqual() {
		assertNotEquals(HashHelper.hashMessage(message1.getBytes()),
				HashHelper.hashMessage(message2.getBytes()));
	}

}
