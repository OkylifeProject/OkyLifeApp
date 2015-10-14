package data;

/**
 * Created by mordreth on 10/14/15.
 */
public class User {
    private String firstName;
    private String lastName;
    private String sex;
    private String imageBytes;
    private String email;
    private String password;
    private String registerType;
    private String age;
    private String id;

    public User(String id, String age, String registerType, String password, String email, String imageBytes, String sex, String lastName, String firstName) {
        this.id = id;
        this.age = age;
        this.registerType = registerType;
        this.password = password;
        this.email = email;
        this.imageBytes = imageBytes;
        this.sex = sex;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
