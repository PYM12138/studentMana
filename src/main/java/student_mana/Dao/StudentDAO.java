package student_mana.Dao;

import student_mana.Model.*;
import org.springframework.stereotype.Repository;
import student_mana.Model.Class;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDAO {

    @Resource
    private DataSource dataSource;

    // 获取所有学生
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.*, c.class_name FROM student s JOIN class c ON s.class_id = c.class_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setName(rs.getString("name"));
                s.setGender(rs.getString("gender"));
                s.setAge(rs.getInt("age"));
                s.setClassId(rs.getInt("class_id"));
                s.setClassName(rs.getString("class_name"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 获取所有班级
    public List<Class> getAllClasses() {
        List<Class> list = new ArrayList<>();
        String sql = "SELECT class_id, class_name FROM class";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Class c = new Class();
                c.setClassId(rs.getInt("class_id"));
                c.setClassName(rs.getString("class_name"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 获取所有课程
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_id, course_name, credit FROM course";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseName(rs.getString("course_name"));
                c.setCredit(rs.getInt("credit"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 获取学生学分统计总和
    public double getCreditStats(int studentId) throws SQLException {
        String sql = "SELECT SUM(c.credit) AS totalCredit FROM enrollment e JOIN course c ON e.course_id = c.course_id WHERE e.student_id = ? AND e.grade >= 60";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("totalCredit");
                }
            }
        }
        return 0.0;
    }

    // 获取学生所有修读记录
    public List<Enrollment> getStudentCreditStats(int studentId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.grade, c.course_name, c.credit " +
                "FROM enrollment e JOIN course c ON e.course_id = c.course_id " +
                "WHERE e.student_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                    enrollment.setStudentId(rs.getInt("student_id"));
                    enrollment.setCourseId(rs.getInt("course_id"));
                    enrollment.setGrade(rs.getDouble("grade"));

                    Course course = new Course();
                    course.setCourseId(rs.getInt("course_id"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setCredit(rs.getInt("credit"));
                    enrollment.setCourse(course);

                    list.add(enrollment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 添加学生
    public void addStudent(Student student) throws SQLException {
        if (existsByStudentId(student.getStudentId())) {
            throw new SQLException("学号 " + student.getStudentId() + " 已存在");
        }
        if (!existsByClassId(student.getClassId())) {
            throw new SQLException("班级 ID " + student.getClassId() + " 不存在");
        }

        String sql = "INSERT INTO student (student_id, name, gender, age, class_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, student.getStudentId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getGender());
            stmt.setInt(4, student.getAge());
            stmt.setInt(5, student.getClassId());
            stmt.executeUpdate();
        }
    }

    // 更新学生
    public void updateStudent(int originalStudentId, Student student) throws SQLException {
        if (!existsByStudentId(originalStudentId)) {
            throw new SQLException("学生 ID " + originalStudentId + " 不存在");
        }
        if (!existsByClassId(student.getClassId())) {
            throw new SQLException("班级 ID " + student.getClassId() + " 不存在");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new SQLException("姓名不能为空");
        }
        if (student.getGender() == null || !student.getGender().matches("男|女")) {
            throw new SQLException("性别必须是 '男' 或 '女'");
        }
        if (student.getAge() < 1 || student.getAge() > 100) {
            throw new SQLException("年龄必须在 1-100 之间");
        }

        student.setStudentId(originalStudentId);

        String sql = "UPDATE student SET name = ?, gender = ?, age = ?, class_id = ? WHERE student_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getGender());
            stmt.setInt(3, student.getAge());
            stmt.setInt(4, student.getClassId());
            stmt.setInt(5, originalStudentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("更新学生失败，学生 ID " + originalStudentId + " 不存在");
            }
        }
    }

    // 删除学生
    public void deleteStudent(int studentId) throws SQLException {
        if (!existsByStudentId(studentId)) {
            throw new SQLException("学号 " + studentId + " 不存在");
        }

        String deleteEnrollmentSql = "DELETE FROM enrollment WHERE student_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteEnrollmentSql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        }

        String deleteAttendanceSql = "DELETE FROM attendance WHERE student_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteAttendanceSql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        }

        String deleteStudentSql = "DELETE FROM student WHERE student_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteStudentSql)) {
            stmt.setInt(1, studentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("删除学生失败，学号 " + studentId + " 未找到");
            }
        }
    }

    // 添加修读记录
    public void addEnrollment(int studentId, int courseId, double grade) throws SQLException {
        if (existsEnrollment(studentId, courseId)) {
            throw new SQLException("学生已修读该课程");
        }
        if (!existsByStudentId(studentId)) {
            throw new SQLException("学生 ID " + studentId + " 不存在");
        }
        if (!existsByCourseId(courseId)) {
            throw new SQLException("课程 ID " + courseId + " 不存在");
        }

        String sql = "INSERT INTO enrollment (student_id, course_id, grade) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.setDouble(3, grade);
            stmt.executeUpdate();
        }
    }

    // 更新修读记录
    public void updateEnrollment(int enrollmentId, double grade) throws SQLException {
        String sql = "UPDATE enrollment SET grade = ? WHERE enrollment_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, grade);
            stmt.setInt(2, enrollmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("更新成绩失败，记录 ID " + enrollmentId + " 不存在");
            }
        }
    }

    // 删除修读记录
    public void deleteEnrollment(int enrollmentId) throws SQLException {
        String sql = "DELETE FROM enrollment WHERE enrollment_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("删除修读记录失败，记录 ID " + enrollmentId + " 未找到");
            }
        }
    }

    // 获取考勤记录
    public List<Attendance> getAttendance(int studentId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT attendance_id, student_id, date, status FROM attendance WHERE student_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setAttendanceId(rs.getInt("attendance_id"));
                    attendance.setStudentId(rs.getInt("student_id"));
                    attendance.setDate(rs.getDate("date"));
                    attendance.setStatus(rs.getString("status"));
                    list.add(attendance);
                }
            }
        }
        return list;
    }

    // 添加考勤记录
    public void addAttendance(int studentId, String date, String status) throws SQLException {
        if (!existsByStudentId(studentId)) {
            throw new SQLException("学生 ID " + studentId + " 不存在");
        }

        String sql = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setString(3, status);
            stmt.executeUpdate();
        }
    }

    // 更新考勤记录
    public void updateAttendance(int attendanceId, String status) throws SQLException {
        String sql = "UPDATE attendance SET status = ? WHERE attendance_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, attendanceId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("更新考勤失败，记录 ID " + attendanceId + " 不存在");
            }
        }
    }

    // 删除考勤记录
    public void deleteAttendance(int attendanceId) throws SQLException {
        String sql = "DELETE FROM attendance WHERE attendance_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendanceId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("删除考勤失败，记录 ID " + attendanceId + " 不存在");
            }
        }
    }

    // 检查学号是否存在
    private boolean existsByStudentId(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE student_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // 检查班级 ID 是否存在
    private boolean existsByClassId(int classId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM class WHERE class_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // 检查课程 ID 是否存在
    private boolean existsByCourseId(int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM course WHERE course_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // 检查是否重复选课
    private boolean existsEnrollment(int studentId, int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // 获取学生已完成学分
    public double getCompletedCredits(int studentId) throws SQLException {
        String sql = "SELECT SUM(c.credit) AS completedCredits FROM enrollment e JOIN course c ON e.course_id = c.course_id WHERE e.student_id = ? AND e.grade >= 60";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("completedCredits");
                }
            }
        }
        return 0.0;
    }

    // 获取班级平均分
    public double getClassAverageScore(int classId) throws SQLException {
        String sql = "SELECT AVG(e.grade) AS averageScore FROM enrollment e JOIN student s ON e.student_id = s.student_id WHERE s.class_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("averageScore");
                }
            }
        }
        return 0.0;
    }

    // 获取班级考勤率
    public double getClassAttendanceRate(int classId) throws SQLException {
        String sql = "SELECT (COUNT(CASE WHEN a.status = '签到' THEN 1 END) * 100.0 / COUNT(*)) AS attendanceRate FROM attendance a JOIN student s ON a.student_id = s.student_id WHERE s.class_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("attendanceRate");
                }
            }
        }
        return 0.0;
    }
    // 获取所有活动
    public List<Activity> getAllActivities() {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM activity";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getInt("activity_id"));
                activity.setActivityName(rs.getString("activity_name"));
                activity.setActivityDate(rs.getDate("activity_date"));
                activity.setActivityDescription(rs.getString("activity_description"));
                list.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 记录学生参与活动
    public void recordActivity(int studentId, int activityId, String participationDate) throws SQLException {
        if (!existsByStudentId(studentId)) {
            throw new SQLException("学生 ID " + studentId + " 不存在");
        }
        if (!existsByActivityId(activityId)) {
            throw new SQLException("活动 ID " + activityId + " 不存在");
        }
        String sql = "INSERT INTO activity_record (student_id, activity_id, participation_date) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, activityId);
            stmt.setDate(3, java.sql.Date.valueOf(participationDate));
            stmt.executeUpdate();
        }
    }

    // 获取学生活动记录
    public List<ActivityRecord> getStudentActivityRecords(int studentId) {
        List<ActivityRecord> list = new ArrayList<>();
        String sql = "SELECT ar.record_id, ar.student_id, ar.activity_id, ar.participation_date, a.activity_name " +
                "FROM activity_record ar JOIN activity a ON ar.activity_id = a.activity_id " +
                "WHERE ar.student_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ActivityRecord record = new ActivityRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setStudentId(rs.getInt("student_id"));
                    record.setActivityId(rs.getInt("activity_id"));
                    record.setParticipationDate(rs.getDate("participation_date"));
                    record.setActivityName(rs.getString("activity_name"));
                    list.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*// 检查学生是否存在
    private boolean existsByStudentId(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE student_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }*/

    // 检查活动是否存在
    private boolean existsByActivityId(int activityId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM activity WHERE activity_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activityId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}