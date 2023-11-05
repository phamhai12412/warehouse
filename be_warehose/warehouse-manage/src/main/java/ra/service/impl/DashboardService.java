package ra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ra.dto.response.*;

import ra.model.Category;
import ra.model.DeliveryName;
import ra.model.StatusName;
import ra.model.TypeStorage;
import ra.repository.IBillRepository;
import ra.repository.ICategoryRepository;
import ra.repository.IProductRepository;
import ra.repository.IUserRepository;
import ra.service.IDashboardService;

import java.awt.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DashboardService implements IDashboardService {

    @Autowired
    private IBillRepository billRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IUserRepository userRepository;

    //biểu đồ doanh thu theo kho
//    data = [
//    {
//        name: 'Tháng 1',
//                GVC: 4000,
//
//    },
//    {
//        name: 'Tháng 2',
//                GVC: 3000,
//
//    },
//    ]
    @Override
    public List<DashboardResponse> findByCreatedGVCStorageId(Long storageId, Integer yearValue) {
        List<DashboardResponse> dashboardResponseList = new ArrayList<>();
//        Year currentYear = Year.now();
//        int yearValue = currentYear.getValue();

        Double t1 = billRepository.findByCreatedGVCStorageId(yearValue, 1, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t2 = billRepository.findByCreatedGVCStorageId(yearValue, 2, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t3 = billRepository.findByCreatedGVCStorageId(yearValue, 3, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t4 = billRepository.findByCreatedGVCStorageId(yearValue, 4, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t5 = billRepository.findByCreatedGVCStorageId(yearValue, 5, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t6 = billRepository.findByCreatedGVCStorageId(yearValue, 6, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t7 = billRepository.findByCreatedGVCStorageId(yearValue, 7, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t8 = billRepository.findByCreatedGVCStorageId(yearValue, 8, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t9 = billRepository.findByCreatedGVCStorageId(yearValue, 9, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t10 = billRepository.findByCreatedGVCStorageId(yearValue, 10, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t11 = billRepository.findByCreatedGVCStorageId(yearValue, 11, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        Double t12 = billRepository.findByCreatedGVCStorageId(yearValue, 12, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        if (t1 == null) {
            t1 = (double) 0;
        }
        if (t2 == null) {
            t2 = (double) 0;
        }
        if (t3 == null) {
            t3 = (double) 0;
        }
        if (t4 == null) {
            t4 = (double) 0;
        }
        if (t5 == null) {
            t5 = (double) 0;
        }
        if (t6 == null) {
            t6 = (double) 0;
        }
        if (t7 == null) {
            t7 = (double) 0;
        }
        if (t8 == null) {
            t8 = (double) 0;
        }
        if (t9 == null) {
            t9 = (double) 0;
        }
        if (t10 == null) {
            t10 = (double) 0;
        }
        if (t11 == null) {
            t11 = (double) 0;
        }
        if (t12 == null) {
            t12 = (double) 0;
        }
        dashboardResponseList.add(new DashboardResponse("Tháng 1", t1));
        dashboardResponseList.add(new DashboardResponse("Tháng 2", t2));
        dashboardResponseList.add(new DashboardResponse("Tháng 3", t3));
        dashboardResponseList.add(new DashboardResponse("Tháng 4", t4));
        dashboardResponseList.add(new DashboardResponse("Tháng 5", t5));
        dashboardResponseList.add(new DashboardResponse("Tháng 6", t6));
        dashboardResponseList.add(new DashboardResponse("Tháng 7", t7));
        dashboardResponseList.add(new DashboardResponse("Tháng 8", t8));
        dashboardResponseList.add(new DashboardResponse("Tháng 9", t9));
        dashboardResponseList.add(new DashboardResponse("Tháng 10", t10));
        dashboardResponseList.add(new DashboardResponse("Tháng 11", t11));
        dashboardResponseList.add(new DashboardResponse("Tháng 12", t12));

        return dashboardResponseList;
    }

    // biểu đồ doanh thu admin
    @Override
    public List<DashboardResponse> findByCreatedGVCS(Integer yearValue) {
        List<DashboardResponse> dashboardResponseList = new ArrayList<>();
//        Year currentYear = Year.now();
//        int yearValue = currentYear.getValue();

        Double t1 = billRepository.findByCreatedGVCS(yearValue, 1, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t2 = billRepository.findByCreatedGVCS(yearValue, 2, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t3 = billRepository.findByCreatedGVCS(yearValue, 3, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t4 = billRepository.findByCreatedGVCS(yearValue, 4, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t5 = billRepository.findByCreatedGVCS(yearValue, 5, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t6 = billRepository.findByCreatedGVCS(yearValue, 6, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t7 = billRepository.findByCreatedGVCS(yearValue, 7, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t8 = billRepository.findByCreatedGVCS(yearValue, 8, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t9 = billRepository.findByCreatedGVCS(yearValue, 9, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t10 = billRepository.findByCreatedGVCS(yearValue, 10, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t11 = billRepository.findByCreatedGVCS(yearValue, 11, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        Double t12 = billRepository.findByCreatedGVCS(yearValue, 12, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        if (t1 == null) {
            t1 = (double) 0;
        }
        if (t2 == null) {
            t2 = (double) 0;
        }
        if (t3 == null) {
            t3 = (double) 0;
        }
        if (t4 == null) {
            t4 = (double) 0;
        }
        if (t5 == null) {
            t5 = (double) 0;
        }
        if (t6 == null) {
            t6 = (double) 0;
        }
        if (t7 == null) {
            t7 = (double) 0;
        }
        if (t8 == null) {
            t8 = (double) 0;
        }
        if (t9 == null) {
            t9 = (double) 0;
        }
        if (t10 == null) {
            t10 = (double) 0;
        }
        if (t11 == null) {
            t11 = (double) 0;
        }
        if (t12 == null) {
            t12 = (double) 0;
        }
        dashboardResponseList.add(new DashboardResponse("Tháng 1", t1));
        dashboardResponseList.add(new DashboardResponse("Tháng 2", t2));
        dashboardResponseList.add(new DashboardResponse("Tháng 3", t3));
        dashboardResponseList.add(new DashboardResponse("Tháng 4", t4));
        dashboardResponseList.add(new DashboardResponse("Tháng 5", t5));
        dashboardResponseList.add(new DashboardResponse("Tháng 6", t6));
        dashboardResponseList.add(new DashboardResponse("Tháng 7", t7));
        dashboardResponseList.add(new DashboardResponse("Tháng 8", t8));
        dashboardResponseList.add(new DashboardResponse("Tháng 9", t9));
        dashboardResponseList.add(new DashboardResponse("Tháng 10", t10));
        dashboardResponseList.add(new DashboardResponse("Tháng 11", t11));
        dashboardResponseList.add(new DashboardResponse("Tháng 12", t12));

        return dashboardResponseList;
    }

    //doanh thu theo năm admin
    @Override
    public Double GVCSAndByYear() {
        Year currentYear = Year.now();
        int yearValue = currentYear.getValue();
        Double total = billRepository.GVCSByYear(yearValue, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        if (total == null) {
            total = (double) 0;
        }
        return total;
    }

    //tổng số bill  giao thành công tới siêu thị admin
    @Override
    public Integer billByYear() {
        Year currentYear = Year.now();
        int yearValue = currentYear.getValue();
        Integer total = billRepository.TotalBillByYear(yearValue, DeliveryName.SUCCESS, TypeStorage.SUPERMARKET);
        if (total == null) {
            total = (int) 0;
        }
        return total;
    }

    //doanh thu theo năm của kho
    @Override
    public Double GVCSByYearAndStorage(Long storageId) {
        Year currentYear = Year.now();
        int yearValue = currentYear.getValue();
        Double total = billRepository.GVCSByYearAndStorage(yearValue, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        if (total == null) {
            total = (double) 0;
        }
        return total;
    }

    //tổng số bill giao thành công tới siêu thị của kho
    @Override
    public Integer billByYearAndStorage(Long storageId) {
        Year currentYear = Year.now();
        int yearValue = currentYear.getValue();
        Integer total = billRepository.TotalBillByYearAndStorage(yearValue, DeliveryName.SUCCESS, storageId, TypeStorage.SUPERMARKET);
        if (total == null) {
            total = (int) 0;
        }
        return total;
    }

    //tổng sản pẩm admin
    @Override
    public Integer getTotalProductByStatus() {
        Integer total = productRepository.totalProduct(StatusName.ACCEPT);
        if (total == null) {
            total = 0;
        }
        return total;
    }

    //tổng sản phẩm theo kho
    @Override
    public Integer getTotalProductByStatusAndStorage(Long storageId) {
        Integer total = productRepository.totalProductStorage(StatusName.ACCEPT, storageId);
        if (total == null) {
            total = 0;
        }
        return total;
    }

    //tổng nhân viên
    @Override
    public Integer getTotalUser() {
        Integer total = userRepository.totalUsers(true);
        if (total == null) {
            total = 0;
        }
        return total;
    }

    //biểu đồ nhập, xuất  kho theo ngày kho đầu vào kho và tháng lựa chọn
    @Override
    public List<DashboardExportImportResponse> dashboardExportImport(Long storageId, Integer month, Integer yearValue) {
        List<DashboardExportImportResponse> dashboardExportImportResponses = new ArrayList<>();
//        Year currentYear = Year.now();
//        int yearValue = currentYear.getValue();
        Integer t1 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 1, storageId);
        Integer t2 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 2, storageId);
        Integer t3 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 3, storageId);
        Integer t4 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 4, storageId);
        Integer t5 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 5, storageId);
        Integer t6 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 6, storageId);
        Integer t7 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 7, storageId);
        Integer t8 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 8, storageId);
        Integer t9 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 9, storageId);
        Integer t10 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 10, storageId);
        Integer t11 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 11, storageId);
        Integer t12 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 12, storageId);
        Integer t13 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 13, storageId);
        Integer t14 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 14, storageId);
        Integer t15 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 15, storageId);
        Integer t16 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 16, storageId);
        Integer t17 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 17, storageId);
        Integer t18 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 18, storageId);
        Integer t19 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 19, storageId);
        Integer t20 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 20, storageId);
        Integer t21 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 21, storageId);
        Integer t22 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 22, storageId);
        Integer t23 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 23, storageId);
        Integer t24 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 24, storageId);
        Integer t25 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 25, storageId);
        Integer t26 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 26, storageId);
        Integer t27 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 27, storageId);
        Integer t28 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 28, storageId);
        Integer t29 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 29, storageId);
        Integer t30 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 30, storageId);
        Integer t31 = billRepository.findByCreatedAndStorageAndImportDashboard(yearValue, month, 31, storageId);

        if (t1 == null) {
            t1 = 0;
        }
        if (t2 == null) {
            t2 = 0;
        }
        if (t3 == null) {
            t3 = 0;
        }
        if (t4 == null) {
            t4 = 0;
        }
        if (t5 == null) {
            t5 = 0;
        }
        if (t6 == null) {
            t6 = 0;
        }
        if (t7 == null) {
            t7 = 0;
        }
        if (t8 == null) {
            t8 = 0;
        }
        if (t9 == null) {
            t9 = 0;
        }
        if (t10 == null) {
            t10 = 0;
        }
        if (t11 == null) {
            t11 = 0;
        }
        if (t12 == null) {
            t12 = 0;
        }
        if (t13 == null) {
            t13 = 0;
        }
        if (t14 == null) {
            t14 = 0;
        }
        if (t15 == null) {
            t15 = 0;
        }
        if (t16 == null) {
            t16 = 0;
        }
        if (t17 == null) {
            t17 = 0;
        }
        if (t18 == null) {
            t18 = 0;
        }
        if (t19 == null) {
            t19 = 0;
        }
        if (t20 == null) {
            t20 = 0;
        }
        if (t21 == null) {
            t21 = 0;
        }
        if (t22 == null) {
            t22 = 0;
        }
        if (t23 == null) {
            t23 = 0;
        }
        if (t24 == null) {
            t24 = 0;
        }
        if (t25 == null) {
            t25 = 0;
        }
        if (t26 == null) {
            t26 = 0;
        }
        if (t27 == null) {
            t27 = 0;
        }
        if (t28 == null) {
            t28 = 0;
        }
        if (t29 == null) {
            t29 = 0;
        }
        if (t30 == null) {
            t30 = 0;
        }
        if (t31 == null) {
            t31 = 0;
        }
        Integer xt1 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 1, storageId);
        Integer xt2 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 2, storageId);
        Integer xt3 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 3, storageId);
        Integer xt4 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 4, storageId);
        Integer xt5 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 5, storageId);
        Integer xt6 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 6, storageId);
        Integer xt7 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 7, storageId);
        Integer xt8 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 8, storageId);
        Integer xt9 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 9, storageId);
        Integer xt10 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 10, storageId);
        Integer xt11 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 11, storageId);
        Integer xt12 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 12, storageId);
        Integer xt13 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 13, storageId);
        Integer xt14 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 14, storageId);
        Integer xt15 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 15, storageId);
        Integer xt16 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 16, storageId);
        Integer xt17 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 17, storageId);
        Integer xt18 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 18, storageId);
        Integer xt19 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 19, storageId);
        Integer xt20 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 20, storageId);
        Integer xt21 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 21, storageId);
        Integer xt22 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 22, storageId);
        Integer xt23 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 23, storageId);
        Integer xt24 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 24, storageId);
        Integer xt25 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 25, storageId);
        Integer xt26 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 26, storageId);
        Integer xt27 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 27, storageId);
        Integer xt28 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 28, storageId);
        Integer xt29 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 29, storageId);
        Integer xt30 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 30, storageId);
        Integer xt31 = billRepository.findByCreatedAndStorageAndExportDashboard(yearValue, month, 31, storageId);

        if (xt1 == null) {
            xt1 = 0;
        }
        if (xt2 == null) {
            xt2 = 0;
        }
        if (xt3 == null) {
            xt3 = 0;
        }
        if (xt4 == null) {
            xt4 = 0;
        }
        if (xt5 == null) {
            xt5 = 0;
        }
        if (xt6 == null) {
            xt6 = 0;
        }
        if (xt7 == null) {
            xt7 = 0;
        }
        if (xt8 == null) {
            xt8 = 0;
        }
        if (xt9 == null) {
            xt9 = 0;
        }
        if (xt10 == null) {
            xt10 = 0;
        }
        if (xt11 == null) {
            xt11 = 0;
        }
        if (xt12 == null) {
            xt12 = 0;
        }
        if (xt13 == null) {
            xt13 = 0;
        }
        if (xt14 == null) {
            xt14 = 0;
        }
        if (xt15 == null) {
            xt15 = 0;
        }
        if (xt16 == null) {
            xt16 = 0;
        }
        if (xt17 == null) {
            xt17 = 0;
        }
        if (xt18 == null) {
            xt18 = 0;
        }
        if (xt19 == null) {
            xt19 = 0;
        }
        if (xt20 == null) {
            xt20 = 0;
        }
        if (xt21 == null) {
            xt21 = 0;
        }
        if (xt22 == null) {
            xt22 = 0;
        }
        if (xt23 == null) {
            xt23 = 0;
        }
        if (xt24 == null) {
            xt24 = 0;
        }
        if (xt25 == null) {
            xt25 = 0;
        }
        if (xt26 == null) {
            xt26 = 0;
        }
        if (xt27 == null) {
            xt27 = 0;
        }
        if (xt28 == null) {
            xt28 = 0;
        }
        if (xt29 == null) {
            xt29 = 0;
        }
        if (xt30 == null) {
            xt30 = 0;
        }
        if (xt31 == null) {
            xt31 = 0;
        }

        dashboardExportImportResponses.add(new DashboardExportImportResponse("1", t1, xt1));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("2", t2, xt2));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("3", t3, xt3));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("4", t4, xt4));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("5", t5, xt5));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("6", t6, xt6));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("7", t7, xt7));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("8", t8, xt8));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("9", t9, xt9));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("10", t10, xt10));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("11", t11, xt11));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("12", t12, xt12));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("13", t13, xt13));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("14", t14, xt14));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("15", t15, xt15));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("16", t16, xt16));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("17", t17, xt17));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("18", t18, xt18));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("19", t19, xt19));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("20", t20, xt20));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("21", t21, xt21));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("22", t22, xt22));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("23", t23, xt23));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("24", t24, xt24));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("25", t25, xt25));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("26", t26, xt26));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("27", t27, xt27));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("28", t28, xt28));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("29", t29, xt29));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("30", t30, xt30));
        dashboardExportImportResponses.add(new DashboardExportImportResponse("31", t31, xt31));

        return dashboardExportImportResponses;
    }

    //biểu đồ nhập thành công và huỷ đơn nhập theo kho
    @Override
    public List<DashboardImportAndCancelResponse> importAndCancelStorageId(Long storageId, Integer yearValue) {
        List<DashboardImportAndCancelResponse> dashboardResponseList = new ArrayList<>();
//        Year currentYear = Year.now();
//        int yearValue = currentYear.getValue();

        Integer t1 = billRepository.dashboardImportAndCancel(yearValue, 1, DeliveryName.SUCCESS, storageId);
        Integer t2 = billRepository.dashboardImportAndCancel(yearValue, 2, DeliveryName.SUCCESS, storageId);
        Integer t3 = billRepository.dashboardImportAndCancel(yearValue, 3, DeliveryName.SUCCESS, storageId);
        Integer t4 = billRepository.dashboardImportAndCancel(yearValue, 4, DeliveryName.SUCCESS, storageId);
        Integer t5 = billRepository.dashboardImportAndCancel(yearValue, 5, DeliveryName.SUCCESS, storageId);
        Integer t6 = billRepository.dashboardImportAndCancel(yearValue, 6, DeliveryName.SUCCESS, storageId);
        Integer t7 = billRepository.dashboardImportAndCancel(yearValue, 7, DeliveryName.SUCCESS, storageId);
        Integer t8 = billRepository.dashboardImportAndCancel(yearValue, 8, DeliveryName.SUCCESS, storageId);
        Integer t9 = billRepository.dashboardImportAndCancel(yearValue, 9, DeliveryName.SUCCESS, storageId);
        Integer t10 = billRepository.dashboardImportAndCancel(yearValue, 10, DeliveryName.SUCCESS, storageId);
        Integer t11 = billRepository.dashboardImportAndCancel(yearValue, 11, DeliveryName.SUCCESS, storageId);
        Integer t12 = billRepository.dashboardImportAndCancel(yearValue, 12, DeliveryName.SUCCESS, storageId);
        if (t1 == null) {
            t1 = 0;
        }
        if (t2 == null) {
            t2 = 0;
        }
        if (t3 == null) {
            t3 = 0;
        }
        if (t4 == null) {
            t4 = 0;
        }
        if (t5 == null) {
            t5 = 0;
        }
        if (t6 == null) {
            t6 = 0;
        }
        if (t7 == null) {
            t7 = 0;
        }
        if (t8 == null) {
            t8 = 0;
        }
        if (t9 == null) {
            t9 = 0;
        }
        if (t10 == null) {
            t10 = 0;
        }
        if (t11 == null) {
            t11 = 0;
        }
        if (t12 == null) {
            t12 = 0;
        }
        Integer xt1 = billRepository.dashboardImportAndCancel(yearValue, 1, DeliveryName.CANCEL, storageId);
        Integer xt2 = billRepository.dashboardImportAndCancel(yearValue, 2, DeliveryName.CANCEL, storageId);
        Integer xt3 = billRepository.dashboardImportAndCancel(yearValue, 3, DeliveryName.CANCEL, storageId);
        Integer xt4 = billRepository.dashboardImportAndCancel(yearValue, 4, DeliveryName.CANCEL, storageId);
        Integer xt5 = billRepository.dashboardImportAndCancel(yearValue, 5, DeliveryName.CANCEL, storageId);
        Integer xt6 = billRepository.dashboardImportAndCancel(yearValue, 6, DeliveryName.CANCEL, storageId);
        Integer xt7 = billRepository.dashboardImportAndCancel(yearValue, 7, DeliveryName.CANCEL, storageId);
        Integer xt8 = billRepository.dashboardImportAndCancel(yearValue, 8, DeliveryName.CANCEL, storageId);
        Integer xt9 = billRepository.dashboardImportAndCancel(yearValue, 9, DeliveryName.CANCEL, storageId);
        Integer xt10 = billRepository.dashboardImportAndCancel(yearValue, 10, DeliveryName.CANCEL, storageId);
        Integer xt11 = billRepository.dashboardImportAndCancel(yearValue, 11, DeliveryName.CANCEL, storageId);
        Integer xt12 = billRepository.dashboardImportAndCancel(yearValue, 12, DeliveryName.CANCEL, storageId);
        if (xt1 == null) {
            xt1 = 0;
        }
        if (xt2 == null) {
            xt2 = 0;
        }
        if (xt3 == null) {
            xt3 = 0;
        }
        if (xt4 == null) {
            xt4 = 0;
        }
        if (xt5 == null) {
            xt5 = 0;
        }
        if (xt6 == null) {
            xt6 = 0;
        }
        if (xt7 == null) {
            xt7 = 0;
        }
        if (xt8 == null) {
            xt8 = 0;
        }
        if (xt9 == null) {
            xt9 = 0;
        }
        if (xt10 == null) {
            xt10 = 0;
        }
        if (xt11 == null) {
            xt11 = 0;
        }
        if (xt12 == null) {
            xt12 = 0;
        }
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 1", t1, xt1));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 2", t2, xt2));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 3", t3, xt3));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 4", t4, xt4));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 5", t5, xt5));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 6", t6, xt6));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 7", t7, xt7));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 8", t8, xt8));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 9", t9, xt9));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 10", t10, xt10));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 11", t11, xt11));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 12", t12, xt12));

        return dashboardResponseList;
    }

    @Override
    public List<DashboardImportAndCancelResponse> exportAndCancelStorageId(Long storageId, Integer yearValue) {
        List<DashboardImportAndCancelResponse> dashboardResponseList = new ArrayList<>();
//        Year currentYear = Year.now();
//        int yearValue = currentYear.getValue();

        Integer t1 = billRepository.dashboardExportAndCancel(yearValue, 1, DeliveryName.SUCCESS, storageId);
        Integer t2 = billRepository.dashboardExportAndCancel(yearValue, 2, DeliveryName.SUCCESS, storageId);
        Integer t3 = billRepository.dashboardExportAndCancel(yearValue, 3, DeliveryName.SUCCESS, storageId);
        Integer t4 = billRepository.dashboardExportAndCancel(yearValue, 4, DeliveryName.SUCCESS, storageId);
        Integer t5 = billRepository.dashboardExportAndCancel(yearValue, 5, DeliveryName.SUCCESS, storageId);
        Integer t6 = billRepository.dashboardExportAndCancel(yearValue, 6, DeliveryName.SUCCESS, storageId);
        Integer t7 = billRepository.dashboardExportAndCancel(yearValue, 7, DeliveryName.SUCCESS, storageId);
        Integer t8 = billRepository.dashboardExportAndCancel(yearValue, 8, DeliveryName.SUCCESS, storageId);
        Integer t9 = billRepository.dashboardExportAndCancel(yearValue, 9, DeliveryName.SUCCESS, storageId);
        Integer t10 = billRepository.dashboardExportAndCancel(yearValue, 10, DeliveryName.SUCCESS, storageId);
        Integer t11 = billRepository.dashboardExportAndCancel(yearValue, 11, DeliveryName.SUCCESS, storageId);
        Integer t12 = billRepository.dashboardExportAndCancel(yearValue, 12, DeliveryName.SUCCESS, storageId);
        if (t1 == null) {
            t1 = 0;
        }
        if (t2 == null) {
            t2 = 0;
        }
        if (t3 == null) {
            t3 = 0;
        }
        if (t4 == null) {
            t4 = 0;
        }
        if (t5 == null) {
            t5 = 0;
        }
        if (t6 == null) {
            t6 = 0;
        }
        if (t7 == null) {
            t7 = 0;
        }
        if (t8 == null) {
            t8 = 0;
        }
        if (t9 == null) {
            t9 = 0;
        }
        if (t10 == null) {
            t10 = 0;
        }
        if (t11 == null) {
            t11 = 0;
        }
        if (t12 == null) {
            t12 = 0;
        }
        Integer xt1 = billRepository.dashboardExportAndCancel(yearValue, 1, DeliveryName.CANCEL, storageId);
        Integer xt2 = billRepository.dashboardExportAndCancel(yearValue, 2, DeliveryName.CANCEL, storageId);
        Integer xt3 = billRepository.dashboardExportAndCancel(yearValue, 3, DeliveryName.CANCEL, storageId);
        Integer xt4 = billRepository.dashboardExportAndCancel(yearValue, 4, DeliveryName.CANCEL, storageId);
        Integer xt5 = billRepository.dashboardExportAndCancel(yearValue, 5, DeliveryName.CANCEL, storageId);
        Integer xt6 = billRepository.dashboardExportAndCancel(yearValue, 6, DeliveryName.CANCEL, storageId);
        Integer xt7 = billRepository.dashboardExportAndCancel(yearValue, 7, DeliveryName.CANCEL, storageId);
        Integer xt8 = billRepository.dashboardExportAndCancel(yearValue, 8, DeliveryName.CANCEL, storageId);
        Integer xt9 = billRepository.dashboardExportAndCancel(yearValue, 9, DeliveryName.CANCEL, storageId);
        Integer xt10 = billRepository.dashboardExportAndCancel(yearValue, 10, DeliveryName.CANCEL, storageId);
        Integer xt11 = billRepository.dashboardExportAndCancel(yearValue, 11, DeliveryName.CANCEL, storageId);
        Integer xt12 = billRepository.dashboardExportAndCancel(yearValue, 12, DeliveryName.CANCEL, storageId);
        if (xt1 == null) {
            xt1 = 0;
        }
        if (xt2 == null) {
            xt2 = 0;
        }
        if (xt3 == null) {
            xt3 = 0;
        }
        if (xt4 == null) {
            xt4 = 0;
        }
        if (xt5 == null) {
            xt5 = 0;
        }
        if (xt6 == null) {
            xt6 = 0;
        }
        if (xt7 == null) {
            xt7 = 0;
        }
        if (xt8 == null) {
            xt8 = 0;
        }
        if (xt9 == null) {
            xt9 = 0;
        }
        if (xt10 == null) {
            xt10 = 0;
        }
        if (xt11 == null) {
            xt11 = 0;
        }
        if (xt12 == null) {
            xt12 = 0;
        }
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 1", t1, xt1));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 2", t2, xt2));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 3", t3, xt3));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 4", t4, xt4));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 5", t5, xt5));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 6", t6, xt6));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 7", t7, xt7));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 8", t8, xt8));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 9", t9, xt9));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 10", t10, xt10));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 11", t11, xt11));
        dashboardResponseList.add(new DashboardImportAndCancelResponse("Tháng 12", t12, xt12));

        return dashboardResponseList;
    }
    //tạo data biểu đồ hình tròn theo mẫu data
//    data = {
//        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
//        datasets: [
//        {
//
//                    data: [12, 19, 3, 5, 2, 3],
//            backgroundColor: [
//            'rgba(255, 99, 132, 0.2)',
//                    'rgba(54, 162, 235, 0.2)',
//                    'rgba(255, 206, 86, 0.2)',
//                    'rgba(75, 192, 192, 0.2)',
//                    'rgba(153, 102, 255, 0.2)',
//                    'rgba(255, 159, 64, 0.2)',
//      ],
//            borderColor: [
//            'rgba(255, 99, 132, 1)',
//                    'rgba(54, 162, 235, 1)',
//                    'rgba(255, 206, 86, 1)',
//                    'rgba(75, 192, 192, 1)',
//                    'rgba(153, 102, 255, 1)',
//                    'rgba(255, 159, 64, 1)',
//      ],
//            borderWidth: 1,
//        },
//  ],
//    };
    @Override
    public TransformedData dasboardTransformedData() {
        // Dữ liệu đầu vào tạo ra list categoryItems
        List<CategoryItem> categoryItems=new ArrayList<>();
        List<Category> categories=categoryRepository.findAll();
        for (Category c:categories
             ) {
            CategoryItem categoryItem=new CategoryItem();
            categoryItem.setCategoryName(c.getCategoryName());
            categoryItem.setId(c.getId());
           Integer totalProduct= productRepository.totalProductByCategory(c);
           if(totalProduct==null){
               totalProduct=0;
           }
            categoryItem.setTotalProduct(totalProduct);
            categoryItems.add(categoryItem);
        }
        //số lượng sản phẩm theo category
        //tạo list danh sách tên category
        List<String> listCategory=new ArrayList<>();
        //tạo 1 list tổng số sản phẩm theo category
        List<Integer> totalProduct=new ArrayList<>();
        for (CategoryItem categoryItem:categoryItems
             ) {
            listCategory.add(categoryItem.getCategoryName());
            totalProduct.add(categoryItem.getTotalProduct());
        }
        TransformedData transformedData=new TransformedData();
        //set danh sách category
        transformedData.setLabels(listCategory);
        //tạo dataItem
        DataItem dataItem=new DataItem();
        //data kí hiệu của List tổng sản phẩm theo category
        dataItem.setData(totalProduct);
        //tạo 1 list backgroundColor với alpha độ trong suất 0.2
      List<String> backgroundColor=generateRandomColors(categoryItems.size(),0.2f);
       //set vào dataItem.setBackgroundColor
        dataItem.setBackgroundColor(backgroundColor);
        //set borderWidth bằng 1
        dataItem.setBorderWidth(1L);
        //tạo 1 list borderColor với alpha độ trong suất 1 dựa trên backgroundColor(thay độ trong suất từ 0,2 thành 1)
        List<String> borderColor = generateBorderColors(backgroundColor);
       // Set danh sách borderColor vào dataItem
        dataItem.setBorderColor(borderColor);
        //tạo list Datasets gồm 1 mảng các dataItem
        List<DataItem> datasets=new ArrayList<>();
        datasets.add(dataItem);
        transformedData.setDatasets(datasets);
        return transformedData;

    }

    private List<String> generateRandomColors(int numColors,float alpha) {
        List<String> colors = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numColors; i++) {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            String randomColor = String.format("rgba(%d, %d, %d, %.1f)", red, green, blue, alpha);
            colors.add(randomColor);
        }

        return colors;
    }
    private List<String> generateBorderColors(List<String> backgroundColors) {
        List<String> borderColors = new ArrayList<>();

        // Duyệt qua danh sách màu nền (backgroundColor)
        for (String backgroundColor : backgroundColors) {
            // Tách thông tin màu nền từ chuỗi backgroundColor cắt lấy chuối số rgba không chứa ngoặc(), cắt thành mảng số dựa ", "
            String[] rgba = backgroundColor.substring(5, backgroundColor.length() - 1).split(", ");

            // Lấy giá trị red, green, blue từ các thành phần rgba
            int red = Integer.parseInt(rgba[0]);
            int green = Integer.parseInt(rgba[1]);
            int blue = Integer.parseInt(rgba[2]);

            // Tạo chuỗi borderColor với alpha độ trong suất 1
            String borderColor = String.format("rgba(%d, %d, %d, %.1f)", red, green, blue, 1.0);

            // Thêm borderColor vào danh sách borderColors
            borderColors.add(borderColor);
        }

        // Trả về danh sách borderColors đã tạo
        return borderColors;
    }



}