package com.mobiquityinc.packer;

/**
 * Service class that used to keep signature for the assignment. Also acts as facade.
 */
public class PackerService {
    /**
     * Main service method
     * @param filePath file name
     * @return string output
     * @throws APIException business cases related exception
     */
    public static String performPackaging(String filePath) throws APIException {
        DefaultPacker p = new DefaultPacker();
        return p.pack(filePath);
    }
}
