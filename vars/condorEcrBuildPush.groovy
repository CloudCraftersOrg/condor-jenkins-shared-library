// Builds the Dockerfile in the current workspace, pushes it to ECR tagged
// with the commit SHA, and sets env.IMAGE_DIGEST to the pushed digest -
// callers pin deployments to the digest, not the mutable tag.
def call(Map config) {
    sh """
        ACCOUNT_ID=\$(aws sts get-caller-identity --query Account --output text)
        REPO=\${ACCOUNT_ID}.dkr.ecr.${config.region ?: 'us-east-1'}.amazonaws.com/${config.repository}
        IMAGE="\${REPO}:${env.GIT_COMMIT ?: env.BUILD_TAG}"
        aws ecr get-login-password --region ${config.region ?: 'us-east-1'} | docker login --username AWS --password-stdin "\${REPO}"
        docker build -t "\${IMAGE}" .
        docker push "\${IMAGE}"
        DIGEST=\$(aws ecr describe-images --repository-name ${config.repository} --image-ids imageTag=${env.GIT_COMMIT ?: env.BUILD_TAG} --query 'imageDetails[0].imageDigest' --output text)
        echo "\${DIGEST}" > .image-digest
    """
    env.IMAGE_DIGEST = readFile('.image-digest').trim()
}
