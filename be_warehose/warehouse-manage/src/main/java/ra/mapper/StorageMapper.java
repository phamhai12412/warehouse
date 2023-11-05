package ra.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.dto.request.StorageRequest;
import ra.dto.response.StorageResponse;
import ra.exception.MyCustomRuntimeException;
import ra.model.Storage;
import ra.model.TypeStorage;
import ra.repository.IProductRepository;

import java.util.stream.Collectors;

@Component
public class StorageMapper implements IGenericMapper<Storage, StorageRequest, StorageResponse> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IProductRepository productRepository;
    @Override
    public Storage toEntity(StorageRequest storageRequest) {
        if (storageRequest == null) {
            throw new MyCustomRuntimeException("không có dữ liệu");
        }
        TypeStorage typeStorage;
        try {
            typeStorage = TypeStorage.valueOf(storageRequest.getTypeStorage());
        } catch (IllegalArgumentException e) {
            throw new MyCustomRuntimeException("TypeStorage không tồn tại");
        }

        return Storage.builder()
                .storageName(storageRequest.getStorageName())
                .address(storageRequest.getAddress())
                .typeStorage(typeStorage)
                .build();
    }

    @Override
    public StorageResponse toResponse(Storage storage) {
        if (storage == null) {
            throw new MyCustomRuntimeException("không có dữ liệu");
        }

        return StorageResponse.builder()
                .id(storage.getId())
                .storageName(storage.getStorageName())
                .created(storage.getCreated().toString())
                .address(storage.getAddress())
                .typeStorage(storage.getTypeStorage().toString())
                .zone(storage.getZone())
                .status(storage.getStatusName().toString())
                .users(userMapper.toResponse(storage.getUsers()))
                .productResponseList(productRepository.findAllByStorage_Id(storage.getId()).stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList()))
                .build();
    }
}
