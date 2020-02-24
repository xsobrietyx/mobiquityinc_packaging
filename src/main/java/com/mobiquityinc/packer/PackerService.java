package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;

/**
 * Service class that used to keep signature for the assignment. Also acts as facade.
 */
public class PackerService {

    private static final PackerTemplate packer = new DefaultPacker();

    /**
     * Main service method
     *
     * @param filePath file name
     * @return string output
     * @throws APIException business cases related exception
     */
    public static String performPackaging(String filePath) throws APIException {
        return packer.pack(filePath);
    }
}
