package org.ksens.demo.java.springboot.clothstore.services.interfaces;

import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.RoleModel;
import org.ksens.demo.java.springboot.clothstore.models.UserModel;
import org.springframework.security.core.Authentication;

public interface IAuthService {
    ResponseModel getRoles();
    ResponseModel createRole(RoleModel roleModel);
    ResponseModel getRoleUsers(Long roleId);
    ResponseModel createUser(UserModel userModel);
    ResponseModel deleteUser(Long id);
    ResponseModel makeUserAdmin(Long id) throws Exception;
    ResponseModel check(Authentication authentication);
    ResponseModel onSignOut();
    ResponseModel onError();
}
