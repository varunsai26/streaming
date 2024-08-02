package com.varun.streaming.utility;

import java.io.File;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtility {

    /**
     * Deletes a directory and all its contents recursively.
     *
     * @param directory The directory to be deleted.
     * @return true if the directory and all its contents were deleted successfully, false otherwise.
     */
    public static boolean deleteDirectory(File directory) {
        // Check if the directory exists
        if (directory == null || !directory.exists()) {
            return false;
        }

        // Check if it's a directory
        if (!directory.isDirectory()) {
            return false;
        }

        // Get all files and subdirectories
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively delete subdirectories
                    deleteDirectory(file);
                } else {
                    // Delete files
                    if (!file.delete()) {
                        return false;
                    }
                }
            }
        }

        // Delete the now-empty directory
        return directory.delete();
    }
}
