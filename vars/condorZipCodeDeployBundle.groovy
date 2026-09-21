// Zips a CodeDeploy bundle matching the shape every condor app's
// appspec.yml expects: <appName>.jar at the root, the systemd unit under
// deploy/, start.sh+stop.sh under scripts/, appspec.yml at the root.
def call(Map config) {
    sh """
        rm -rf bundle bundle.zip
        mkdir -p bundle/deploy bundle/scripts
        cp ${config.jar} bundle/${config.appName}.jar
        cp ${config.serviceUnit} bundle/deploy/
        cp scripts/start.sh scripts/stop.sh bundle/scripts/
        cp appspec.yml bundle/
        cd bundle && zip -r ../bundle.zip .
    """
}
