// Uploads bundle.zip (from condorZipCodeDeployBundle) and triggers the
// CodeDeploy deployment. Resolves the account ID via STS rather than
// hardcoding it - the same fix P1-11 needed for condor-reportes' own
// Jenkinsfile before it had ever built a real bundle.
def call(Map config) {
    sh """
        ACCOUNT_ID=\$(aws sts get-caller-identity --query Account --output text)
        BUCKET=${config.s3BucketPrefix}-\${ACCOUNT_ID}
        KEY=${env.BUILD_TAG}.zip
        aws s3 cp bundle.zip "s3://\${BUCKET}/\${KEY}"
        aws deploy create-deployment \\
            --application-name ${config.applicationName} \\
            --deployment-group-name ${config.deploymentGroupName} \\
            --s3-location bucket=\${BUCKET},key=\${KEY},bundleType=zip
    """
}
