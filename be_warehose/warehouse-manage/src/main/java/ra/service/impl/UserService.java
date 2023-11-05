package ra.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.dto.request.FormSignUpDto;
import ra.dto.request.StorageRequest;
import ra.dto.response.PasswordResponse;
import ra.dto.response.StorageResponse;
import ra.dto.response.UserResponse;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.UserMapper;
import ra.model.*;
import ra.repository.IStorageRepository;
import ra.repository.IUserRepository;
import ra.repository.IZoneRepository;
import ra.security.user_principal.UserPrinciple;
import ra.service.IRoleService;
import ra.service.IUserService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StorageService storageService;
    @Autowired
    private IZoneRepository zoneRepository;
    @Autowired
    private IStorageRepository storageRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Optional<Users> findByUserName(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    //nếu tạo tài khoản admin thì ko tạo kho, nếu tài khoản kho hoặc siêu thi nhớ tạo thêm kho hoặc siêu thị
    public UserResponse save(FormSignUpDto form) throws MyCustomRuntimeException {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new MyCustomRuntimeException("use đã tồn tại");
        }
        if (userRepository.existsByPhone(form.getPhone())) {
            throw new MyCustomRuntimeException("use đã tồn tại");
        }
        Roles roles;
        try {
            roles = roleService.findByRoleName(RoleName.valueOf(form.getRoles()));
            // Xử lý roles ở đây nếu không có lỗi
        } catch (IllegalArgumentException e) {
            throw new MyCustomRuntimeException("role name không tồn tại");
        }
        Zone zone = zoneRepository.findById(form.getZoneId()).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy zone"));

        Users users = Users.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .address(form.getAddress())
                .sex(form.isSex())
                .dateOfBirth(form.getDateOfBirth())
                .phone(form.getPhone())
                .roles(roles)
                .status(true)
                .build();
        users = userRepository.save(users);
        if(users.getRoles().getRoleName().equals(RoleName.ROLE_ADMIN)){
            return userMapper.toResponse(users) ;
        }
        List<String> list = new ArrayList<>();
        list.add("ROLE_ADMIN");
        StorageRequest storageRequest = new StorageRequest();
        String text = "";
        if (users.getRoles().getRoleName().equals((RoleName.ROLE_SUPERMARKET))) {
            storageRequest.setTypeStorage("SUPERMARKET");
            text += "SMK-";
        } else if (users.getRoles().getRoleName().equals((RoleName.ROLE_MANAGER))) {
            storageRequest.setTypeStorage("STORAGE");
            text += "WH-";
        }
        String[] words = zone.getZoneName().split(" ");
        StringBuilder abbreviation = new StringBuilder();

        for (String word : words) {
                abbreviation.append(word.charAt(0));

        }
        text+=abbreviation;
        String result;
        while (true) {
            int randomNumber = (int) (Math.floor(Math.random() * 999999) + 111111);
            boolean check = storageRepository.existsByStorageName(text + randomNumber);
            if (!check) {
                result = text + randomNumber;
                break;
            }
        }
        storageRequest.setStorageName(result);
        storageRequest.setAddress(users.getAddress());
        storageRequest.setZoneId(form.getZoneId());
        storageRequest.setUserId(users.getId());
        StorageResponse storage= storageService.addStorage(storageRequest, list, users.getId());
     UserResponse userResponse=   userMapper.toResponse(users) ;
     userResponse.setStorageName(storage.getStorageName());
        return userResponse;
    }
    @Override
    //in danh sách nhân viên  kho hoặc siêu thị
    public byte[] exportToExcel(RoleName roleName) throws IOException, MyCustomRuntimeException {
        List<Users> users = userRepository.listUsers(roleName);
        // Tạo tệp Excel và ghi dữ liệu vào đó (sử dụng Apache POI)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách nhân viên");

        // Tạo tiêu đề cho các cột
        Row headerRow1 = sheet.createRow(0);
        // Tạo một CellStyle để định dạng văn bản và căn giữa
        CellStyle centerCellStyle = workbook.createCellStyle();
        centerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        centerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Tạo một FontStyle để định dạng font của cell
        Font font = workbook.createFont();
        font.setBold(true); // Đặt in đậm
        font.setFontHeightInPoints((short) 16); // Đặt kích thước font
        centerCellStyle.setFont(font);

        // Tạo cell "Danh sách nhân viên" và đặt style
        Cell headerCell = headerRow1.createCell(1);
        headerCell.setCellValue("Danh sách nhân viên");
        headerCell.setCellStyle(centerCellStyle);

        // Merge các ô từ B1 đến J1 để tạo một ô lớn cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));

        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Họ");
        headerRow.createCell(2).setCellValue("Tên");
        headerRow.createCell(3).setCellValue("email");
        headerRow.createCell(4).setCellValue("Số điện thoại");
        headerRow.createCell(5).setCellValue("Địa chỉ");
        headerRow.createCell(6).setCellValue("Giới tính");
        headerRow.createCell(7).setCellValue("Ngày sinh");
        headerRow.createCell(8).setCellValue("Trạng thái");

        // Đổ dữ liệu từ danh sách users vào các dòng
        int rowNum = 2;
        for (Users user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFirstName());
            row.createCell(2).setCellValue(user.getLastName());
            row.createCell(3).setCellValue(user.getEmail());
            row.createCell(4).setCellValue(user.getPhone());
            row.createCell(5).setCellValue(user.getAddress());
            row.createCell(6).setCellValue(user.isSex() ? "Nam" : "Nữ");
            row.createCell(7).setCellValue(user.getDateOfBirth().toString());
            row.createCell(8).setCellValue(user.isStatus()? "Hoạt động" : "khóa");
        }

        // Đặt độ rộng cột để thỏa mãn nội dung
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);

        // Chuyển workbook thành mảng byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] excelBytes = byteArrayOutputStream.toByteArray();
        return excelBytes;
    }


    @Override
    public Page<UserResponse> findAllByEmailContainingIgnoreCase(String email, Pageable pageable) {
        Page<Users> users = userRepository.findAllByEmailContainingIgnoreCase(email, pageable);
        List<UserResponse> newUsers = users.stream()
                .filter(users1 -> !users1.getRoles().getRoleName().equals(RoleName.ROLE_ADMIN))
                .map(item -> userMapper.toResponse(item))
                .collect(Collectors.toList());
        return new PageImpl<>(newUsers,pageable,newUsers.size());
    }

    @Override
    //thay dổi tran thái use
    public UserResponse changeStatus(Long userId) {

        Users users = userRepository.findById(userId).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy use"));
        users.setStatus(!users.isStatus());
        users = userRepository.save(users);
        return userMapper.toResponse(users);
    }

    @Override
    //cập nhật thông tin user
    public UserResponse updateUser(FormSignUpDto userUpdate, Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new MyCustomRuntimeException("không tim thấy use"));
        if (userUpdate.getAddress() != null) {
            users.setAddress(userUpdate.getAddress());
        }
        if (userUpdate.getDateOfBirth() != null) {
            users.setDateOfBirth(userUpdate.getDateOfBirth());
        }
        if (userUpdate.getFirstName() != null) {
            users.setFirstName(userUpdate.getFirstName());
        }
        if (userUpdate.getLastName() != null) {
            users.setLastName(userUpdate.getLastName());
        }
        if (userUpdate.getPhone() != null) {
            users.setPhone(userUpdate.getPhone());
        }
        users.setSex(userUpdate.isSex());
        users = userRepository.save(users);
        return userMapper.toResponse(users);
    }

    @Override
    //lấy lại mật khẩu, cấp lại mật khẩu tạm thời cho use
    public PasswordResponse updatePasswordUser(Long userId) {

        Users users = userRepository.findById(userId).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại use này"));
        int newPassword = (int) (Math.floor(Math.random() * 999999) + 111111);
        users.setPassword((passwordEncoder.encode(String.valueOf(newPassword))));
        userRepository.save(users);
        return PasswordResponse.builder().passwordNew(String.valueOf(newPassword)).build();
    }
    //đổi mật khẩu use
    @Override
    public UserResponse editPasswordUser(Authentication authentication, String passwordUser, String passwordUserEdit) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Users users = userRepository.findById(userPrinciple.getId()).orElseThrow(() -> new MyCustomRuntimeException("Không tồn tại user này"));
        if (passwordEncoder.matches(passwordUser, users.getPassword())) {
            // So sánh mật khẩu đã mã hóa với mật khẩu đã mã hóa trong cơ sở dữ liệu
            users.setPassword(passwordEncoder.encode(passwordUserEdit)); // Mã hóa và lưu mật khẩu mới
        } else {
            throw new MyCustomRuntimeException("Mật khẩu cũ không khớp");
        }

        userRepository.save(users);
        return userMapper.toResponse(users);
    }

}