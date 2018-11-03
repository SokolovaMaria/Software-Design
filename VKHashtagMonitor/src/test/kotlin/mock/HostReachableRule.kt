package mock

import org.junit.rules.ExternalResource
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 1000L

class HostReachableRule : TestRule {
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
    annotation class HostReachable(val host: String)

    private fun pingHost(host: String) = nativePing("ping", host) || nativePing("ping6", host)

    private fun nativePing(cmd: String, host: String): Boolean {
        try {
            val p = ProcessBuilder(cmd, "-c", "1", host).start()
            if (!p.waitFor(TIMEOUT, TimeUnit.MILLISECONDS)) {
                return false
            }
            return p.exitValue() == 0
        } catch(e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        val hostReachableAnnotation = description.getAnnotation(HostReachable::class.java)
        if (hostReachableAnnotation != null) {
            if (!pingHost(hostReachableAnnotation.host)) {
                return FailedStatement(hostReachableAnnotation.host)
            }
        }
        return base
    }



    private class FailedStatement(val host: String) : Statement() {
        override fun evaluate() {
            System.err.println("Skipped: $host is not reachable")
        }
    }
}