// Full pipeline for a Maven app deployed to an ASG via CodeDeploy - the
// shape condor-reportes' own Jenkinsfile used before this library existed.
// No test stage, no input step: those are load-bearing (P1-11, AK-PIP-06),
// not an oversight - do not add them to this step.
def call(Map config) {
    // Declarative pipeline's environment{} block only accepts literals,
    // interpolated strings, or function calls - not the ?: expression
    // directly (confirmed live: CpsCompilationErrorsException). Resolve it
    // first, then interpolate the plain variable.
    def region = config.awsRegion ?: 'us-east-1'

    pipeline {
        agent any

        environment {
            AWS_DEFAULT_REGION = "${region}"
        }

        stages {
            stage('Build') {
                steps {
                    condorMavenBuild()
                }
            }
            stage('Deploy') {
                steps {
                    condorZipCodeDeployBundle(
                        jar: config.jar ?: "target/${config.appName}.jar",
                        appName: config.appName,
                        serviceUnit: config.serviceUnit ?: "deploy/${config.appName}.service",
                    )
                    condorPublishAndDeploy(
                        s3BucketPrefix: config.s3BucketPrefix,
                        applicationName: config.applicationName,
                        deploymentGroupName: config.deploymentGroupName,
                    )
                }
            }
        }
    }
}
