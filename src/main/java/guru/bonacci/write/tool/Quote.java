package guru.bonacci.write.tool;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "quotes")
public class Quote {

	@Id
	@ToString.Exclude 
	String id;
	
	@Field(type = Text)
  String text;

	@Field(type = Text)
  String author;
    
	@Field(type = Text)
  String book;

	@Field(type = Text)
  String chapter;
}