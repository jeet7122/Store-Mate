package org.storemate.pos.services;

import org.storemate.pos.dto.PurchaseDTO;
import org.storemate.pos.models.Supplier;

public class PurchaseEmailBuilder {
    public static String buildPurchaseEmail(PurchaseDTO dto, Supplier supplier) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2>Purchase Order</h2>");
        sb.append("<p><strong>Supplier:</strong> ").append(supplier.getSupplier_name()).append("</p>");
        sb.append("<table style='border-collapse:collapse' border='1' cellpadding='6'>");
        sb.append("<tr><th>Product</th><th>Qty</th><th>Unit</th><th>Total</th></tr>");
        dto.getCart().forEach(item -> {
            sb.append("<tr>");
            sb.append("<td>").append(item.getProduct().getProduct_name()).append("</td>");
            sb.append("<td align='right'>").append(item.getQuantity()).append("</td>");
            sb.append("<td align='right'>").append(item.getCostPrice()).append("</td>");
            sb.append("<td align='right'>").append(item.getTotalAmount()).append("</td>");
            sb.append("</tr>");
        });
        sb.append("</table>");
        sb.append("<p><strong>Subtotal:</strong> ").append(dto.getSubtotal()).append("</p>");
        sb.append("<p><strong>Tax:</strong> ").append(dto.getTax()).append("</p>");
        sb.append("<h3>Total: ").append(dto.getTotal()).append("</h3>");
        sb.append("</body></html>");
        return sb.toString();
    }
}
