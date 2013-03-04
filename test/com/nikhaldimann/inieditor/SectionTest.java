package com.nikhaldimann.inieditor;

import junit.framework.*;
import com.nikhaldimann.inieditor.IniEditor;

import java.io.*;
import java.util.*;

public class SectionTest extends TestCase {

    public SectionTest(String name) {
        super(name);
    }

	/** Illegal section names. */
    public void testAddSectionIllegal() {
    	try {
    		IniEditor.Section s = new IniEditor.Section("[hallo");
    		fail("Should throw IllegalArgumentException.");
    	}
    	catch (IllegalArgumentException ex) { /* ok, this should happen */ }
    	try {
    		IniEditor.Section s = new IniEditor.Section("hallo]");
    		fail("Should throw IllegalArgumentException.");
    	}
    	catch (IllegalArgumentException ex) { /* ok, this should happen */ }
    	try {
    		IniEditor.Section s = new IniEditor.Section("  \t ");
    		fail("Should throw IllegalArgumentException.");
    	}
    	catch (IllegalArgumentException ex) { /* ok, this should happen */ }
    	try {
    		IniEditor.Section s = new IniEditor.Section("");
    		fail("Should throw IllegalArgumentException.");
    	}
    	catch (IllegalArgumentException ex) {/* ok, this should happen */ }
    }

	/** Setting and getting options. */
    public void testSetGet() {
    	IniEditor.Section s = new IniEditor.Section("test");
    	assertEquals(s.get("hallo"), null);
    	assertTrue(!s.hasOption("hallo"));
		s.set(" HALLO \t", " \tvelo ");
		assertEquals(s.get("hallo"), "velo");
    	assertTrue(s.hasOption("hallo"));
		s.set("hallo", "bike");
		assertEquals(s.get(" \tHALLO "), "bike");
		s.set("hallo", "bi\nk\n\re\n");
		assertEquals(s.get("hallo"), "bike");
    }

	/** Setting and getting options with case-sensitivity. */
    public void testSetGetCaseSensitive() {
    	IniEditor.Section s = new IniEditor.Section("test", true);
		s.set(" Hallo ", " \tvelo ");
		assertEquals(s.get("Hallo"), "velo");
    	assertTrue(s.hasOption("Hallo"));
    	assertTrue(!s.hasOption("hallo"));
		s.set("hallO", "bike");
		assertEquals(s.get(" \thallO "), "bike");
		assertEquals(s.get("Hallo"), "velo");
    }

	/** Setting options with illegal names. */
    public void testSetIllegalName() {
    	IniEditor.Section s = new IniEditor.Section("test");
    	try {
			s.set("hallo=", "velo");
			fail("Should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) { /* ok, this should happen */ }
    	try {
			s.set(" \t\t ", "velo");
			fail("Should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) { /* ok, this should happen */ }
    	try {
			s.set("", "velo");
			fail("Should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) { /* ok, this should happen */ }
    }

    /** Setting and getting with null arguments. */
    public void testSetGetNull() {
        IniEditor.Section s = new IniEditor.Section("test");
        try {
            s.set(null, "velo");
            fail("Should throw NullPointerException");
        }
        catch (NullPointerException ex) { /* ok, this should happen */ }
        s.set("hallo", null);
        try {
            s.get(null);
            fail("Should throw NullPointerException");
        }
        catch (NullPointerException ex) { /* ok, this should happen */ }
    }

    /** Getting option names. */
    public void testOptionNames() {
        IniEditor.Section s = new IniEditor.Section("test");
       	s.set("hallo", "velo");
       	s.set("hello", "bike");
       	List names = s.optionNames();
       	assertEquals(names.get(0), "hallo");
       	assertEquals(names.get(1), "hello");
       	assertEquals(names.size(), 2);
    }

    /** Adding lines. */
    public void testAddLines() {
        IniEditor.Section s = new IniEditor.Section("test");
       	s.addBlankLine();
       	s.addComment("hollderidi");
       	s.addComment("hollderidi", '#');
    }

    /** Saving. */
    public void testSave() throws IOException {
       	String[] expected = new String[] {"", "hallo = velo", "# english",
       	                                  "hello = bike"};
        IniEditor.Section s = new IniEditor.Section("test");
       	s.addBlankLine();
       	s.set("hallo", "velo");
       	s.addComment("english");
       	s.set("hello", "bike");
        File f = File.createTempFile("test", null);
        s.save(new PrintWriter(new FileOutputStream(f)));
        Object[] saved = fileToStrings(f);
        assertTrue(Arrays.equals(expected, saved));
    }

	/** Loading. */
    public void testLoad() throws IOException {
		String[] expected = new String[] {
		      "hallo = velo"
		    , "hello = bike"
		    , "hi = cycle"
		    , "ciao : bicicletta"
		    , "# some comment"
		    , ""
		};
		String[] feed = new String[] {
		      "hallo=velo"
		    , "hello bike"
		    , "hi = cycle"
		    , "ciao: bicicletta"
		    , "# some comment"
		    , ""
		    , "[nextsection]"
		    , "hallo = bike"
		};
		File f = toTempFile(feed);
		IniEditor.Section s = new IniEditor.Section("test");
		s.load(new BufferedReader(new FileReader(f)));
		assertEquals(s.get("hallo"), "velo");
		assertEquals(s.get("hello"), "bike");
		assertEquals(s.get("hi"), "cycle");
		assertEquals(s.get("ciao"), "bicicletta");
		assertEquals(s.optionNames().size(), 4);
		s.save(new PrintWriter(new FileOutputStream(f)));
		Object[] saved = fileToStrings(f);
		//System.out.println(Arrays.asList(saved));
		assertTrue(Arrays.equals(expected, saved));
		// XXX need more tests
	}

    public void testSetOptionFormat() throws IOException {
       	String[] formats = new String[] {
       	    "%s %s %s", "%s%s%s", "b%s%%%s%%%%%sa"
        };
       	String[] expected = new String[] {
       	    "hallo = velo", "hallo=velo", "bhallo%=%%veloa"
        };
       	for (int i = 0; i < formats.length; i++) {
            IniEditor.Section s = new IniEditor.Section("test");
            s.setOptionFormatString(formats[i]);
            s.set("hallo", "velo");
            File f = File.createTempFile("test", null);
            s.save(new PrintWriter(new FileOutputStream(f)));
            Object[] saved = fileToStrings(f);
            assertEquals(expected[i], saved[0]);
        }
    }

	public void testSetIllegalOptionFormat() {
        String[] formats = {"%s %s % %s", "%s %s", "%s%s%s%s", "%s %d %s %s"};
        IniEditor.Section s = new IniEditor.Section("test");
        for (int i = 0; i < formats.length; i++) {
            try {
                s.setOptionFormatString(formats[i]);
                fail("Should throw IllegalArgumentException");
            }
            catch (IllegalArgumentException ex) { /* ok, this should happen */ }
        }
	}

    private static Object[] fileToStrings(File f) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(f));
        List<String> l = new LinkedList<String>();
        while (r.ready()) {
            l.add(r.readLine());
        }
        return l.toArray();
    }

	private static File toTempFile(String text) throws IOException {
		File temp = File.createTempFile("inieditortest", null);
		FileWriter fw = new FileWriter(temp);
		fw.write(text);
		fw.close();
		return temp;
	}

	private static File toTempFile(String[] lines) throws IOException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lines.length; i++) {
			sb.append(lines[i] + "\n");
		}
		return toTempFile(sb.toString());
	}

    public static Test suite() {
        return new TestSuite(SectionTest.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
