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
		File file = new File(System.getProperty("user.dir") + "\\TestDoxon\\src\\main\\testdoxon\\util");
		TDFile tfile = new TDFile(file);
		
		assertEquals(System.getProperty("user.dir") + "\\TestDoxon\\src\\test\\testdoxon\\",DoxonUtils.createTestPath(tfile));
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
		
		assertEquals("/TestDoxon/src/test", DoxonUtils.findRootFolder(path));
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
		
		assertEquals("/TestDoxon/src/test", DoxonUtils.findRootFolder(path));
	}
	
	@Test
	public void testFindTestFolderWrongSlashes()
	{
		String path = "/TestDoxon/src/main/testdoxon";
		
		System.out.println(DoxonUtils.findRootFolder(path));
		assertEquals("/TestDoxon/src/test", DoxonUtils.findRootFolder(path));
	}
	
	


}
