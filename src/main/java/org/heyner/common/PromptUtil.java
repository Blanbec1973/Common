package org.heyner.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class PromptUtil {
    private static final Logger logger = LogManager.getLogger(PromptUtil.class);

    private PromptUtil() {}
    public static boolean getYesOrNoResponse(String message) {
        Scanner scanner = new Scanner(System.in);
        logger.info("{} (y/n)", message);
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y");
    }




}
