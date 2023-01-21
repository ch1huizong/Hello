package Json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class CourseInfo {

    @JsonField
    public String name;

    @JsonField
    public String author;

    @JsonField
    public List<String> content;
}
