package br.com.testerang.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JpaUtil {

	private static final String PERSISTENCE_UNIT = "RANG";
	private static final DatabaseSettings DATABASE_SETTINGS = DatabaseSettings.load();
	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT,
			DATABASE_SETTINGS.toJpaProperties());

	private JpaUtil() {
	}

	public static EntityManager createEntityManager() {
		return ENTITY_MANAGER_FACTORY.createEntityManager();
	}

	public static void close() {
		if (ENTITY_MANAGER_FACTORY.isOpen()) {
			ENTITY_MANAGER_FACTORY.close();
		}
	}

	private static final class DatabaseSettings {

		private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
		private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/rangdatabase?useSSL=false&serverTimezone=UTC";
		private static final String DEFAULT_USER = "root";
		private static final String DEFAULT_PASSWORD = "";

		private final String driver;
		private final String url;
		private final String user;
		private final String password;
		private final String schemaStrategy;
		private final String showSql;

		private DatabaseSettings(
				String driver,
				String url,
				String user,
				String password,
				String schemaStrategy,
				String showSql) {
			this.driver = driver;
			this.url = url;
			this.user = user;
			this.password = password;
			this.schemaStrategy = schemaStrategy;
			this.showSql = showSql;
		}

		private static DatabaseSettings load() {
			return new DatabaseSettings(
					read("database.driver", "DB_DRIVER", DEFAULT_DRIVER),
					read("database.url", "DB_URL", DEFAULT_URL),
					read("database.user", "DB_USER", DEFAULT_USER),
					read("database.password", "DB_PASSWORD", DEFAULT_PASSWORD),
					read("database.schema", "DB_SCHEMA_STRATEGY", "update"),
					read("database.show_sql", "DB_SHOW_SQL", "false"));
		}

		private Map<String, Object> toJpaProperties() {
			Map<String, Object> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.driver", driver);
			properties.put("javax.persistence.jdbc.url", url);
			properties.put("javax.persistence.jdbc.user", user);
			properties.put("javax.persistence.jdbc.password", password);
			properties.put("hibernate.hbm2ddl.auto", schemaStrategy);
			properties.put("hibernate.show_sql", showSql);
			return properties;
		}

		private static String read(String propertyName, String environmentName, String defaultValue) {
			String value = System.getProperty(propertyName);
			if (isBlank(value)) {
				value = System.getenv(environmentName);
			}

			return isBlank(value) ? defaultValue : value.trim();
		}

		private static boolean isBlank(String value) {
			return value == null || value.trim().isEmpty();
		}
	}
}
