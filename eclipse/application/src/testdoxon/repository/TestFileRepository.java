package testdoxon.repository;

import static org.junit.jupiter.api.Assertions.*;

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
	public void testFetchMethodNamesNullParam() throws TDException
	{
		String path = null;
		FileRepository FR = new FileRepository();
		
		assertNull(FR.fetchMethodNames(path));
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
		
		assertNull(FR.findLineNumberOfMethod(path, "FindTestFolderReturn"));
	}
	
	@Test
	public void testFindLineNumberOfMethodPathNull() throws TDException
	{
		String path = null;
		FileRepository FR = new FileRepository();
		
		assertNull(FR.findLineNumberOfMethod(path, "FindTestFolderReturn"));
	}

}
