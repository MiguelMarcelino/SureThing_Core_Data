package pt.ulisboa.tecnico.test;

import org.junit.jupiter.api.*;

import pt.ulisboa.tecnico.surethingcore.locationproof.*;

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
		
		LocationProofProto.getDescriptor();
	}
	

}