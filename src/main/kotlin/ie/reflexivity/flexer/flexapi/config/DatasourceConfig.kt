package ie.reflexivity.flexer.flexapi.config

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Value("\${liquibase.change-log}")
    private val changelog: String? = null

    @Value("\${liquibase.contexts}")
    private val contexts: String? = null

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.application_pool")
    fun applicationDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.db_admin_pool")
    fun dbAdminDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    fun liquibase(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = dbAdminDataSource()
        liquibase.changeLog = changelog
        liquibase.contexts = contexts
        return liquibase
    }

}
