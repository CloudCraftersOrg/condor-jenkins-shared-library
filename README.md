# condor-jenkins-shared-library

Reusable Jenkins pipeline steps for the Condor estate apps, registered on
`condor-jenkins` as the global library `condor-shared`.

Used by `condor-reportes`, `condor-tienda`, and `condor-pagos`. Tienda and
Pagos also keep their original pipelines (CodePipeline and GitHub Actions
respectively) — these Jenkins pipelines run alongside them, not instead of
them.

## Steps

| Step | Purpose |
|---|---|
| `condorMavenBuild()` | `mvn -B package` |
| `condorZipCodeDeployBundle(...)` | Build a CodeDeploy zip from a single artifact (jar + systemd unit + scripts + appspec.yml) |
| `condorZipWorkspaceBundle()` | Build a CodeDeploy zip from the whole workspace (apps whose appspec.yml ships the whole repo) |
| `condorPublishAndDeploy(...)` | Upload a CodeDeploy bundle to S3, trigger `aws deploy create-deployment` |
| `condorEcrBuildPush(...)` | Build the workspace Dockerfile, push to ECR tagged with the commit SHA, set `env.IMAGE_DIGEST` |
| `condorHelmUpgrade(...)` | `aws eks update-kubeconfig` + `helm upgrade --install` |
| `condorJavaCodeDeployPipeline(...)` | The full Maven + CodeDeploy Build+Deploy pipeline in one call |

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
