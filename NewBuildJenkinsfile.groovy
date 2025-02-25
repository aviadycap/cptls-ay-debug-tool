@Library('jenkins-libs')
import capitolis.jenkins.utils.*
import capitolis.jenkins.ci.*

node ("slave_builder") {

    def pipeline = new MavenPipeline(this)
    pipeline.projectName = "cptls-ay-debug-tool"
    pipeline.dockerImageName = "cptls-ay-debug-tool"
    pipeline.autoDeployToEdge2 = false
    pipeline.productName = "market-data"

    pipeline.buildMvnCmd = "mvn package -DskipTests -f /root/pom.xml"

    pipeline.setBranchProperties()
    println "Service is " + pipeline.projectName
    println "Branch is " + pipeline.branchName

    pipeline.run()
}
