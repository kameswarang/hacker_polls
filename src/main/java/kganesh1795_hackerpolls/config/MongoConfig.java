package kganesh1795_hackerpolls.config;

import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
	private String uri;
	private String db;
	private String profile;

	@Override
	protected String getDatabaseName() {
		if (this.uri == null) {
			this.uri = System.getenv("MONGODB_URI");
			if (this.uri == null) {
				this.uri = "mongodb://localhost/hackerpolls";
			}
			this.db = this.uri.substring(uri.lastIndexOf("/") + 1);
		}
		return this.db;
	}

	@Override
	public MongoClient mongoClient() {
		this.getDatabaseName();
		this.profile = System.getenv("spring_profiles_active");
		
		MongoClient mc = MongoClients.create(this.uri);

		//For non-prod envs drop "candidate" collection
		if(profile == null || profile.equals("prod") == false) {
		    MongoCollection<Document> candidates = mc.getDatabase(this.getDatabaseName()).getCollection("candidate");
		    if(candidates != null) {
		    	//candidates.drop();
		    }
		}
		
		return mc;
	}
}