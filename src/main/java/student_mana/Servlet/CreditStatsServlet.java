package student_mana.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import student_mana.Dao.StudentDAO;
import student_mana.Model.Attendance;
import student_mana.Model.Enrollment;
import student_mana.Model.ActivityRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet({"/creditStats", "/addEnrollment", "/updateEnrollment", "/deleteEnrollment",
        "/exportCreditStats", "/exportEnrollments", "/attendance", "/addAttendance",
        "/updateAttendance", "/deleteAttendance", "/exportAttendance", "/activityRecords",
        "/addActivityRecord", "/classStats"})
public class CreditStatsServlet extends HttpServlet {

    @Autowired
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String path = request.getServletPath();
        switch (path) {
            case "/deleteEnrollment":
                handleDeleteEnrollment(request, response);
                break;
            case "/exportCreditStats":
                handleExportCreditStats(request, response);
                break;
            case "/exportEnrollments":
                handleExportEnrollments(request, response);
                break;
            case "/attendance":
                handleAttendance(request, response);
                break;
            case "/deleteAttendance":
                try {
                    handleDeleteAttendance(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "/exportAttendance":
                handleExportAttendance(request, response);
                break;
            case "/activityRecords":
                handleActivityRecords(request, response);
                break;
            case "/classStats":
                handleClassStats(request, response);
                break;
            default:
                handleCreditStats(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String path = request.getServletPath();
        switch (path) {
            case "/addEnrollment":
                handleAddEnrollment(request, response);
                break;
            case "/updateEnrollment":
                handleUpdateEnrollment(request, response);
                break;
            case "/addAttendance":
                try {
                    handleAddAttendance(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "/updateAttendance":
                try {
                    handleUpdateAttendance(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "/addActivityRecord":
                handleAddActivityRecord(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/students");
                break;
        }
    }

    private void handleCreditStats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        int studentId;

        try {
            studentId = Integer.parseInt(studentIdParam);
            if (studentId <= 0) {
                throw new NumberFormatException("学生 ID 必须为正整数");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的学生 ID");
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
            return;
        }

        try {
            List<Enrollment> enrollments = studentDAO.getStudentCreditStats(studentId);
            double totalCredit = studentDAO.getCreditStats(studentId);
            request.setAttribute("enrollmentList", enrollments);
            request.setAttribute("totalCredit", totalCredit);
            request.setAttribute("courseList", studentDAO.getAllCourses());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/creditStats.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "加载学分统计失败：" + e.getMessage());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        }
    }

    private void handleAddEnrollment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        String courseIdParam = request.getParameter("courseId");
        String gradeParam = request.getParameter("grade");
        int studentId = 0, courseId;
        double grade;

        try {
            studentId = Integer.parseInt(studentIdParam);
            courseId = Integer.parseInt(courseIdParam);
            grade = Double.parseDouble(gradeParam);
            if (grade < 0 || grade > 100) {
                throw new NumberFormatException("成绩必须在 0-100 之间");
            }

            studentDAO.addEnrollment(studentId, courseId, grade);
            response.sendRedirect(request.getContextPath() + "/creditStats?studentId=" + studentId);
        } catch (SQLException e) {
            request.setAttribute("error", "添加修读记录失败：" + e.getMessage());
            request.setAttribute("enrollmentList", studentDAO.getStudentCreditStats(studentId));
            request.setAttribute("courseList", studentDAO.getAllCourses());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/creditStats.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "输入格式错误：" + (e.getMessage() != null ? e.getMessage() : "请检查课程或成绩"));
            request.setAttribute("enrollmentList", studentDAO.getStudentCreditStats(studentId));
            request.setAttribute("courseList", studentDAO.getAllCourses());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/creditStats.jsp").forward(request, response);
        }
    }

    private void handleUpdateEnrollment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enrollmentIdParam = request.getParameter("enrollmentId");
        String gradeParam = request.getParameter("grade");
        String studentIdParam = request.getParameter("studentId");
        int enrollmentId, studentId = 0;
        double grade;

        try {
            enrollmentId = Integer.parseInt(enrollmentIdParam);
            studentId = Integer.parseInt(studentIdParam);
            grade = Double.parseDouble(gradeParam);
            if (grade < 0 || grade > 100) {
                throw new NumberFormatException("成绩必须在 0-100 之间");
            }

            studentDAO.updateEnrollment(enrollmentId, grade);
            response.sendRedirect(request.getContextPath() + "/creditStats?studentId=" + studentId);
        } catch (SQLException e) {
            request.setAttribute("error", "更新成绩失败：" + e.getMessage());
            request.setAttribute("enrollmentList", studentDAO.getStudentCreditStats(studentId));
            request.setAttribute("courseList", studentDAO.getAllCourses());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/creditStats.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "输入格式错误：" + (e.getMessage() != null ? e.getMessage() : "请检查成绩"));
            request.setAttribute("enrollmentList", studentDAO.getStudentCreditStats(studentId));
            request.setAttribute("courseList", studentDAO.getAllCourses());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/creditStats.jsp").forward(request, response);
        }
    }

    private void handleDeleteEnrollment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enrollmentIdParam = request.getParameter("enrollmentId");
        String studentIdParam = request.getParameter("studentId");
        int enrollmentId, studentId = 0;

        try {
            enrollmentId = Integer.parseInt(enrollmentIdParam);
            studentId = Integer.parseInt(studentIdParam);
            if (enrollmentId <= 0 || studentId <= 0) {
                throw new NumberFormatException("记录 ID 或学生 ID 必须为正整数");
            }

            studentDAO.deleteEnrollment(enrollmentId);
            response.sendRedirect(request.getContextPath() + "/creditStats?studentId=" + studentId);
        } catch (SQLException e) {
            request.setAttribute("error", "删除修读记录失败：" + e.getMessage());
            request.setAttribute("enrollmentList", studentDAO.getStudentCreditStats(studentId));
            request.setAttribute("courseList", studentDAO.getAllCourses());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/creditStats.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的记录 ID 或学生 ID");
            request.setAttribute("enrollmentList", studentDAO.getStudentCreditStats(studentId));
            request.setAttribute("courseList", studentDAO.getAllCourses());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/creditStats.jsp").forward(request, response);
        }
    }

    private void handleExportCreditStats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        int studentId;

        try {
            studentId = Integer.parseInt(studentIdParam);
            if (studentId <= 0) {
                throw new NumberFormatException("学生 ID 必须为正整数");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的学生 ID");
            return;
        }

        try {
            double totalCredit = studentDAO.getCreditStats(studentId);
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"credit_stats_" + studentId + ".csv\"");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.write("\uFEFF");
            writer.println("学生 ID,总学分");
            writer.println(studentId + "," + totalCredit);
            writer.flush();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出学分统计失败：" + e.getMessage());
        }
    }

    private void handleExportEnrollments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        int studentId;

        try {
            studentId = Integer.parseInt(studentIdParam);
            if (studentId <= 0) {
                throw new NumberFormatException("学生 ID 必须为正整数");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的学生 ID");
            return;
        }

        try {
            List<Enrollment> enrollments = studentDAO.getStudentCreditStats(studentId);
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"enrollments_" + studentId + ".csv\"");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.write("\uFEFF");
            writer.println("课程名称,学分,成绩");
            for (Enrollment enrollment : enrollments) {
                String courseName = "\"" + enrollment.getCourse().getCourseName().replace("\"", "\"\"") + "\"";
                writer.println(courseName + "," + enrollment.getCourse().getCredit() + "," + enrollment.getGrade());
            }
            writer.flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出成绩失败：" + e.getMessage());
        }
    }

    private void handleAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        int studentId;

        try {
            studentId = Integer.parseInt(studentIdParam);
            if (studentId <= 0) {
                throw new NumberFormatException("学生 ID 必须为正整数");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的学生 ID");
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
            return;
        }

        try {
            List<Attendance> attendances = studentDAO.getAttendance(studentId);
            request.setAttribute("attendanceList", attendances);
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "加载考勤记录失败：" + e.getMessage());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        }
    }

    private void handleAddAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String studentIdParam = request.getParameter("studentId");
        String dateParam = request.getParameter("date");
        String statusParam = request.getParameter("status");
        int studentId = 0;

        try {
            studentId = Integer.parseInt(studentIdParam);
            if (!dateParam.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalArgumentException("日期格式错误，必须为 YYYY-MM-DD");
            }
            if (!statusParam.matches("签到|请假|旷课")) {
                throw new IllegalArgumentException("状态必须是 '签到'、'请假' 或 '旷课'");
            }

            studentDAO.addAttendance(studentId, dateParam, statusParam);
            response.sendRedirect(request.getContextPath() + "/attendance?studentId=" + studentId);
        } catch (SQLException e) {
            request.setAttribute("error", "添加考勤记录失败：" + e.getMessage());
            request.setAttribute("attendanceList", studentDAO.getAttendance(studentId));
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "输入格式错误：" + e.getMessage());
            request.setAttribute("attendanceList", studentDAO.getAttendance(studentId));
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        }
    }

    private void handleUpdateAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String attendanceIdParam = request.getParameter("attendanceId");
        String statusParam = request.getParameter("status");
        String studentIdParam = request.getParameter("studentId");
        int attendanceId, studentId = 0;

        try {
            attendanceId = Integer.parseInt(attendanceIdParam);
            studentId = Integer.parseInt(studentIdParam);
            if (!statusParam.matches("签到|请假|旷课")) {
                throw new IllegalArgumentException("状态必须是 '签到'、'请假' 或 '旷课'");
            }

            studentDAO.updateAttendance(attendanceId, statusParam);
            response.sendRedirect(request.getContextPath() + "/attendance?studentId=" + studentId);
        } catch (SQLException e) {
            request.setAttribute("error", "更新考勤记录失败：" + e.getMessage());
            request.setAttribute("attendanceList", studentDAO.getAttendance(studentId));
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "输入格式错误：" + e.getMessage());
            request.setAttribute("attendanceList", studentDAO.getAttendance(studentId));
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        }
    }

    private void handleDeleteAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String attendanceIdParam = request.getParameter("attendanceId");
        String studentIdParam = request.getParameter("studentId");
        int attendanceId, studentId = 0;

        try {
            attendanceId = Integer.parseInt(attendanceIdParam);
            studentId = Integer.parseInt(studentIdParam);
            if (attendanceId <= 0 || studentId <= 0) {
                throw new NumberFormatException("记录 ID 或学生 ID 必须为正整数");
            }

            studentDAO.deleteAttendance(attendanceId);
            response.sendRedirect(request.getContextPath() + "/attendance?studentId=" + studentId);
        } catch (SQLException e) {
            request.setAttribute("error", "删除考勤记录失败：" + e.getMessage());
            request.setAttribute("attendanceList", studentDAO.getAttendance(studentId));
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的记录 ID 或学生 ID");
            request.setAttribute("attendanceList", studentDAO.getAttendance(studentId));
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/attendance.jsp").forward(request, response);
        }
    }

    private void handleExportAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        int studentId;

        try {
            studentId = Integer.parseInt(studentIdParam);
            if (studentId <= 0) {
                throw new NumberFormatException("学生 ID 必须为正整数");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的学生 ID");
            return;
        }

        try {
            List<Attendance> attendances = studentDAO.getAttendance(studentId);
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"attendance_" + studentId + ".csv\"");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.write("\uFEFF");
            writer.println("日期,状态");
            for (Attendance attendance : attendances) {
                String status = "\"" + attendance.getStatus().replace("\"", "\"\"") + "\"";
                writer.println(attendance.getDate() + "," + status);
            }
            writer.flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出考勤失败：" + e.getMessage());
        }
    }

    private void handleActivityRecords(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        int studentId;

        try {
            studentId = Integer.parseInt(studentIdParam);
            if (studentId <= 0) {
                throw new NumberFormatException("学生 ID 必须为正整数");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的学生 ID");
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
            return;
        }

        try {
            List<ActivityRecord> records = studentDAO.getStudentActivityRecords(studentId);
            request.setAttribute("activityRecords", records);
            request.setAttribute("activityList", studentDAO.getAllActivities());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/activityRecords.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "加载活动记录失败：" + e.getMessage());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        }
    }

    private void handleAddActivityRecord(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentIdParam = request.getParameter("studentId");
        String activityIdParam = request.getParameter("activityId");
        String participationDateParam = request.getParameter("participationDate");
        int studentId = 0, activityId;

        try {
            studentId = Integer.parseInt(studentIdParam);
            activityId = Integer.parseInt(activityIdParam);
            if (!participationDateParam.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalArgumentException("日期格式错误，必须为 YYYY-MM-DD");
            }

            studentDAO.recordActivity(studentId, activityId, participationDateParam);
            response.sendRedirect(request.getContextPath() + "/activityRecords?studentId=" + studentId);
        } catch (SQLException e) {
            request.setAttribute("error", "添加活动记录失败：" + e.getMessage());
            request.setAttribute("activityRecords", studentDAO.getStudentActivityRecords(studentId));
            request.setAttribute("activityList", studentDAO.getAllActivities());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/activityRecords.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "输入格式错误：" + e.getMessage());
            request.setAttribute("activityRecords", studentDAO.getStudentActivityRecords(studentId));
            request.setAttribute("activityList", studentDAO.getAllActivities());
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("/activityRecords.jsp").forward(request, response);
        }
    }

    private void handleClassStats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String classIdParam = request.getParameter("classId");
        int classId;

        try {
            classId = Integer.parseInt(classIdParam);
            if (classId <= 0) {
                throw new NumberFormatException("班级 ID 必须为正整数");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的班级 ID");
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
            return;
        }

        try {
            double averageScore = studentDAO.getClassAverageScore(classId);
            double attendanceRate = studentDAO.getClassAttendanceRate(classId);
            request.setAttribute("averageScore", String.format("%.2f", averageScore));
            request.setAttribute("attendanceRate", String.format("%.2f", attendanceRate));
            request.setAttribute("classId", classId);
            request.getRequestDispatcher("/classStats.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "加载班级统计失败：" + e.getMessage());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        }
    }
}