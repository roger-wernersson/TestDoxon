package testdoxon.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import testdoxon.exceptionHandler.TDException;

class TestFileRepository {

	@Test
	public void testFetchMethodNamesNotNull() throws TDException {
		
		String path = System.getProperty("user.dir") + "\\src\\testdoxon\\util\\TestDoxonUtils.java";
		FileRepository FR = new FileRepository();
		
		assertNotNull(FR.fetchMethodNames(path));
	}
	
	@Test
	public void testFetchMethodNamesTriggerNull() throws TDException
	{
		String path = System.getProperty("user.dir") + "\\src\\testdoxon\\util\\DoxonUtils.java";
		FileRepository FR = new FileRepository();
		
		assertNull(FR.fetchMethodNames(path));
	}
	
	@Test
	public void testFetchMethodNamesEmptyString() throws TDException
	{
		String path = "";
		FileRepository FR = new FileRepository();
		
		assertNull(FR.fetchMethodNames(path));
	}
	
	@Test
	public void testFetchMethodNamesNullParam()
	{
		String path = null;
		FileRepository FR = new FileRepository();

		try {
			assertNull(FR.fetchMethodNames(path));
		} catch (TDException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindLineNumberOfMethod() throws TDException
	{
		String path = System.getProperty("user.dir") + "\\src\\testdoxon\\util\\TestDoxonUtils.java";
		FileRepository FR = new FileRepository();
		
		assertEquals(34,FR.findLineNumberOfMethod(path, "FindTestFolderReturn"));
	}
	
	@Test
	public void testFindLineNumberOfMethodReturnZero() throws TDException
	{
		String path = System.getProperty("user.dir") + "\\src\\testdoxon\\util\\TestDoxonUtils.java";
		FileRepository FR = new FileRepository();
		
		assertEquals(0,FR.findLineNumberOfMethod(path, "FindTestFolder"));
	}
	
	@Test
	public void testFindLineNumberOfMethodEmptyString() throws TDException
	{
		String path = "";
		FileRepository FR = new FileRepository();
		
		assertEquals(-1,FR.findLineNumberOfMethod(path, "FindTestFolderReturn"));
	}
	
	@Test
	public void testFindLineNumberOfMethodPathNull()
	{
		String path = null;
		FileRepository FR = new FileRepository();
		
		try {
			assertEquals(-1,FR.findLineNumberOfMethod(path, "FindTestFolderReturn"));
		} catch (TDException e) {
			e.printStackTrace();
		}
	}

}
