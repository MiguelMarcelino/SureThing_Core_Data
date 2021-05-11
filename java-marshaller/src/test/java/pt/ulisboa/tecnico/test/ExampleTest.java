package pt.ulisboa.tecnico.test;

import org.junit.jupiter.api.*;


/**
 *  Test suite
 */
public class ExampleTest {

	@BeforeAll
	public static void oneTimeSetUp() { }

	@AfterAll
	public static void oneTimeTearDown() { }

	@BeforeEach
	public void setUp() {
		
	}

	@AfterEach
	public void tearDown() {
		
	}


	// tests

	@Test
	public void testMarshaller() {
		
		//LocationProofProto.getDescriptor();

		// create claim

		// serialize

		// desserialize

		// check contents of claim
	}
	
	// endorsement structure
	// ...

	// verification structure
	// ...

}