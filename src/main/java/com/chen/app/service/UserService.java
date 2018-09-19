package com.chen.app.service;

import com.chen.app.jpa.model.Role;
import com.chen.app.jpa.model.User;
import com.chen.app.jpa.repository.RoleRepository;
import com.chen.app.jpa.repository.UserRepository;
import com.chen.web.dto.UserDTO;
import com.chen.web.util.ServiceResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: ccong
 * Date: 18/8/26 下午12:04
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    public User findByName(String name) {
        User user = userRepository.findByName(name);

        if(user==null) {
            return user;
        }

        List<Role> roles = roleRepository.findRolesByUserId(user.getId());
        if(roles == null || roles.isEmpty()){
            throw new DisabledException("access denied");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        user.setAuthorityList(authorities);

        return user;
    }

    public ServiceResult<UserDTO> findById(Long id) {
        User user = userRepository.getOne(id);
        if (user == null) {
            return ServiceResult.notFound();
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ServiceResult.of(userDTO);
    }
}
