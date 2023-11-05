package ra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.exception.MyCustomRuntimeException;
import ra.model.RoleName;
import ra.model.Roles;
import ra.repository.IRoleRepository;
import ra.service.IRoleService;
@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Roles findByRoleName(RoleName roleName) throws MyCustomRuntimeException {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy role"));
    }
}
