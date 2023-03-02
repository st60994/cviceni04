package cz.upce.fei.cviceni01.controller;

import cz.upce.fei.cviceni01.domain.AppUser;
import cz.upce.fei.cviceni01.domain.Role;
import cz.upce.fei.cviceni01.dto.AppUserDto;
import cz.upce.fei.cviceni01.dto.AppUserInputDto;
import cz.upce.fei.cviceni01.repository.AppUserRepository;
import cz.upce.fei.cviceni01.repository.RoleRepository;
import cz.upce.fei.cviceni01.service.AppUserService;
import cz.upce.fei.cviceni01.service.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/app-user")
public class AppUserController {
    private final AppUserService appUserService;
    private final RoleRepository roleRepository;

    @GetMapping
    public List<AppUser> findAll() {
        return appUserService.findAllByActiveEquals(true);
    }

    @GetMapping("/role")
    public List<AppUser> findAllByRolesContaining(Role role) {
        return appUserService.findAllByRolesContaining(role);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity getAppUserJson(@PathVariable final Long id) throws ResourceNotFoundException {
        var result = appUserService.findById(id);
        return ResponseEntity.ok(toDto(result));
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody @Validated AppUserInputDto appUserInputDto) {
        var result = appUserService.create(toEntity(appUserInputDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toDto(result));
    }

    // save je update i uložení
    // delete - set null nebo vymazat role pomocí sql dotazu
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable final Long id, @RequestBody AppUserInputDto appUserInputDto) throws ResourceNotFoundException {
        var result = appUserService.update(id, toEntity(appUserInputDto));
        return ResponseEntity.status(HttpStatus.OK).body(toDto(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Long id) throws ResourceNotFoundException {
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static AppUserDto toDto(final AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getActive(),
                appUser.getCreationDate(),
                appUser.getUpdateDate()
        );
    }

    private static AppUser toEntity(final AppUserInputDto appUserInputDto) {
        return new AppUser(
                appUserInputDto.getUsername(),
                appUserInputDto.getPassword(),
                appUserInputDto.getActive(),
                appUserInputDto.getCreationDate(),
                appUserInputDto.getCreationDate()
        );
    }

}
