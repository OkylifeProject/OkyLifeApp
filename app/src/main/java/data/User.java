package data;

/**
 * Created by mordreth on 10/14/15.
 */
public class User {
    private String firstName;
    private String sex;
    private String imageBytes;
    private String email;
    private String registerType;
    private String age;
    private String id;
    private String weight;
    private String height;

    public User(String firstName, String id, String age, String registerType, String email, String imageBytes, String sex, String weight, String height) {
        this.firstName = firstName;
        this.id = id;
        this.age = age;
        this.registerType = registerType;
        this.email = email;
        this.imageBytes = imageBytes;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
    }

    public User(String firstName, String email, String registerType, String id) {
        this.firstName = firstName;
        this.email = email;
        this.registerType = registerType;
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", sex='" + sex + '\'' +
                ", imageBytes='" + imageBytes + '\'' +
                ", email='" + email + '\'' +
                ", registerType='" + registerType + '\'' +
                ", age='" + age + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
