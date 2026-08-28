package com.attendance.app.controller;

import com.attendance.app.model.Attendance;
import com.attendance.app.model.AttendanceStatus;
import com.attendance.app.model.Role;
import com.attendance.app.service.AttendanceService;
import com.attendance.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/attendance")
public class AdminAttendanceController {

    private final AttendanceService attendanceService;
    private final UserService userService;

    public AdminAttendanceController(AttendanceService attendanceService, UserService userService) {
        this.attendanceService = attendanceService;
        this.userService = userService;
    }

    @GetMapping
    public String listAttendance(Model model) {
        model.addAttribute("records", attendanceService.findAll());
        return "admin/attendance";
    }

    @GetMapping("/new")
    public String newAttendanceForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        model.addAttribute("users", userService.findAll().stream()
                .filter(u -> u.getRole() != Role.ADMIN).toList());
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("today", LocalDate.now());
        return "admin/attendance-form";
    }

    @PostMapping
    public String markAttendance(@RequestParam Long userId,
                                  @RequestParam String date,
                                  @RequestParam AttendanceStatus status,
                                  @RequestParam(required = false) String remarks,
                                  RedirectAttributes redirectAttributes) {
        Attendance attendance = new Attendance(userService.findById(userId), LocalDate.parse(date), status, remarks);
        attendanceService.markAttendance(attendance);
        redirectAttributes.addFlashAttribute("success", "Attendance recorded successfully.");
        return "redirect:/admin/attendance";
    }

    @GetMapping("/{id}/edit")
    public String editAttendanceForm(@PathVariable Long id, Model model) {
        model.addAttribute("record", attendanceService.findById(id));
        model.addAttribute("statuses", AttendanceStatus.values());
        return "admin/attendance-edit";
    }

    @PostMapping("/{id}")
    public String updateAttendance(@PathVariable Long id,
                                    @RequestParam String date,
                                    @RequestParam AttendanceStatus status,
                                    @RequestParam(required = false) String remarks,
                                    RedirectAttributes redirectAttributes) {
        Attendance updated = new Attendance();
        updated.setDate(LocalDate.parse(date));
        updated.setStatus(status);
        updated.setRemarks(remarks);
        attendanceService.updateAttendance(id, updated);
        redirectAttributes.addFlashAttribute("success", "Attendance updated successfully.");
        return "redirect:/admin/attendance";
    }

    @PostMapping("/{id}/delete")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        attendanceService.deleteAttendance(id);
        redirectAttributes.addFlashAttribute("success", "Attendance record deleted.");
        return "redirect:/admin/attendance";
    }
}
