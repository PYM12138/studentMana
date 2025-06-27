package student_mana.Servlet;

import student_mana.Dao.StudentDAO;
import student_mana.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet({"/students", "/addStudent", "/editStudent", "/deleteStudent"})
public class StudentServlet extends HttpServlet {

    @Autowired
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        // 启用 Spring 依赖注入
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getServletPath();

        if ("/deleteStudent".equals(path)) {
            handleDeleteStudent(request, response);
        } else {
            try {
                List<Student> students = studentDAO.getAllStudents();
                for (Student student : students) {
                    double totalCredit = studentDAO.getCompletedCredits(student.getStudentId());
                    student.setTotalCredit(totalCredit);
                }
                request.setAttribute("studentList", students);
                request.setAttribute("classList", studentDAO.getAllClasses());
                request.getRequestDispatcher("/studentList.jsp").forward(request, response);
            } catch (SQLException e) {
                request.setAttribute("error", "加载学生列表失败：" + e.getMessage());
                request.getRequestDispatcher("/studentList.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String path = request.getServletPath();
        if ("/addStudent".equals(path)) {
            handleAddStudent(request, response);
        } else if ("/editStudent".equals(path)) {
            handleEditStudent(request, response);
        }
    }

    private void handleAddStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Student student = new Student();
            student.setStudentId(Integer.parseInt(request.getParameter("studentId")));
            student.setName(request.getParameter("name"));
            student.setGender(request.getParameter("gender"));
            student.setAge(Integer.parseInt(request.getParameter("age")));
            student.setClassId(Integer.parseInt(request.getParameter("classId")));

            studentDAO.addStudent(student);
            response.sendRedirect(request.getContextPath() + "/students");

        } catch (SQLException e) {
            request.setAttribute("error", "添加学生失败：" + e.getMessage());
            request.setAttribute("studentList", studentDAO.getAllStudents());
            request.setAttribute("classList", studentDAO.getAllClasses());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "输入格式错误，请检查学号、年龄或班级 ID");
            request.setAttribute("studentList", studentDAO.getAllStudents());
            request.setAttribute("classList", studentDAO.getAllClasses());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        }
    }

    private void handleEditStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int originalStudentId = Integer.parseInt(request.getParameter("originalStudentId"));
            Student student = new Student();
            student.setStudentId(Integer.parseInt(request.getParameter("studentId")));
            student.setName(request.getParameter("name"));
            student.setGender(request.getParameter("gender"));
            student.setAge(Integer.parseInt(request.getParameter("age")));
            student.setClassId(Integer.parseInt(request.getParameter("classId")));

            studentDAO.updateStudent(originalStudentId, student);
            response.sendRedirect(request.getContextPath() + "/students");
        } catch (SQLException e) {
            request.setAttribute("error", "编辑学生失败：" + e.getMessage());
            request.setAttribute("studentList", studentDAO.getAllStudents());
            request.setAttribute("classList", studentDAO.getAllClasses());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            request.setAttribute("error", "输入格式错误，请检查学号、年龄或班级 ID");
            request.setAttribute("studentList", studentDAO.getAllStudents());
            request.setAttribute("classList", studentDAO.getAllClasses());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        }
    }

    private void handleDeleteStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            studentDAO.deleteStudent(studentId);
            response.sendRedirect(request.getContextPath() + "/students");
        } catch (SQLException e) {
            request.setAttribute("error", "删除学生失败：" + e.getMessage());
            request.setAttribute("studentList", studentDAO.getAllStudents());
            request.setAttribute("classList", studentDAO.getAllClasses());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的学号格式");
            request.setAttribute("studentList", studentDAO.getAllStudents());
            request.setAttribute("classList", studentDAO.getAllClasses());
            request.getRequestDispatcher("/studentList.jsp").forward(request, response);
        }
    }
}