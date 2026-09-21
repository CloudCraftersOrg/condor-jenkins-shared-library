// For apps whose appspec.yml ships the whole repo (source: / in the
// files list), not a single built artifact - zips the workspace as-is,
// minus .git and any bundle.zip left over from a previous run.
def call() {
    sh '''
        rm -f bundle.zip
        zip -rq bundle.zip . -x ".git/*"
    '''
}
