package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
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
    @FileParameters("src/test/resources/fileNames.csv")
    public void testAllCasesExceptionThrown(String fileName) {
        File file = packerTemplate.getFile(fileName);
        packerTemplate.getItems(file);
    }

    @Test
    @FileParameters("src/test/resources/fileNamesToMessages.csv")
    public void testAllCasesCorrectExceptionMessageThrown(String fileName, String exceptionMessage) {
        expectedException.expect(APIException.class);
        expectedException.expectMessage(exceptionMessage);

        File file = packerTemplate.getFile(fileName);
        packerTemplate.getItems(file);
    }
}
