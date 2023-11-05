package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.dto.response.DashboardExportImportResponse;
import ra.dto.response.DashboardImportAndCancelResponse;
import ra.dto.response.DashboardResponse;
import ra.dto.response.TransformedData;
import ra.service.IDashboardService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;
    //biểu đồ doanh thu theo kho
    @GetMapping("/GVCStorageId/{id}")
    public ResponseEntity< List<DashboardResponse> > GVCStorageId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "2023") Integer yearValue
    ) {
        return new ResponseEntity<>(dashboardService.findByCreatedGVCStorageId(id,yearValue), HttpStatus.OK) ;
    }
    //biểu đồ doanh thu admin
    @GetMapping("/admin/GVCAdmin")
    public ResponseEntity< List<DashboardResponse>> GVCAdmin(
            @RequestParam(defaultValue = "2023") Integer yearValue
    ) {
        return new ResponseEntity<>(dashboardService.findByCreatedGVCS(yearValue), HttpStatus.OK) ;
    }
    //tổng doanh thu hiện trang admin
    @GetMapping("/admin/GVCSAndByYear")
    public ResponseEntity<Double> GVCSAndByYear() {
        return new ResponseEntity<>( dashboardService.GVCSAndByYear(),HttpStatus.OK);
    }
    // tổng số bill giao thành công đến siêu thị admin
    @GetMapping("/admin/billByYear")
    public ResponseEntity<Integer> BillByYear() {
        return new ResponseEntity<>( dashboardService.billByYear(),HttpStatus.OK);
    }
    //tổng doanh thu hiện trang kho
    @GetMapping("/storage/GVCSAndByYear/{id}")
    public ResponseEntity<Double> GVCSByYearAndStorage(  @PathVariable Long id ) {
        return new ResponseEntity<>( dashboardService.GVCSByYearAndStorage(id),HttpStatus.OK);
    }
    // tổng số bill giao thành công đến siêu thị kho
    @GetMapping("/storage/billByYear/{id}")
    public ResponseEntity<Integer> billByYearAndStorage(  @PathVariable Long id) {
        return new ResponseEntity<>( dashboardService.billByYearAndStorage(id),HttpStatus.OK);
    }
    //tổng sản phẩm admin
    @GetMapping("/admin/totalProduct")
    public ResponseEntity< Integer> getTotalProductByStatus() {
        return new ResponseEntity<>(dashboardService.getTotalProductByStatus(),HttpStatus.OK) ;
    }
    //tổng sản phẩm theo kho
    @GetMapping("/storage/total/{storageId}")
    public ResponseEntity< Integer> getTotalProductByStatusAndStorage(@PathVariable Long storageId) {
        return new ResponseEntity<>( dashboardService.getTotalProductByStatusAndStorage( storageId),HttpStatus.OK);
    }
    //tổng nhân viên
    @GetMapping("/admin/totalUser")
    public ResponseEntity< Integer> getTotalUser() {
        return new ResponseEntity<>( dashboardService.getTotalUser(),HttpStatus.OK);
    }
    //dữ liệu biểu đồ xuất nhập theo dõi theo tháng và id kho
    @GetMapping("/dashboard/{storageId}/{month}")
    public List<DashboardExportImportResponse> dashboardExportImport(
            @PathVariable("storageId") Long storageId,
            @PathVariable("month") Integer month,
            @RequestParam(defaultValue = "2023") Integer yearValue

    ) {
        return dashboardService.dashboardExportImport(storageId, month,yearValue);
    }
    //dữ liệu biểu đồ so sánh nhập thành công và huy bỏ theo kho
    @GetMapping("/dashboard-import/{storageId}")
    public List<DashboardImportAndCancelResponse> importAndCancel(
            @PathVariable("storageId") Long storageId,
            @RequestParam(defaultValue = "2023") Integer yearValue

    ) {
        return dashboardService.importAndCancelStorageId(storageId,yearValue);
    }
    //dữ liệu biểu đồ so sánh xuất thành công và huy bỏ theo kho
    @GetMapping("/dashboard-export/{storageId}")
    public List<DashboardImportAndCancelResponse> exportAndCancel(
            @PathVariable("storageId") Long storageId,
            @RequestParam(defaultValue = "2023") Integer yearValue

    ) {
        return dashboardService.exportAndCancelStorageId(storageId,yearValue);
    }
    //biểu đồ hình tròn biểu thị tổng số lượng sản phẩm theo category
    @GetMapping("/transformed-data")
    public TransformedData getTransformedData() {
        return dashboardService.dasboardTransformedData();
    }

}
