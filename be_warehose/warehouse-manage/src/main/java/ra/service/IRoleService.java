package ra.service;

import ra.exception.LoginException;
import ra.model.RoleName;
import ra.model.Roles;

public interface IRoleService {
    Roles findByRoleName(RoleName roleName);
}
