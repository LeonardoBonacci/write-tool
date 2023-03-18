package guru.bonacci.write.tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@EnableElasticsearchRepositories
public class WriteToolApp implements CommandLineRunner {

	static final String SAVENOW = "---";

	public static void main(String[] args) {
		SpringApplication.run(WriteToolApp.class, args);
	}
	
	
	static final String SEARCH_ON = "some";
	
	final QuoteRepository repo;
	
  @Override
  public void run(String... args) throws IOException, ParseException {
  	var pathOpt = Arrays.stream(args)
  		.filter(arg -> arg.startsWith("path="))
  		.map(arg -> arg.replace("path=", ""))
  		.findFirst();

  	var path =  Paths.get(pathOpt.orElseThrow());

  	System.out.println("--------------------------");

  	repo.deleteAll();

    var newQuote = new Quote();
    for (String line : Files.readAllLines(path)) {
    	if (SAVENOW.equals(line)) {
    		System.out.print("insert > " + newQuote.toString() + "\n");
    		repo.save(newQuote);
        newQuote = new Quote();
        continue;
    	}	

    	newQuote = buildQuote(newQuote, line);
    }	
  	
    
  	System.out.println("--------------------------");
    System.out.println("searching on " + SEARCH_ON);
  	System.out.println("--------------------------");
  	repo.findByText(SEARCH_ON).forEach(System.out::println);
  	System.out.println("--------------------------");
  	repo.findByText(SEARCH_ON).map(Quote::getText).forEach(System.out::println);
  	
  	System.out.println("--------DATE-----------");
  	var dateFormat = new SimpleDateFormat("dd-MM-yyyy");
  	repo.findAllByWhenBetween(dateFormat.parse("20-03-2023"), dateFormat.parse("01-01-2030")).forEach(System.out::println);

  }
  
  Quote buildQuote(Quote quote, String line) throws ParseException {
  	if (line.startsWith("text:")) {
  		quote.setText(line.replace("text:", "").trim());
    } else if (line.startsWith("author:")) {
  		quote.setAuthor(line.replace("author:", "").trim());
    } else if (line.startsWith("book:")) {
  		quote.setBook(line.replace("book:", "").trim());
    } else if (line.startsWith("chapter:")) {
  		quote.setChapter(line.replace("chapter:", "").trim());
    } else if (line.startsWith("when:")) {
    	quote.setWhen(new SimpleDateFormat("dd-MM-yyyy").parse(line.replace("when:", "").trim()));
    }

  	return quote;
  }
}
