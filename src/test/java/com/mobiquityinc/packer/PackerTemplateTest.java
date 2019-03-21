package com.mobiquityinc.packer;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Main test cases holder class.
 * Method names are refer to the method names of class that covered with tests.
 * Class name refer to the class that covered with tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class PackerTemplateTest {
    private PackerTemplate packerTemplate;
    private final String fileName = "correctInput.txt";

    @Before
    public void setup() {
        packerTemplate = Mockito.spy(new DefaultPacker());
    }

    @Test
    public void testGetFile() {
        File testFile = packerTemplate.getFile(fileName);

        assertEquals(testFile.getName(), fileName);
    }

    @Test
    public void testGetItems() {
        File file = packerTemplate.getFile(fileName);
        Multimap<String, Item> items = packerTemplate.getItems(file);

        assertNotNull(items);
        assertNotEquals(Collections.emptyMap(), items.asMap());
        assertEquals(items.size(), 25);
    }

    @Test
    public void testProcessItems() {
        File file = packerTemplate.getFile(fileName);
        Multimap<String, Item> items = packerTemplate.getItems(file);
        Map<String, ArrayList<Item>> processedItems = packerTemplate.processItems(items);

        assertNotNull(processedItems);
        assertEquals(processedItems.size(), 4);
        assertEquals(processedItems.keySet().toString(), "[56, 75, 8, 81]");
        assertEquals(processedItems.get("56").size() == 2, processedItems.get("75").size() == 2);
        assertEquals(processedItems.get("81").size(), 1);

        assertEquals(processedItems.get("8"), Collections.emptyList());
        assertTrue(processedItems.get("56").stream().anyMatch(item -> item.getIndex().equals(8)));
        assertTrue(processedItems.get("56").stream().anyMatch(item -> item.getIndex().equals(9)));
        assertTrue(processedItems.get("81").stream().anyMatch(item -> item.getIndex().equals(4)));
        assertTrue(processedItems.get("75").stream().anyMatch(item -> item.getIndex().equals(7)));
        assertTrue(processedItems.get("75").stream().anyMatch(item -> item.getIndex().equals(2)));
    }

    @Test
    public void testGetOutput() {
        File file = packerTemplate.getFile(fileName);
        Multimap<String, Item> items = packerTemplate.getItems(file);
        Map<String, ArrayList<Item>> processedItems = packerTemplate.processItems(items);
        String output = packerTemplate.getOutput(processedItems);
        String expectedOutput = "4\n" +
                "-\n" +
                "7,2\n" +
                "9,8\n";

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testPack() {
        packerTemplate.pack(fileName);

        verify(packerTemplate, times(1)).getFile(anyString());
        verify(packerTemplate, times(1)).getItems(any(File.class));
        verify(packerTemplate, times(1)).processItems(any());
        verify(packerTemplate, times(1)).getOutput(anyMap());
    }
}
