package org.khareedlo.admin.user;

import org.hibernate.annotations.Parameter;
import org.khareedlo.admin.FileUploadUtil;
import org.khareedlo.common.entity.Role;
import org.khareedlo.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "firstName", "asc");
    }

    @GetMapping("/users/page/{pageNumber}")
    public String listByPage(
            @PathVariable(name = "pageNumber") int pageNumber,
            Model model,
            @RequestParam(name = "sortField", required = false, defaultValue = "firstName") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        Page<User> page = userService.listByPage(pageNumber, sortField, sortDir);
        List<User> listUsers = page.getContent();

        long startCount = (long) (pageNumber - 1) * UserService.USERS_PER_PAGE+1;
        long endCount = startCount + UserService.USERS_PER_PAGE-1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();

        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");

        return "users_form";
    }

    @PostMapping("/users/save")
    public String saveUser(
            User user,
            RedirectAttributes redirectAttributes,
            @RequestParam("image") MultipartFile multipartFile
            ) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }

        // userService.save(user);

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(
            @PathVariable(name = "id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            model.addAttribute("listRoles", listRoles);

            return "users_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user Id "
                    + id + " has been deleted successfully");
        } catch (UserNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }

        return "redirect:/users";
    }
}
