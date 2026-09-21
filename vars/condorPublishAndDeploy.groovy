// Uploads bundle.zip (from condorZipCodeDeployBundle or
// condorZipWorkspaceBundle) and triggers the CodeDeploy deployment.
// Resolves the account ID via STS rather than hardcoding it - the same fix
// P1-11 needed for condor-reportes' own Jenkinsfile before it had ever
// built a real bundle.
def call(Map config) {
    // bucket: for apps whose artifact bucket has a CFN-random name (e.g.
    // Tienda's condor-tienda-artifactbucket-<suffix>), not the predictable
    // <s3BucketPrefix>-<account-id> pattern condorPagos-style apps use.
    def bucketExpr = config.bucket ? "'${config.bucket}'" : "${config.s3BucketPrefix}-\${ACCOUNT_ID}"
    sh """
        ACCOUNT_ID=\$(aws sts get-caller-identity --query Account --output text)
        BUCKET=${bucketExpr}
        KEY=${env.BUILD_TAG}.zip
        aws s3 cp bundle.zip "s3://\${BUCKET}/\${KEY}"
        aws deploy create-deployment \\
            --application-name ${config.applicationName} \\
            --deployment-group-name ${config.deploymentGroupName} \\
            --s3-location bucket=\${BUCKET},key=\${KEY},bundleType=zip
    """
}
