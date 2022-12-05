package test.kotlin.log

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.support.descriptor.MethodSource
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import java.io.File
import kotlin.io.path.Path

class JsonDaysLog : TestExecutionListener {

    private val objectMapper = ObjectMapper()
    private val modernTests = mutableMapOf<String, MutableList<Test>>()
    private val buildDirPath = System.getProperty("user.dir") + File.separator + "build" + File.separator

    override fun testPlanExecutionFinished(testPlan: TestPlan?) {
        val reportPath = buildDirPath + "reports" + File.separator + "report.json"
        objectMapper.writeValue(Path(reportPath).toFile(), modernTests.toSortedMap())
        println("Json report done!")
    }

    override fun executionFinished(testIdentifier: TestIdentifier, result: TestExecutionResult) {
        if (testIdentifier.isTest)
            addToTestsModern(
                testClassName = testIdentifier.className,
                test = Test(testIdentifier.displayName, result.status.name, result.throwableString)
            )
    }

    private fun addToTestsModern(testClassName: String, test: Test) {
        modernTests.getOrPut(testClassName, ::mutableListOf).add(test)
    }

    private val TestIdentifier.className
        get() = (source?.get() as MethodSource).className.substringAfterLast('.')

    private val TestExecutionResult.throwableString
        get() = (if (throwable.isPresent) throwable.get().toString() else null)
}

data class Test(val name: String, val status: String, val error: String?)