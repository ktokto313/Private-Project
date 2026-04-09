package model;
public class User {
    private Integer userID;
    private Role role;
    private String username;
    private String password;
    private Integer departmentID;
    public User() {
    }

    public User(Integer userID, Role role, String username, String password, Integer departmentID) {
        this.userID = userID;
        this.role = role;
        this.username = username;
        this.password = password;
        this.departmentID = departmentID;
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

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }
}
