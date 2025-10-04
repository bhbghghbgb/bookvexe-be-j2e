package org.example.bookvexebej2e.controllers.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.controllers.admin.base.BaseAdminController;
import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.requests.PaymentQueryRequest;
import org.example.bookvexebej2e.services.admin.PaymentAdminService;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/payments")
@Tag(name = "Payment Admin", description = "Payment management APIs for administrators")
public class PaymentAdminController extends BaseAdminController<PaymentDbModel, Integer, PaymentQueryRequest> {

    private final PaymentAdminService paymentService;

    public PaymentAdminController(PaymentAdminService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    protected BaseAdminService<PaymentDbModel, Integer, PaymentQueryRequest> getService() {
        return paymentService;
    }
}
