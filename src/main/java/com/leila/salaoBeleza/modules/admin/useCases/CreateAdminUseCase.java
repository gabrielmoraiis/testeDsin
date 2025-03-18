package com.leila.salaoBeleza.modules.admin.useCases;

import com.leila.salaoBeleza.exceptions.AdminAlreadyExistsException;
import com.leila.salaoBeleza.modules.admin.entities.AdminEntity;
import com.leila.salaoBeleza.modules.admin.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateAdminUseCase {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminEntity execute(AdminEntity admin) {
        if (adminRepository.count() > 0) {
            throw new AdminAlreadyExistsException();
        }
        var password = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(password);

        return this.adminRepository.save(admin);
    }
}
