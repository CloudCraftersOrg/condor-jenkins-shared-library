# condor-jenkins-shared-library

Reusable Jenkins pipeline steps for the Condor estate apps, registered on
`condor-jenkins` as the global library `condor-shared`.

Currently used by `condor-reportes` (the only estate app whose pipeline
runs on Jenkins today — Tienda deploys through CodePipeline, Pagos through
GitHub Actions). Written generically so any future Java + CodeDeploy app
on Jenkins can adopt it directly.

## Steps

| Step | Purpose |
|---|---|
| `condorMavenBuild()` | `mvn -B package` |
| `condorZipCodeDeployBundle(...)` | Build the CodeDeploy zip (jar + systemd unit + scripts + appspec.yml) |
| `condorPublishAndDeploy(...)` | Upload the bundle to S3, trigger `aws deploy create-deployment` |
| `condorJavaCodeDeployPipeline(...)` | The full Build+Deploy pipeline in one call |

See each step's `.txt` file in `vars/` for parameters and an example.

## Using it in a Jenkinsfile

```groovy
@Library('condor-shared') _

condorJavaCodeDeployPipeline(
    appName: 'condor-reportes',
    applicationName: 'condor-reportes',
    deploymentGroupName: 'condor-reportes-prod',
    s3BucketPrefix: 'condor-reportes-artifacts',
)
```
