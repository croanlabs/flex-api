package ie.reflexivity.flexer.flexapi

import liquibase.integration.spring.SpringLiquibase
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.AbstractTestExecutionListener

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@TestExecutionListeners(mergeMode =
TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = arrayOf(CleanUpDatabaseExecutionListener::class)
)
annotation class CleanDatabase

class CleanUpDatabaseExecutionListener : AbstractTestExecutionListener() {

    private val log by logger()

    override fun afterTestMethod(testContext: TestContext?) {
        cleanupDatabase(testContext!!)
    }

    private fun cleanupDatabase(testContext: TestContext) {
        log.info("Cleaning up the database")
        val app = testContext.applicationContext
        val springLiquibase = app.getBean(SpringLiquibase::class.java)
        springLiquibase.isDropFirst = true
        springLiquibase.afterPropertiesSet() // The database get recreated here
    }

}
