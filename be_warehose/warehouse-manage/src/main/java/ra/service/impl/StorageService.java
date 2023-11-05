package ra.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ra.dto.request.StorageRequest;
import ra.dto.response.StorageResponse;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.ProductMapper;
import ra.mapper.StorageMapper;
import ra.model.*;
import ra.repository.IProductRepository;
import ra.repository.IStorageRepository;
import ra.repository.IUserRepository;
import ra.repository.IZoneRepository;
import org.springframework.data.domain.Pageable;
import ra.service.IStorageService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class


StorageService implements IStorageService {
    @Autowired
    private IStorageRepository storageRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IZoneRepository zoneRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private StorageMapper storageMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    //tạo kho mới chú ý
    //nêú là admin tạo thì kho hoặc đại lý sẽ có người quản lý là do admin chọn, manager thì mặc định manager là người quản lý và chờ xét duyệt
    public StorageResponse addStorage(StorageRequest storageRequest, List<String> list, Long iduse) throws MyCustomRuntimeException {
        if (storageRepository.findByStorageName(storageRequest.getStorageName()) != null) {
            throw new MyCustomRuntimeException("Đã tồn tại");
        }
        Zone zone = zoneRepository.findById(storageRequest.getZoneId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại khu vực này"));
        Users users = userRepository.findById(storageRequest.getUserId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại user này"));

        Storage storage = storageMapper.toEntity(storageRequest);
        storage.setZone(zone);
        storage.setCreated(new Date());
        //nêú là admin tạo thì kho hoặc đại lý sẽ có người quản lý là do admin chọn, manager thì mặc định manager là người quản lý và chờ xét duyệt
        if (list.get(0).equals(RoleName.ROLE_ADMIN.toString())) {
            storage.setStatusName(StatusName.ACCEPT);
            storage.setUsers(users);
        } else {
            Users users1 = userRepository.findById(iduse).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại user này"));
            storage.setStatusName(StatusName.PENDING);
            storage.setUsers(users1);
        }
        Storage savedStorage = storageRepository.save(storage);
      StorageResponse storageResponse=  storageMapper.toResponse(savedStorage);
      storageResponse.setProductResponseList(new ArrayList<>());
        return storageResponse;
    }

    @Override
    //tìm kiếm kho theo id
    public StorageResponse getStorage(Long id) throws MyCustomRuntimeException {
        Storage storage = storageRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy"));
        StorageResponse storageResponse =storageMapper.toResponse(storage);
        List<Product> productList=productRepository.findAllByStorage_Id(id);
        storageResponse.setProductResponseList(productList.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList()));

        return storageResponse;
    }

    @Override
    //tìm kiếm tất cả các kho có hỗ tro tìm kiếm theo tên kho
    public Page<StorageResponse> getStorages(String name, Pageable pageable) {
        Page<Storage> storages;
        if (name.isEmpty()) {
            storages = storageRepository.findAll(pageable);
        } else {
            storages = storageRepository.findAllByStorageNameContainingIgnoreCase(name, pageable);
        }

        return storages.map(storage -> {
            StorageResponse storageResponse = storageMapper.toResponse(storage);
            List<Product> productList = productRepository.findAllByStorage_Id(storage.getId());
            storageResponse.setProductResponseList(productList.stream().map(productMapper::toResponse).collect(Collectors.toList()));
            return storageResponse;
        });
    }

    @Override
    //cập nhật thông tin kho
    public StorageResponse editStorage(Long id, StorageRequest storageRequest) throws MyCustomRuntimeException {

        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy"));

        if (storageRequest.getStorageName() != null) {
            storage.setStorageName(storageRequest.getStorageName());
        }
        if (storageRequest.getAddress() != null) {
            storage.setAddress(storageRequest.getAddress());
        }
        if (storageRequest.getUserId() != null) {
            storage.setUsers(storage.getUsers());
        }
        if (storageRequest.getTypeStorage() != null) {
            try {
                TypeStorage typeStorage = TypeStorage.valueOf(storageRequest.getTypeStorage());
                storage.setTypeStorage(typeStorage);
            } catch (IllegalArgumentException e) {
                throw new MyCustomRuntimeException("typeStorage không đúng định dạng FE kiểm tra lại");
            }

        }
        Storage updatedStorage = storageRepository.save(storage);
        StorageResponse storageResponse =storageMapper.toResponse(updatedStorage);
        List<Product> productList=productRepository.findAllByStorage_Id(updatedStorage.getId());
        storageResponse.setProductResponseList(productList.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList()));

        return storageResponse;
    }

    @Override
    //thay đổi trạng thái kho
    public StorageResponse updateStorageStatus(Long id, String statusName) throws MyCustomRuntimeException {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new MyCustomRuntimeException("không tồn tại kho hoặc đại lý "));


        try {
            storage.setStatusName(StatusName.valueOf(statusName));
        } catch (IllegalArgumentException e) {
            throw new MyCustomRuntimeException("statusName không tồn tại");
        }
        storage = storageRepository.save(storage);
        StorageResponse storageResponse =storageMapper.toResponse(storage);
        List<Product> productList=productRepository.findAllByStorage_Id(storage.getId());
        storageResponse.setProductResponseList(productList.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList()));
        return storageResponse;
    }

    @Override
    //tìm tất cả các kho dựa theo trạng thái
    public Page<StorageResponse> findAllByStatusName(String statusName, Pageable pageable) {
        Page<Storage> storages;
        try {
            storages = storageRepository.findAllByStatusName(StatusName.valueOf(statusName), pageable);
        } catch (IllegalArgumentException e) {
            throw new MyCustomRuntimeException("StatusName không tồn tại");
        }

        return storages.map(storage -> {
            StorageResponse storageResponse = storageMapper.toResponse(storage);
            List<Product> productList = productRepository.findAllByStorage_Id(storage.getId());
            storageResponse.setProductResponseList(productList.stream().map(productMapper::toResponse).collect(Collectors.toList()));
            return storageResponse;
        });
    }

    @Override
    //chấp thuận kho
    public StorageResponse approveStorage(Long storageId) throws MyCustomRuntimeException {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new MyCustomRuntimeException("không tồn tại"));

        // Kiểm tra xem kho có ở trạng thái pending không
        if (!storage.getStatusName().equals(StatusName.PENDING)) {
            throw new MyCustomRuntimeException("không ở trạng thái pending");
        }

        storage.setStatusName(StatusName.ACCEPT);
        storage = storageRepository.save(storage);
        StorageResponse storageResponse =storageMapper.toResponse(storage);
        List<Product> productList=productRepository.findAllByStorage_Id(storage.getId());
        storageResponse.setProductResponseList(productList.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList()));
        return storageResponse;
    }

    @Override
    //không chấp thuận kho
    public StorageResponse rejectStorage(Long storageId) throws MyCustomRuntimeException {

        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new MyCustomRuntimeException("Kho không tồn tại"));

        // Kiểm tra xem kho có ở trạng thái pending không
        if (!storage.getStatusName().equals(StatusName.PENDING)) {
            throw new MyCustomRuntimeException("Kho không ở trạng thái pending");
        }

        storage.setStatusName(StatusName.DENIED);
        StorageResponse storageResponse =storageMapper.toResponse(storage);
        List<Product> productList=productRepository.findAllByStorage_Id(storage.getId());
        storageResponse.setProductResponseList(productList.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList()));
        storageRepository.delete(storage);
        return storageResponse;
    }

    //tìm kiếm kho theo useid
    @Override
    public StorageResponse findByUserId(Long userId) {
        Storage storage= storageRepository.findByUsers_Id(userId);
        return storageMapper.toResponse(storage) ;
    }
    @Override
    //chuyển trạng thái sang DELETE
    public StorageResponse deleteStorage(Long storageId) throws MyCustomRuntimeException {

        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new MyCustomRuntimeException("Kho không tồn tại"));
        List<Product> productList=productRepository.findAllByStorage_Id(storage.getId());
        if(!productList.isEmpty()){
            throw new MyCustomRuntimeException("kho vẫn còn sản phẩm hãy kiểm tra lại");
        }
        storage.setStatusName(StatusName.DELETE);
        storage= storageRepository.save(storage);
        StorageResponse storageResponse =storageMapper.toResponse(storage);

        storageResponse.setProductResponseList(productList.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList()));

        return storageResponse;
    }
    @Override
    //tìm tất cả các kho có hỗ trợ tìm kiếm theo tên kho và phân trang
    public List<StorageResponse> getStoragesList(String name) {
        List<Storage> storages;
        if (name.isEmpty()) {
            storages = storageRepository.findAll();
        } else {
            storages = storageRepository.findAllByStorageNameContainingIgnoreCase(name);
        }

        return storages.stream().map(storage -> {
            StorageResponse storageResponse = storageMapper.toResponse(storage);
            List<Product> productList = productRepository.findAllByStorage_Id(storage.getId());
            storageResponse.setProductResponseList(productList.stream().map(productMapper::toResponse).collect(Collectors.toList()));
            return storageResponse;
        }).collect(Collectors.toList());
    }
}
