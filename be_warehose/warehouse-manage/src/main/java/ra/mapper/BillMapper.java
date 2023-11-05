package ra.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.dto.response.BillResponse;
import ra.model.Bill;
import ra.model.BillDetail;
import ra.repository.IBillDetailRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillMapper {
    @Autowired
    private IBillDetailRepository billDetailRepository;
    @Autowired
    private  BillDetailMapper billDetailMapper;
    public BillResponse convert(Bill bill) {
     List <BillDetail> billDetails = billDetailRepository.findAllByBill_Id(bill.getId());
        return BillResponse.builder()
                .bill(bill)
                .delivery(String.valueOf(bill.getDelivery()))
                .userCreate(bill.getStart().getUsers())
                .start(bill.getStart())
                .end(bill.getEnd())
                .created(String.valueOf(bill.getCreated()))
                .billDetais(billDetails.stream().map(billDetail -> billDetailMapper.convertBillDetail(billDetail)).collect(Collectors.toList()))
                .build();
    }
}
