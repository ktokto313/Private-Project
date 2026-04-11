package lkt.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {
    private Integer userID;
    private Role role;
    private String username;
    private String password;
    private Department department;

    public User() {
    }

    public User(Integer userID, Role role, String username, String password, Department department) {
        this.userID = userID;
        this.role = role;
        this.username = username;
        this.password = password;
        this.department = department;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getDepartmentID() {
        return department == null ? null : department.getID();
    }

    public void setDepartmentID(Integer departmentID) {
        if (departmentID == null) {
            this.department = null;
            return;
        }
        Department departmentRef = new Department();
        departmentRef.setID(departmentID);
        this.department = departmentRef;
    }

    public User getUserNoPassword() {
        return new User(userID, role, username, null, department);
    }
}
