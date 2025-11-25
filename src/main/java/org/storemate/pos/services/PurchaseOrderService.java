package org.storemate.pos.services;

import org.storemate.pos.dao.PurchaseDAO;
import org.storemate.pos.dto.PurchaseDTO;
import org.storemate.pos.models.EmailSettings;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.utils.EmailConfigLoader;

public class PurchaseOrderService {

    private final PurchaseDAO purchaseDAO = new PurchaseDAO();

    /**
     * Creates purchase in DB and sends email to supplier.
     * Returns purchaseId (>0) on success or -1 on failure.
     */
    public int createAndSendOrder(PurchaseDTO cart, String status, Supplier supplier) {
        try {
            int purchaseId = purchaseDAO.createPurchase(cart, status, "DEBIT");
            if (purchaseId <= 0) return -1;

            EmailSettings settings = EmailConfigLoader.loadFromResources();
            if (settings == null) {
                System.err.println("Email settings not found");
                return purchaseId; // purchase created, but no email config
            }

            EmailService emailService = new EmailService(settings);
            String html = PurchaseEmailBuilder.buildPurchaseEmail(cart, supplier);

            // send email - throws MessagingException if fails
            emailService.sendHtmlEmail(supplier.getSupplier_email(), "Purchase Order #" + purchaseId, html);

            return purchaseId;
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return -1;
        }
    }
}
