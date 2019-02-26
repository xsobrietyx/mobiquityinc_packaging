package com.mobiquityinc.packer;

/**
 * Service class that used to keep signature for the assignment. Also acts as facade.
 */
public class PackerService {
    public static String performPackaging(String filePath) throws APIException {
        DefaultPacker p = new DefaultPacker();
        return p.pack(filePath);
    }
}
