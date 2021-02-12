package bot.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Alert {

    private String name;
    private String message;
    private User owner;
    
}
