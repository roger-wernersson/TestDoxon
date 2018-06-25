package testdoxon.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.eclipse.jface.viewers.TableViewer;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import testdoxon.model.TDFile;

class TestDoxonUtils {

	
	@Test
	public void testCreateTestPathReturn()
	{
		File file = new File("\\TestDoxon\\src\\main\\testdoxon\\util");
		TDFile tfile = new TDFile(file);

		assertEquals("C:/TestDoxon/src/test/testdoxon/",DoxonUtils.createTestPath(tfile));
	}
	
	@Test
	public void testCreateTestPathNull()
	{
		TDFile file = null;
		
		assertEquals(null,DoxonUtils.createTestPath(file));
	}
	
	@Test
	public void testFindTestFolderReturn()
	{
		String path = "\\TestDoxon\\src\\main\\testdoxon\\blah.exe";

		assertEquals("/TestDoxon/src", DoxonUtils.findRootFolder(path));
	}
	
	@Test
	public void testFindTestFolderNull()
	{
		String path = null;

		assertEquals(null,DoxonUtils.findRootFolder(path));
	}
	
	@Test
	public void testFindTestFolderBadPath()
	{
		String path = "\\TestDoxon\\src\\badpathing\\testdoxon";
		
		assertEquals("/TestDoxon/src", DoxonUtils.findRootFolder(path));
	}
	
	@Test
	public void testFindTestFolderEmptyPath()
	{
		String path = "";
		
		assertNull(DoxonUtils.findRootFolder(path));
	}

}
