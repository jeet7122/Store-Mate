package org.storemate.pos.services;

import org.storemate.pos.dao.PurchaseDAO;
import org.storemate.pos.dto.PurchaseDTO;
import org.storemate.pos.models.EmailSettings;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.utils.EmailConfigLoader;


/**
 * Service class responsible for managing purchase orders.
 * <p>
 * This class handles creating purchase records in the database and sending
 * email notifications to the respective suppliers. It integrates with
 * {@link PurchaseDAO} for database operations and {@link EmailService} for
 * sending emails.
 * </p>
 * @author Jeet
 */
public class PurchaseOrderService {

    private final PurchaseDAO purchaseDAO = new PurchaseDAO();

    /**
     * Creates a purchase order in the database and sends an HTML email
     * to the specified supplier.
     *
     * @param cart     the {@link PurchaseDTO} containing purchase details, products, quantities, and pricing
     * @param status   the status of the purchase order (e.g., "PENDING", "COMPLETED")
     * @param supplier the {@link Supplier} to whom the purchase order is addressed
     * @return the ID of the created purchase order if successful; returns -1 if there was an error
     *         during creation or email sending
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
