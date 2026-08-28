package com.attendance.app.controller;

import com.attendance.app.model.Attendance;
import com.attendance.app.model.AttendanceStatus;
import com.attendance.app.model.User;
import com.attendance.app.service.AttendanceService;
import com.attendance.app.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserAttendanceController {

    private final AttendanceService attendanceService;
    private final UserService userService;

    public UserAttendanceController(AttendanceService attendanceService, UserService userService) {
        this.attendanceService = attendanceService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Attendance> records = attendanceService.findByUser(currentUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("recentRecords", records.stream().limit(5).toList());
        model.addAttribute("totalPresent", attendanceService.countByStatus(records, AttendanceStatus.PRESENT));
        model.addAttribute("totalAbsent", attendanceService.countByStatus(records, AttendanceStatus.ABSENT));
        model.addAttribute("totalLeave", attendanceService.countByStatus(records, AttendanceStatus.LEAVE));
        model.addAttribute("totalRecords", records.size());
        return "user/dashboard";
    }

    @GetMapping("/attendance")
    public String attendanceHistory(Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("records", attendanceService.findByUser(currentUser));
        return "user/attendance";
    }
}
