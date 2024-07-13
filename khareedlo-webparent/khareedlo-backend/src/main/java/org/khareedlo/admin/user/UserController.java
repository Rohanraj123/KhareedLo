package org.khareedlo.admin.user;

import org.khareedlo.admin.FileUploadUtil;
import org.khareedlo.common.entity.Role;
import org.khareedlo.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model) {
         List<User> listUsers = userService.listAll();
         model.addAttribute("listUsers", listUsers);
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
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
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
