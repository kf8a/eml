/**
 *  '$RCSfile: EMLValidatorTest.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: walbridge $'
 *     '$Date: 2008-11-05 23:00:46 $'
 * '$Revision: 1.6 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA    02111-1307    USA
 */

package org.ecoinformatics.emltest;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.ecoinformatics.eml.EMLValidator;

/**
 * A JUnit test for testing the EMLValidator
 */
public class EMLValidatorTest extends TestCase {
    private final static String TEST_DIR = "./src/test/resources";
    private final static String INVALID_DIR = TEST_DIR + "/invalidEML";

    /**
     * Constructor to build the test
     *
     * @param name  the name of the test method
     */
    public EMLValidatorTest(String name) {
        super(name);
    }

    /** Establish a testing framework by initializing appropriate objects  */
    public void setUp() {
    }

    /** Release any objects after tests are complete  */
    public void tearDown() {
    }

    /**
     * Create a suite of tests to be run together
     *
     * @return   The test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new EMLValidatorTest("initialize"));
        suite.addTest(new EMLValidatorTest("testValidDocs"));
        suite.addTest(new EMLValidatorTest("testInvalidDocs"));
        suite.addTest(new EMLValidatorTest("testStringInput"));
        return suite;
    }

    /**
     * Check that the testing framework is functioning properly with a trivial
     * assertion.
     */
    public void initialize() {
        assertTrue(true);
    }

    public void testValidDocs() {
        // all valid documents should validate
        File testDir = new File(TEST_DIR);
        ArrayList fileList = getXmlFiles(testDir);
        for (int i=0; i < fileList.size(); i++) {
            File testFile = (File)fileList.get(i);
            try {
                System.err.println("Validating file: " + testFile.getName());
                EMLValidator validator = new EMLValidator(testFile);
                boolean isValid = validator.validate();
                if (!isValid) {
                    for (String e : validator.getErrors()) {
                        System.err.println(e);
                    }
                    fail("Validator: NOT valid: " + testFile.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                fail("Validator exception!\n\n" + e.getClass().getName() +
                     "(" + e.getMessage() + ")" );
            }
        }
    }

    public void testInvalidDocs() {
        // None of the invalid files should validate
        // NOTE: EMLParser does not validate against the schema (see SAXParserTest)
        int failures = 0;
        File invalidDir = new File(INVALID_DIR);
        ArrayList invalidList = getXmlFiles(invalidDir);
        int invalidFileCount = invalidList.size();
        System.err.println("Checking invalid files: " + invalidFileCount);
        for (int i=0; i < invalidFileCount; i++) {
            File invalidFile = (File)invalidList.get(i);
            System.err.println("Invalidating file: " + invalidFile.getName());
            try {
                EMLValidator validator = new EMLValidator(invalidFile);
                boolean isValid = validator.validate();
                if (!isValid) {
                    System.err.println("    Invalid, which is right.");
                    failures++;
                } else {
                    System.err.println("    Valid, which it shouldn't be.");
                    fail("Validator: found valid when should not be: " + invalidFile.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                fail("Validator exception!\n\n" + e.getClass().getName() +
                     "(" + e.getMessage() + ")" );
            }
        }
        if (failures != invalidFileCount) {
            System.err.println(failures + "/" + invalidFileCount + " failures in directory.");
            fail("Error: An error should have been thrown for all invalid files.");
        }
    }

    public void testStringInput() {
        // document should validate when passed in as a String
        File testDir = new File(TEST_DIR);
        File testFile = new File(TEST_DIR, "eml-sample.xml");
        try {
            System.err.println("Validating string input for: " + testFile.getName());
            String emltext = new String(Files.readAllBytes(Paths.get(testFile.getAbsolutePath())), StandardCharsets.UTF_8);
            EMLValidator validator = new EMLValidator(emltext);
            boolean isValid = validator.validate();
            if (!isValid) {
                for (String e : validator.getErrors()) {
                    System.err.println(e);
                }
                fail("Validator: NOT valid: " + testFile.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Validator exception!\n\n" + e.getClass().getName() +
                 "(" + e.getMessage() + ")" );
        }
    }

    /**
     * Get the list of files in a directory.
     *
     * @param directory the directory to list
     * @return a vector of File objects in the directory
     */
    private ArrayList getXmlFiles(File directory) {
        String[] files = directory.list();
        ArrayList fileList = new ArrayList();

        for (int i=0; i < files.length; i++) {
            String filename = files[i];
            File currentFile = new File(directory, filename);
            if (currentFile.isFile() && filename.endsWith(".xml") && !filename.startsWith("stmml")) {
                fileList.add(currentFile);
            }
        }
        return fileList;
    }
}
