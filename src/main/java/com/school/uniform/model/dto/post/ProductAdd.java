package com.school.uniform.model.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAdd {
    String productName;
    String description;
    String freight;
    MultipartFile img;
    String tag;
    Long schoolId;
    MultipartFile file;
    Integer sex;
    String[] size;
    String[] price;
}
