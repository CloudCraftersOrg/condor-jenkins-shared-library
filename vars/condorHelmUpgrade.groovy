// aws eks update-kubeconfig + helm upgrade --install. extraSet is a list of
// "key=value" strings passed straight through as --set flags, so callers
// can pass app-specific values (env vars, secrets already resolved by the
// caller) without this step needing to know what they mean.
def call(Map config) {
    def setArgs = (config.extraSet ?: []).collect { "--set ${it}" }.join(' ')
    sh """
        export HOME=/root
        aws eks update-kubeconfig --name ${config.clusterName} --region ${config.region ?: 'us-east-1'}
        kubectl get namespace ${config.namespace} || kubectl create namespace ${config.namespace}
        helm upgrade --install ${config.releaseName} ${config.chartPath} -n ${config.namespace} ${setArgs}
    """
}
