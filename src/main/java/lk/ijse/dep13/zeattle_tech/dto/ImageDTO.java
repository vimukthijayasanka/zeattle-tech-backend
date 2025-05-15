package lk.ijse.dep13.zeattle_tech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO implements Serializable {
    private Long imageId;
    private String imageName;
    private String imageUrl;
}
