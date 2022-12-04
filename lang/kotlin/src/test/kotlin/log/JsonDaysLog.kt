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
    private val tests = mutableListOf<TestClass>().apply {
        add(TestClass("TestClass", tests = mutableListOf(Test("1. test", status = "failed"))))
    }
    private val buildDirPath = System.getProperty("user.dir") + File.separator + "build" + File.separator

    override fun testPlanExecutionFinished(testPlan: TestPlan?) {
        val reportPath = buildDirPath + "reports" + File.separator + "report.json"
        tests.sortBy { it.name }
        objectMapper.writeValue(Path(reportPath).toFile(), tests)
//        println(testPlan)
    }

    override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) {
        super.executionFinished(testIdentifier, testExecutionResult)
        if (testIdentifier.isTest)
            addToTests(
                (testIdentifier.source?.get() as MethodSource).className,
                Test(testIdentifier.displayName!!, testExecutionResult.status.toString())
            )
        println(testIdentifier)
    }

    private fun addToTests(testClassName: String, test: Test) {
        val testClass = tests.find { it.name == testClassName }
        if (testClass != null) {
            testClass.tests.add(test)
        } else {
            tests.add(TestClass(name = testClassName, tests = mutableListOf(test)))
        }

    }
}

data class Test(val name: String, val status: String)

data class TestClass(val name: String, val tests: MutableList<Test>)