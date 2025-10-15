package org.blood_bank.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class FlashScope {

    private static final String FLASH_MESSAGE_KEY = "flashMessage";

    private FlashScope() {
    }

    public static void setFlashMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute(FLASH_MESSAGE_KEY, message);
    }

    public static String consumeFlashMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        String message = (String) session.getAttribute(FLASH_MESSAGE_KEY);
        if (message != null) {
            session.removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }
}

