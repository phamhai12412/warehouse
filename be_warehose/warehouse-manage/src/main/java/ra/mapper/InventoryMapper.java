package ra.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.dto.response.InventoryBasicResponse;
import ra.dto.response.InventoryDetailResponse;
import ra.dto.response.InventoryResponse;
import ra.model.Inventory;
import ra.model.InventoryDetail;
import ra.repository.IBillDetailRepository;
import ra.repository.IInventoryDetailRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryMapper {
    @Autowired
   private IInventoryDetailRepository inventoryDetailRepository;
    @Autowired
   private InventoryDetailMapper inventoryDetailMapper;
    public InventoryBasicResponse convertInventory(Inventory inventory) {
        List<InventoryDetail> inventoryDetailList = inventoryDetailRepository.findAllByInventory_Id(inventory.getId());

        return InventoryBasicResponse.builder()
                .id(inventory.getId())
                .storage(inventory.getStorage())
                .note(inventory.getNote())
                .created(inventory.getCreated().toString())
                .inventoryDetails(inventoryDetailList.stream().map(inventoryDetail -> inventoryDetailMapper.convertDetail(inventoryDetail)).collect(Collectors.toList()))
                .build();
    }
}
