package com.mobiquityinc.packer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Parametrized test that covers the different types of exception cases.
 */
@RunWith(JUnitParamsRunner.class)
public class APIExceptionTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PackerTemplate packerTemplate = new DefaultPacker();

    @Test(expected = APIException.class)
    @Parameters({"wrongItemCost.txt",
            "wrongItemsCount.txt",
            "wrongItemWeight.txt",
            "wrongPackageWeight.txt"})
    public void testAllCasesExceptionThrown(String fileName) {
        File file = packerTemplate.getFile(fileName);
        packerTemplate.getItems(file);
    }

    @Test
    @Parameters({"wrongPackageWeight.txt | Package weight limit exceeded.",
            "wrongItemCost.txt | Item constraints",
            "wrongItemWeight.txt | Item constraints",
            "wrongItemsCount.txt | Items count limit exceeded."})
    public void testAllCasesCorrectExceptionMessageThrown(String fileName, String exceptionMessage) {
        expectedException.expect(APIException.class);
        expectedException.expectMessage(exceptionMessage);

        File file = packerTemplate.getFile(fileName);
        packerTemplate.getItems(file);
    }
}
