def increment() {
    echo "incrementing the app version..."
    sh "mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit"
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_TAG = "$version-$BUILD_NUMBER"
}

def buildJar() {
    echo "building the application..."
    sh "mvn clean package"
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh "docker build -t jamoraa/demo-java-maven-app:${IMAGE_TAG} ."
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push jamoraa/demo-java-maven-app:${IMAGE_TAG}"
    }
}

def deployApp() {
    echo "deploying the application..."
}

def gitPush() {
    echo "pushing version change to github repository..."
    withCredentials([string(credentialsId: 'github-token', variable: 'TOKEN')]) {
        sh "git config --global user.email 'jenkins@example.com'"
        sh "git config --global user.name 'jenkins'"

        sh "git status"
        sh "git branch"
        sh "git config --list"

        sh "git remote set-url origin https://${TOKEN}@github.com/jamorAA/jenkins-practice.git"
        sh "git add pom.xml"
        sh "git commit -m 'ci: version bump'"
        sh "git push origin HEAD:main"
    }
}
return this
