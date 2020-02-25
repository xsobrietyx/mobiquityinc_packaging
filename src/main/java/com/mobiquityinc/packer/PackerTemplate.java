package com.mobiquityinc.packer;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.mobiquityinc.exception.APIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mobiquityinc.packer.PackerConstants.MAX_ITEMS_AMOUNT;
import static com.mobiquityinc.packer.PackerConstants.MAX_ITEM_COST;
import static com.mobiquityinc.packer.PackerConstants.MAX_ITEM_WEIGHT;
import static com.mobiquityinc.packer.PackerConstants.MAX_PACKAGE_WEIGHT;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;

/**
 * Abstract class that act as Template Method pattern. All methods except pack() could be implemented in another
 * different manner in the child class(es)
 */
abstract class PackerTemplate {
    /**
     * Main processing method, holds all method calls within.
     *
     * @param fileName name of file with initial data
     * @return string output returned by {@link com.mobiquityinc.packer.PackerTemplate#getOutput(Map)}
     * @throws APIException exception related to the wrong initial data format
     */
    final String pack(String fileName) throws APIException {
        File file = getFile(fileName);
        Multimap<String, Item> items = getItems(file);
        Map<String, ArrayList<Item>> packs = processItems(items);
        return getOutput(packs);
    }

    /**
     * Method that performs mostly string related transformations. Holds all the logic related to the output data format.
     *
     * @param packs map with packages as keys and list of items as items in package
     * @return string output in proper format
     */
    String getOutput(Map<String, ArrayList<Item>> packs) {
        StringBuilder result = new StringBuilder();
        for (String s : packs.keySet()) {
            if (packs.get(s).isEmpty()) result.append("-\n");
            else {
                for (Item item : packs.get(s)) {
                    result.append(item.toString()).append(",");
                }
                result.deleteCharAt(result.toString().length() - 1);
                result.append("\n");
            }

        }
        return result.deleteCharAt(result.toString().length() - 1).reverse().append("\n").toString();
    }

    /**
     * Main initial processing method that holds the logic related to the initial transformation and exception handling.
     *
     * @param file initial data as file
     * @return multimap, where keys is package weight and value is list of items that should be filtered
     */
    Multimap<String, Item> getItems(File file) {
        Multimap<String, Item> res = TreeMultimap.create();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.lines()
                    .forEach(s -> {
                        String[] weightAndRawItems = s.split(":");
                        String weight = weightAndRawItems[0];

                        if (parseInt(weight.trim()) > MAX_PACKAGE_WEIGHT)
                            throw new APIException("Package weight limit exceeded.");

                        String rawItem = weightAndRawItems[1];

                        if (Arrays.stream(rawItem
                                .replaceFirst(" ", "")
                                .split(" ")).count() > MAX_ITEMS_AMOUNT)
                            throw new APIException("Items count limit exceeded.");

                        res.putAll(weight.trim(), Arrays.stream(rawItem
                                .replaceFirst(" ", "")
                                .split(" "))
                                .map(item -> item.replaceAll("\\(", "")
                                        .replaceAll("\\)", ""))
                                .map(item -> {

                                    String[] itemData = item.split(",");
                                    String itemWeight = itemData[1];
                                    String itemCost = itemData[2].substring(1);

                                    if (parseInt(itemCost) > MAX_ITEM_COST || parseDouble(itemWeight) > MAX_ITEM_WEIGHT)
                                        throw new APIException("Item constraints not valid. Please check item weight and/or cost");
                                    return new Item(parseInt(itemData[0]),
                                            parseDouble(itemWeight),
                                            parseInt(itemCost));
                                }).collect(Collectors.toList()));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Core processing method that holds business logic implementation.
     * Filters items that should be packed by item cost and item weight.
     *
     * @param things multimap from {@link com.mobiquityinc.packer.PackerTemplate#getItems(File)}
     * @return map with package weights as key and filtered list of items as value
     */
    Map<String, ArrayList<Item>> processItems(Multimap<String, Item> things) {

        Map<String, ArrayList<Item>> result = new LinkedHashMap<>();
        Iterable<String> keys = things.keySet();

        for (String key : keys) {
            result.put(key, new ArrayList<>(MAX_ITEMS_AMOUNT));
        }

        for (Map.Entry<String, Item> entry : things.entries()) {
            result.computeIfPresent(entry.getKey(), (s, integers) -> {
                Item entryValue = entry.getValue();
                if ((Arrays.stream(integers.toArray())
                        .mapToDouble(value ->
                                ((Item) value).getWeight()).sum() + entryValue.getWeight()) < parseInt(entry.getKey().trim())) {
                    integers.add(entryValue);
                }
                return integers;
            });
        }

        return result;
    }

    /**
     * Method that reads file
     *
     * @param fileName name of file that should be parsed
     * @return file object
     */
    File getFile(String fileName) {
        ClassLoader classloader = currentThread().getContextClassLoader();

        return new File(requireNonNull(classloader.getResource(fileName)).getFile());
    }
}
