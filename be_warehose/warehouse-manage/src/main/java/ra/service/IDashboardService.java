package ra.service;

import ra.dto.response.*;

import java.util.List;

public interface IDashboardService {
    List<DashboardResponse> findByCreatedGVCStorageId(Long storageId,Integer yearValue);
    List<DashboardResponse> findByCreatedGVCS(Integer yearValue);
    Double GVCSAndByYear();
    Integer billByYear();
    Double GVCSByYearAndStorage(Long storageId);
    Integer billByYearAndStorage(Long storageId);
    Integer getTotalProductByStatus();
    Integer getTotalProductByStatusAndStorage( Long storageId);
    Integer getTotalUser();
    List<DashboardExportImportResponse> dashboardExportImport(Long storageId, Integer month,Integer yearValue);
    List<DashboardImportAndCancelResponse> importAndCancelStorageId(Long storageId,Integer yearValue);
    List<DashboardImportAndCancelResponse> exportAndCancelStorageId(Long storageId,Integer yearValue);
    TransformedData dasboardTransformedData();
}
