package org.example.bookvexebej2e.models.constant;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Module {

    @Data
    public static class ModuleDto {
        public String code;
        public String name;
        public String description;
        public String urlPageView;

        private Boolean isCanRead = true;
        private Boolean isCanCreate = true;
        private Boolean isCanUpdate = true;
        private Boolean isCanDelete = true;
        private Boolean isCanActivate = true;
        private Boolean isCanDeactivate = true;
        private Boolean isCanImport = true;
        private Boolean isCanExport = true;
    }

    public List<ModuleDto> getModuleList() {
        List<ModuleDto> result = new ArrayList<>();
        result.add(create("CUSTOMER", "Quản lý khách hàng", "Quản lý thông tin khách hàng", "/customer"));
        result.add(create("USER", "Quản lý người dùng", "Quản lý tài khoản và thông tin người dùng", "/user"));
        result.add(create("CAR", "Quản lý xe", "Quản lý thông tin phương tiện vận chuyển", "/car"));
        result.add(create("CAR_TYPE", "Quản lý xe", "Quản lý thông tin loại phương tiện vận chuyển", "/car-type"));
        result.add(create("ROUTE", "Quản lý tuyến", "Quản lý tuyến đường và lộ trình", "/route"));
        result.add(create("TRIP", "Quản lý chuyến đi", "Quản lý chuyến đi, lịch trình, tài xế và xe", "/trip"));
        result.add(create("BOOKING", "Quản lý đặt xe", "Quản lý đơn đặt vé xe của khách hàng", "/booking"));
        result.add(create("PAYMENT", "Quản lý giao dịch", "Quản lý các giao dịch và thanh toán", "/transaction"));
        result.add(create("PAYMENT_METHOD", "Quản lý phương thức thanh toán", "Quản lý các hình thức thanh toán", "/payment-method"));
        result.add(create("ROLE", "Quản lý vai trò", "Quản lý vai trò trong hệ thống", "/role"));
        result.add(create("PERMISSION", "Phân quyền", "Phân quyền", "/permission"));

        return result;
    }

    private ModuleDto create(String code, String name, String description, String urlPageView) {
        ModuleDto dto = new ModuleDto();
        dto.code = code;
        dto.name = name;
        dto.description = description;
        dto.urlPageView = urlPageView;
        return dto;
    }
}
