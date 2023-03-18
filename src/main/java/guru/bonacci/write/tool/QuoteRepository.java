package guru.bonacci.write.tool;

import java.util.stream.Stream;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends ElasticsearchRepository<Quote, String> {

	Stream<Quote> findByText(String text);
}