// Runs `mvn -B package`. No test-skip flag on purpose - a build that
// bypasses its own tests isn't a build worth deploying.
def call() {
    sh 'mvn -B package'
}
